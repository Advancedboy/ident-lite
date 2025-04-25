package com.identlite.api.repositories;


import com.identlite.api.models.Room;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelId(Long hotelId);

    List<Room> findByType(String type);
}

