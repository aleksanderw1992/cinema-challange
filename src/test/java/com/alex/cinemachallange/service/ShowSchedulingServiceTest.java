package com.alex.cinemachallange.service;

import com.alex.cinemachallange.domain.Movie;
import com.alex.cinemachallange.domain.Planner;
import com.alex.cinemachallange.domain.Room;
import com.alex.cinemachallange.domain.Show;
import com.alex.cinemachallange.exceptions.InvalidPremiereTimeException;
import com.alex.cinemachallange.exceptions.RoomUnavailableException;
import com.alex.cinemachallange.exceptions.ShowOverlapException;
import com.alex.cinemachallange.repository.ShowRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShowSchedulingServiceTest {

    @InjectMocks
    private ShowSchedulingService service;

    @Mock
    private ShowRepository showRepository;

    private Movie movie;
    private Room room;
    private Planner planner;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        movie = new Movie("Inception", Duration.ofHours(2), false, false);
        room = new Room("Room A", Duration.ofMinutes(30), Collections.emptyList()); // Empty unavailable dates
        startTime = LocalDateTime.of(2023, 10, 10, 19, 0);
        endTime = LocalDateTime.of(2023, 10, 10, 21, 0);
        planner = new Planner("Jadwiga");
    }

    @Test
    void shouldScheduleShow_whenNoOverlapAndRoomAvailable() {
        when(showRepository.findByRoomAndStartTimeBetweenAndEndTimeBetween(any(), any(), any(), any(), any()))
            .thenReturn(Collections.emptyList());

        Optional<Show> result = service.scheduleShow(planner, movie, room, startTime, endTime);

        assertTrue(result.isPresent());
        verify(showRepository, times(1)).save(any(Show.class));
    }

    @Test
    void shouldThrowException_whenOverlapExists() {
        Show overlappingShow = new Show(movie, room, startTime, endTime, planner);
        when(showRepository.findByRoomAndStartTimeBetweenAndEndTimeBetween(any(), any(), any(), any(), any()))
            .thenReturn(Collections.singletonList(overlappingShow));

        assertThrows(ShowOverlapException.class, () -> service.scheduleShow(planner, movie, room, startTime, endTime));
    }

    @Test
    void shouldThrowException_whenRoomNotAvailable() {
        LocalDate unavailableDate = LocalDate.of(2023, 10, 10);
        room.setUnavailableDates(Collections.singletonList(unavailableDate));

        assertThrows(RoomUnavailableException.class, () -> service.scheduleShow(planner, movie, room, startTime, endTime));
    }

    @Test
    void shouldThrowException_whenPremierNotInCorrectTime() {
        Movie premierMovie = new Movie("Inception", Duration.ofHours(2), false, true);
        startTime = LocalDateTime.of(2023, 10, 10, 15, 0);
        endTime = LocalDateTime.of(2023, 10, 10, 17, 0);

        assertThrows(InvalidPremiereTimeException.class, () -> service.scheduleShow(planner, premierMovie, room, startTime, endTime));
    }

    @Test
    void shouldScheduleShow_whenPremierInCorrectTime() {
        Movie premierMovie = new Movie("Inception", Duration.ofHours(2), false, true);
        startTime = LocalDateTime.of(2023, 10, 10, 18, 0);
        endTime = LocalDateTime.of(2023, 10, 10, 20, 0);

        when(showRepository.findByRoomAndStartTimeBetweenAndEndTimeBetween(any(), any(), any(), any(), any()))
            .thenReturn(Collections.emptyList());

        Optional<Show> result = service.scheduleShow(planner, premierMovie, room, startTime, endTime);
        assertTrue(result.isPresent());
    }
}
