package com.alex.cinemachallange.domain;

import java.time.Duration;
import java.util.UUID;

/**
 * Aggregate root. Represents a movie with necessary details for scheduling.
 */
public class Movie {

  private final String id;
  private final String title;
  private final Duration duration;
  private final boolean requires3DGlasses;
  private final boolean isPremier;

  public Movie(String title, Duration duration, boolean requires3DGlasses, boolean isPremier) {
    this.id = String.valueOf(UUID.randomUUID());
    this.title = title;
    this.duration = duration;
    this.requires3DGlasses = requires3DGlasses;
    this.isPremier = isPremier;
  }

  // getters & setters if exist
  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public Duration getDuration() {
    return duration;
  }

  public boolean isRequires3DGlasses() {
    return requires3DGlasses;
  }

  public boolean isPremier() {
    return isPremier;
  }
}