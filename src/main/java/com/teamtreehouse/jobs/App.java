package com.teamtreehouse.jobs;

import com.teamtreehouse.jobs.model.Job;
import com.teamtreehouse.jobs.service.JobService;

import java.io.IOException;
import java.util.List;

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
    // "Louisville, KY", "Bend, OR"
    printPortlandJobsStream(jobs);
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
