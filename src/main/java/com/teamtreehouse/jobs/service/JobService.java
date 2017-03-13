package com.teamtreehouse.jobs.service;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;

import com.teamtreehouse.jobs.model.Job;
import com.teamtreehouse.jobs.model.ResultsPage;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class JobService {
  private static final String CACHE_PATH = "./src/main/resources/cached";
  static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
  static final JsonFactory JSON_FACTORY = new JacksonFactory();

  public static class IndeedUrl extends GenericUrl {
    @Key
    public String publisher;

    @Key
    public String format = "json";

    @Key
    public String v = "2";

    @Key
    public String fromage = "1";  // 1 day old, not French for cheese

    @Key
    public String q;

    @Key
    public int start;

    @Key
    public int limit = 25;

    public IndeedUrl() {
      super("http://api.indeed.com/ads/apisearch");
    }

  }

  public void refresh() throws IOException {
    cache(search());
  }

  public List<HttpResponse> search() throws IOException {
    JsonObjectParser parser = new JsonObjectParser.Builder(JSON_FACTORY).build();
    HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(req -> req.setParser(parser));
    Properties config = new Properties();
    config.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
    String publisher = config.getProperty("api.indeed.publisher");
    // Execute a quick search just to get the total
    IndeedUrl estimateUrl = new IndeedUrl();
    estimateUrl.publisher = publisher;
    estimateUrl.q = "java";
    estimateUrl.limit = 1;
    ResultsPage estimate = requestFactory
            .buildGetRequest(estimateUrl)
            .execute()
            .parseAs(ResultsPage.class);

    System.out.println("Estimated results: " + estimate.totalResults);
    return IntStream.range(0, Math.min(estimate.totalResults, 1000)).parallel()
            .filter(start -> start % ResultsPage.MAX_PER_PAGE == 0)
            .mapToObj(start -> {
              IndeedUrl url = new IndeedUrl();
              url.publisher = publisher;
              url.q = "java";
              url.start = start;
              url.limit = ResultsPage.MAX_PER_PAGE;
              try {
                return requestFactory.buildGetRequest(url);
              } catch (IOException e) {
                throw new UncheckedIOException(e);
              }
            })
            .map(request -> {
              try {
                return request.execute();
              } catch (IOException e) {
                throw new UncheckedIOException(e);
              }
            })
            .collect(toList());
  }

  public void cache(List<HttpResponse> responses) throws IOException {
    // Remove all files in the cache
    Files.walk(Paths.get(CACHE_PATH))
            .filter(Files::isRegularFile)
            .map(Path::toFile)
            .forEach(File::delete);

    // Loop through responses and drop them out here
    IntStream.range(0, responses.size()).parallel()
            .forEach(i -> {
              HttpResponse response = responses.get(i);
              try {
                response.download(
                        Files.newOutputStream(Paths.get(CACHE_PATH, "jobs-" + (i + 1) + ".json")));
              } catch (IOException e) {
                throw new UncheckedIOException(e);
              }
            });
  }

  public List<Job> loadJobs() throws IOException {
    JsonObjectParser parser = new JsonObjectParser.Builder(JSON_FACTORY).build();
    List<ResultsPage> pages = Files.walk(Paths.get(CACHE_PATH))
            .filter(Files::isRegularFile)
            .map(path -> {
              try {
                return parser.parseAndClose(Files.newBufferedReader(path), ResultsPage.class);
              } catch (IOException e) {
                throw new UncheckedIOException(e);
              }
            })
            .collect(toList());
    return pages.stream()
            .flatMap(page -> page.jobs.stream())
            .collect(toList());
  }



}
