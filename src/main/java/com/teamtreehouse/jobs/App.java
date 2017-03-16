package com.teamtreehouse.jobs;

import com.teamtreehouse.jobs.model.Job;
import com.teamtreehouse.jobs.service.JobService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class App {

  public static void main(String[] args) {
    JobService service = new JobService();
    boolean shouldRefresh = false;
    try {
      if (shouldRefresh) {
        service.refresh();
      }
      List<Job> jobs = service.loadJobs();
      System.out.printf("Total jobs:  %d %n %n", jobs.size());
      explore(jobs);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void explore(List<Job> jobs) {
    // Your amazing code below...

   Function<String, String> converter = createDateStringConverter(
       DateTimeFormatter.RFC_1123_DATE_TIME,
       DateTimeFormatter.ISO_DATE
   );

    jobs.stream()
        .map(Job::getDateTimeString)
        .map(converter)
        .limit(5)
        .forEach(System.out::println);

  }

  public static Function<String, String> createDateStringConverter(
      DateTimeFormatter inFormatter,
      DateTimeFormatter outFormatter) {
    int meaningOfLife = 42;
    return dateString -> {
      return meaningOfLife + "------" + LocalDateTime.parse(dateString, inFormatter)
          .format(outFormatter);
    };
  }


  public static void emailIfMatches(Job job, Predicate<Job> checker) {
    if (checker.test(job)) {
      System.out.println("I am sending an email about " + job);
    }
  }

  private static void displayCompaniesMenuImperatively(List<String> companies) {
    for (int i =0; i < 20; i++) {
      System.out.printf("%d. %s %n", i + 1, companies.get(i));
    }
  }

  private static void displayCompaniesMenuUsingRange(List<String> companies) {
    IntStream.rangeClosed(1, 20)
        .mapToObj(i -> String.format("%d. %s", i, companies.get(i - 1)))
        .forEach(System.out::println);
  }


    private static Optional<Job> luckySearchJob(List<Job> jobs, String searchTerm) {
    return jobs.stream()
          .filter(job -> job.getTitle().contains(searchTerm))
          .findFirst();
  }

  /*
  Job / snippet / This is a job
  Job / snippet / Also a job
   */
  public static Map<String, Long> getSnippetWordCountsStream(List<Job> jobs) {
    return jobs.stream()
        .map(Job::getSnippet)
        .map(snippet -> snippet.split("\\W+"))
        .flatMap(Stream::of)
        .filter(word -> word.length() > 0)
        .map(String::toLowerCase)
        .collect(Collectors.groupingBy(
            Function.identity(),
            Collectors.counting()
        ));
  }

  public static Map<String, Long> getSnippetWordCountsImperatively(List<Job> jobs) {

    Map<String, Long> wordCounts = new HashMap<>();

    for (Job job : jobs) {
      String[] words = job.getSnippet().split("\\W+");
      for (String word : words) {
        if (word.length() == 0) {
          continue;
        }
        String lWord = word.toLowerCase();
        Long count = wordCounts.get(lWord);
        if (count == null) {
          count = 0L;
        }
        wordCounts.put(lWord, ++count);
      }
    }
    return wordCounts;
  }

  private static boolean isJuniorJob(Job job) {
    String title = job.getTitle().toLowerCase();
    return title.contains("junior") || title.contains("jr");
  }


  //  "Senior Dev", "Jr.  Java Engineer", "Java Evangelist", "Junior Java Dev",
  //  "Sr. Java Wizard Ninja", "Junior Java Wizard Ninja", "Full Stack Java Engineer"
  private static List<Job> getThreeJuniorJobsStream(List<Job> jobs) {
    return jobs.stream()
        .filter(App::isJuniorJob)
        .limit(3)
        .collect(Collectors.toList());
  }

  private static List<Job> getThreeJuniorJobsImperatively(List<Job> jobs) {
    List<Job> juniorJobs = new ArrayList<>();
    for (Job job : jobs) {
      if (isJuniorJob(job)) {
        juniorJobs.add(job);
        if (juniorJobs.size() >= 3) {
          break;
        }
      }
    }
    return juniorJobs;
  }

  private static List<String> getCaptionsStream(List<Job> jobs) {
    return jobs.stream()
        .filter(App::isJuniorJob)
        .map(Job::getCaption)
        .limit(3)
        .collect(Collectors.toList());
  }



  private static List<String> getCaptionsImperatively(List<Job> jobs) {
    List<String> captions = new ArrayList<>();
    for (Job job : jobs) {
      if (isJuniorJob(job)) {
        captions.add(job.getCaption());
        if (captions.size() >= 3) {
          break;
        }
      }
    }
    return captions;
  }

  private static void printPortlandJobsStream(List<Job> jobs) {
    jobs.stream()
        .filter(job -> job.getState().equals("OR"))
        .filter(job -> job.getCity().equals("Portland"))
        .forEach(System.out::println);
  }

  private static void printPortlandJobsImperatively(List<Job> jobs) {
    for (Job job : jobs) {
      if (job.getState().equals("OR") && job.getCity().equals("Portland")) {
        System.out.println(job);
      }
    }
  }
}
