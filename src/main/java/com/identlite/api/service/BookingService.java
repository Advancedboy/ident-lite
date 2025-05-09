package com.identlite.api.service;

import com.identlite.api.dto.BookingDto;
import com.identlite.api.dto.CreateBookingDto;
import com.identlite.api.dto.UpdateBookingDto;
import com.identlite.api.dto.mapping.BookingMapper;
import com.identlite.api.model.Booking;
import com.identlite.api.model.Hotel;
import com.identlite.api.model.User;
import com.identlite.api.repository.BookingRepository;
import com.identlite.api.repository.HotelRepository;
import com.identlite.api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id " + id));
    }

    public Booking createBooking(CreateBookingDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        Booking booking = new Booking();
        booking.setStartDate(dto.getStartDate());
        booking.setEndDate(dto.getEndDate());
        booking.setUser(user);
        booking.setHotel(hotel);

        return bookingRepository.save(booking);
    }


    public Booking updateBooking(Long id, UpdateBookingDto updateBookingDto) {
        Booking booking = getBookingById(id);
        booking.setStartDate(updateBookingDto.getStartDate());
        booking.setEndDate(updateBookingDto.getEndDate());

        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        Booking booking = getBookingById(id);
        bookingRepository.delete(booking);
    }
}