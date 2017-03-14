package com.teamtreehouse.jobs;

import com.teamtreehouse.jobs.model.Job;
import com.teamtreehouse.jobs.service.JobService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    getThreeJuniorJobsStream(jobs).forEach(System.out::println);
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
