package com.teamtreehouse.jobs.model;

import com.google.api.client.util.Key;

import java.util.List;

public class ResultsPage {
  public static int MAX_PER_PAGE = 25;

  @Key("results")
  public List<Job> jobs;

  @Key
  public int start;

  @Key
  public int end;

  @Key
  public int pageNumber;


  @Key
  public int totalResults;

  @Override
  public String toString() {
    return "ResultsPage{" +
            "jobs.size=" + jobs.size() +
            ", start=" + start +
            ", end=" + end +
            ", pageNumber=" + pageNumber +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ResultsPage)) return false;

    ResultsPage that = (ResultsPage) o;

    if (start != that.start) return false;
    if (end != that.end) return false;
    if (pageNumber != that.pageNumber) return false;
    if (totalResults != that.totalResults) return false;
    return jobs != null ? jobs.equals(that.jobs) : that.jobs == null;
  }

  @Override
  public int hashCode() {
    int result = jobs != null ? jobs.hashCode() : 0;
    result = 31 * result + start;
    result = 31 * result + end;
    result = 31 * result + pageNumber;
    result = 31 * result + totalResults;
    return result;
  }
}
