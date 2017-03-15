package com.teamtreehouse.jobs.model;

import com.google.api.client.util.Key;

public class Job {
  @Key
  private String jobtitle;

  @Key
  private String company;


  @Key
  private String city;


  @Key
  private String state;


  @Key
  private String country;


  @Key
  private String snippet;

  @Key
  private String date;

  public String getDateTimeString() {
    return date;
  }



  public String getTitle() {
    return jobtitle;
  }

  public String getCompany() {
    return company;
  }

  public String getCity() {
    return city;
  }

  public String getState() {
    return state;
  }

  public String getCountry() {
    return country;
  }

  public String getSnippet() {
    return snippet;
  }

  @Override
  public String toString() {
    return "Job{" +
            "title='" + jobtitle + '\'' +
            ", company='" + company + '\'' +
            ", city='" + city + '\'' +
            ", state='" + state + '\'' +
            ", country='" + country + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Job)) return false;

    Job job = (Job) o;

    if (!jobtitle.equals(job.jobtitle)) return false;
    if (!company.equals(job.company)) return false;
    if (city != null ? !city.equals(job.city) : job.city != null) return false;
    if (state != null ? !state.equals(job.state) : job.state != null) return false;
    if (country != null ? !country.equals(job.country) : job.country != null) return false;
    if (snippet != null ? !snippet.equals(job.snippet) : job.snippet != null) return false;
    return date != null ? date.equals(job.date) : job.date == null;
  }

  @Override
  public int hashCode() {
    int result = jobtitle.hashCode();
    result = 31 * result + company.hashCode();
    result = 31 * result + (city != null ? city.hashCode() : 0);
    result = 31 * result + (state != null ? state.hashCode() : 0);
    result = 31 * result + (country != null ? country.hashCode() : 0);
    result = 31 * result + (snippet != null ? snippet.hashCode() : 0);
    result = 31 * result + (date != null ? date.hashCode() : 0);
    return result;
  }

  public String getCaption() {
    return String.format("%s is looking for a %s in %s",
        getCompany(),
        getTitle(),
        getCity());
  }
}
