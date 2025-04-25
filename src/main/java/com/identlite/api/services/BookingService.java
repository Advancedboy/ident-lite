package com.identlite.api.services;


import com.identlite.api.models.Booking;
import java.util.List;
import java.util.Optional;


public interface BookingService {
    Booking createBooking(Booking booking);

    List<Booking> getBookingsByUserAndStatus(String username, String status);

    List<Booking> getBookingsByUserAndStatusNative(String username, String status);

    Booking updateBooking(Long id, Booking booking);

    boolean deleteBooking(Long id);

    Optional<Booking> getBookingById(Long id);

    List<Booking> getAllBookings();

    List<Booking> getBookingsByUserId(Long userId);

    List<Booking> getBookingsByRoomId(Long roomId);
}
