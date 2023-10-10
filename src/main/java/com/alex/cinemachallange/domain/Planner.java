package com.alex.cinemachallange.domain;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity. Represents the individual responsible for scheduling. Mainly for Jadwiga.
 */
public class Planner {

  private final String id;
  private final String name;

  public Planner(String name) {
    this.id = String.valueOf(UUID.randomUUID());
    this.name = name;
  }

  /**
   * Schedules a show ensuring all constraints are met.
   *
   * @param movie The movie to be scheduled.
   * @param room The room where the movie will be shown.
   * @param startTime Start of show
   * @param endTime End of show
   * @param isPremiere If show is premiere
   * @return The scheduled show or null if scheduling was not possible.
   */
  public Show scheduleShow(Movie movie, Room room, LocalDateTime startTime, LocalDateTime endTime, boolean isPremiere) {
    Show potentialShow = new Show(movie, room, startTime, endTime, this);
    if (room.isAvailableOn(startTime.toLocalDate())) {
      // If all constraints are satisfied:
      return potentialShow;
    }
    return null;
  }
// getters & setters if exist

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}