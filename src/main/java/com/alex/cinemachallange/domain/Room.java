package com.alex.cinemachallange.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Aggregate root.
 * Represents a cinema room and its availability.
 */
public class Room {
    private final String id;
    private final String name;
    private Duration cleaningDuration;
    private List<LocalDate> unavailableDates; // Dates when the room is unavailable.

  public Room(String name, Duration cleaningDuration, List<LocalDate> unavailableDates) {
    this.id = String.valueOf(UUID.randomUUID());
    this.name = name;
    this.cleaningDuration = cleaningDuration;
    this.unavailableDates = Collections.unmodifiableList(unavailableDates);
  }

  /**
     * Checks if the room is available on a given date.
     * @param date The date to check availability.
     * @return Whether the room is available on the given date.
     */
    public boolean isAvailableOn(LocalDate date) {
        return !unavailableDates.contains(date);
    }
}
