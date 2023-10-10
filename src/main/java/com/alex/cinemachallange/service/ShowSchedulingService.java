package com.alex.cinemachallange.service;

import com.alex.cinemachallange.domain.Movie;
import com.alex.cinemachallange.domain.Planner;
import com.alex.cinemachallange.domain.Room;
import com.alex.cinemachallange.domain.Show;
import com.alex.cinemachallange.repository.ShowRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service to handle the scheduling and business rules for shows.
 */
public class ShowSchedulingService {

  private ShowRepository showRepository;

  public ShowSchedulingService(ShowRepository showRepository) {
    this.showRepository = showRepository;
  }

  /**
   * Attempts to schedule a show. Validates against overlapping, cleaning time, premier hours, and room availability.
   *
   * @param planner The planner scheduling the show.
   * @param movie The movie to schedule.
   * @param room The room for the movie.
   * @param startTime The start of show.
   * @param endTime The end of the show.
   * @return the scheduled show if successful; otherwise null.
   */
  public Optional<Show> scheduleShow(Planner planner, Movie movie, Room room, LocalDateTime startTime, LocalDateTime endTime) {
    // First, let's check room availability.
    if (!room.isAvailableOn(startTime.toLocalDate())) {
      return Optional.empty();  // The room is not available on this date.
    }

    // Check if it's a premier movie and if the time is appropriate.
    Show potentialShow;
    try {
      potentialShow = new Show(movie, room, startTime, endTime, planner);
    } catch (IllegalArgumentException e) {
      return Optional.empty();  // The scheduled time is not after working hours for a premier.
    }

    // Now, let's check overlaps with existing shows.
    LocalDateTime cleaningEndTime = endTime.plus(room.getCleaningDuration());
    List<Show> overlappingShows = showRepository.findByRoomAndStartTimeBetweenAndEndTimeBetween(
        room, startTime, cleaningEndTime, startTime, cleaningEndTime
    );

    for (Show show : overlappingShows) {
      if (potentialShow.overlapsWith(show)) {
        return Optional.empty();  // Overlaps with an existing show.
      }
    }

    // If we've passed all the checks, we can schedule the show.
    showRepository.saveAll(Collections.singletonList(potentialShow));
    return Optional.of(potentialShow);
  }

  /**
   * Reschedules an existing show to a new time slot.
   *
   * @param show The show to be rescheduled.
   * @param startTime The start of show.
   * @param endTime The end of the show.
   * @return the rescheduled show if successful.
   */
  public Optional<Show> rescheduleShow(Show show,  LocalDateTime startTime, LocalDateTime endTime) {
    // todo
    return Optional.empty();
  }

  /**
   * Cancels a scheduled show.
   *
   * @param show The show to be canceled.
   * @return true if the cancellation was successful, false otherwise.
   */
  public boolean cancelShow(Show show) {
    // todo
    return false;
  }

  /**
   *
   * @param room
   * @param startTime
   * @param endTime
   * @return
   */
  public List<Show> fetchShowsForRoomAndTimeFrame(Room room, LocalDateTime startTime, LocalDateTime endTime) {
    // todo
    return Collections.emptyList();
  }

  /**
   *
   * @param movie
   * @param planner
   * @return
   */
  public List<Show> fetchShowsForMovieOrPlanner(Movie movie, Planner planner) {
    // Implementation goes here...
    return Collections.emptyList();
  }
}