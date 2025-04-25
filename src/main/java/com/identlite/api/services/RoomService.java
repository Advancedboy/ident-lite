package com.identlite.api.services;

import com.identlite.api.models.Room;
import java.util.List;
import java.util.Optional;


public interface RoomService {
    Room createRoom(Room room);

    Room updateRoom(Long id, Room room);

    void deleteRoom(Long id);

    Optional<Room> getRoomById(Long id);

    List<Room> getAllRooms();

    List<Room> getRoomsByHotelId(Long hotelId);

    List<Room> getRoomsByType(String type);
}

