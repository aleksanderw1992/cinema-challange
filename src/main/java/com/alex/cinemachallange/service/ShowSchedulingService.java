package com.alex.cinemachallange.service;

import com.alex.cinemachallange.domain.Movie;
import com.alex.cinemachallange.domain.Planner;
import com.alex.cinemachallange.domain.Room;
import com.alex.cinemachallange.domain.Show;
import com.alex.cinemachallange.exceptions.InvalidPremiereTimeException;
import com.alex.cinemachallange.exceptions.RoomUnavailableException;
import com.alex.cinemachallange.exceptions.ShowOverlapException;
import com.alex.cinemachallange.repository.ShowRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service to handle the scheduling and business rules for shows.
 */
@Service
public class ShowSchedulingService {

  private static final Logger logger = LoggerFactory.getLogger(ShowSchedulingService.class);
  private final ShowRepository showRepository;

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
   * @return the scheduled show if successful.
   */
  @Transactional
  public Optional<Show> scheduleShow(Planner planner, Movie movie, Room room, LocalDateTime startTime, LocalDateTime endTime) {
    logger.info("Scheduling a show for movie '{}' in room '{}' from {} to {}", movie.getTitle(), room.getName(), startTime, endTime);

    // Room availability check.
    if (!room.isAvailableOn(startTime.toLocalDate())) {
      logger.warn("Room '{}' is not available on date {}", room.getName(), startTime.toLocalDate());
      throw new RoomUnavailableException("Room is not available on this date.");
    }

    // Premier movie time validation.
    validatePremiereTime(movie, startTime, endTime);

    // Overlapping shows check.
    LocalDateTime cleaningEndTime = endTime.plus(room.getCleaningDuration());
    validateOverlappingShows(room, startTime, cleaningEndTime);

    // Schedule the show.
    Show scheduledShow = new Show(movie, room, startTime, endTime, planner);
    showRepository.save(scheduledShow);
    logger.info("Successfully scheduled the show for movie '{}' in room '{}'", movie.getTitle(), room.getName());

    return Optional.of(scheduledShow);
  }

  private void validatePremiereTime(Movie movie, LocalDateTime startTime, LocalDateTime endTime) {
    if (movie.isPremier() && !(startTime.toLocalTime().isAfter(Show.PREMIERE_START) && endTime.toLocalTime().isBefore(Show.PREMIERE_END))) {
      logger.warn("Attempted to schedule a premier show for movie '{}' outside of the allowed premiere hours", movie.getTitle());
      throw new InvalidPremiereTimeException("Premier shows must be scheduled between 17:00 and 21:00.");
    }
  }

  private void validateOverlappingShows(Room room, LocalDateTime startTime, LocalDateTime cleaningEndTime) {
    List<Show> overlappingShows = showRepository.findByRoomAndStartTimeBetweenAndEndTimeBetween(
        room, startTime, cleaningEndTime, startTime, cleaningEndTime
    );

    for (Show show : overlappingShows) {
      if (show.getStartTime().isBefore(cleaningEndTime) && show.getEndTime().isAfter(startTime)) {
        logger.warn("Detected an overlap with another show when trying to schedule in room '{}' from {} to {}", room.getName(), startTime, cleaningEndTime);
        throw new ShowOverlapException("The show overlaps with another scheduled show.");
      }
    }
  }

  /**
   * Reschedules an existing show to a new time slot.
   *
   * @param show The show to be rescheduled.
   * @param startTime The start of show.
   * @param endTime The end of the show.
   * @return the rescheduled show if successful.
   */
  public Optional<Show> rescheduleShow(Show show, LocalDateTime startTime, LocalDateTime endTime) {
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
   * Fetches shows for a given room and timeframe.
   *
   * @param room Room to filter shows by.
   * @param startTime Start of the timeframe.
   * @param endTime End of the timeframe.
   * @return List of shows for the room and timeframe.
   */
  public List<Show> fetchShowsForRoomAndTimeFrame(Room room, LocalDateTime startTime, LocalDateTime endTime) {
    // todo
    return Collections.emptyList();
  }

  /**
   * Fetches shows either for a given movie or a planner.
   *
   * @param movie Movie to filter shows by.
   * @param planner Planner to filter shows by.
   * @return List of shows filtered by the movie or planner.
   */
  public List<Show> fetchShowsForMovieOrPlanner(Movie movie, Planner planner) {
    // Implementation goes here...
    return Collections.emptyList();
  }
}
