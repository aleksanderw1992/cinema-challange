package com.alex.cinemachallange.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Aggregate root.
 * Represents a scheduled movie screening. Invariants - no overlapping.
 */
public class Show {

    public static final LocalTime PREMIERE_START = LocalTime.of(17, 0);
    public static final LocalTime PREMIERE_END = LocalTime.of(21, 0);
    private final String id;
    private final Movie movie;
    private final Room room;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Planner scheduledBy;
    private final boolean isPremier;


    /**
     * Constructor for Show class
     *
     * @param movie Movie to be scheduled
     * @param room Room in which movie will be played
     * @param startTime Start time of the movie
     * @param endTime End time of the movie
     * @param scheduledBy Planner who scheduled the movie
     * @param isPremier Whether the show is a premiere
     */
    public Show(Movie movie, Room room, LocalDateTime startTime, LocalDateTime endTime, Planner scheduledBy, boolean isPremier) {
        this.id = String.valueOf(UUID.randomUUID());
        this.movie = movie;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.scheduledBy = scheduledBy;
        this.isPremier = isPremier;

        if (isPremier && !(startTime.toLocalTime().isAfter(PREMIERE_START) && endTime.toLocalTime().isBefore(PREMIERE_END))) {
            throw new IllegalArgumentException("Premier shows must be scheduled between 17:00 and 21:00.");
        }
    }

    /**
     * Ensures the show doesn't overlap with another.
     * @param otherShow Another show to check overlap against.
     * @return Whether this show overlaps with the other show.
     */
    public boolean overlapsWith(Show otherShow) {
        return this.room.equals(otherShow.room) &&
               (this.startTime.isBefore(otherShow.endTime) && this.endTime.isAfter(otherShow.startTime));
    }
    // getters & setters if exist

    public String getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Planner getScheduledBy() {
        return scheduledBy;
    }

    public boolean isPremier() {
        return isPremier;
    }
}
