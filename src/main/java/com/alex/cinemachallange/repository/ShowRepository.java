package com.alex.cinemachallange.repository;

import com.alex.cinemachallange.domain.Room;
import com.alex.cinemachallange.domain.Show;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for storing and retrieving shows.
 */
@Repository
public interface ShowRepository extends JpaRepository<Show, String> {


  List<Show> findByRoomAndStartTimeBetweenAndEndTimeBetween(Room room,
      LocalDateTime startTime,
      LocalDateTime endTime,
      LocalDateTime startTime2,
      LocalDateTime endTime2);


}