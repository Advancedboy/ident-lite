package com.identlite.api.service;

import com.identlite.api.dto.BookingDto;
import com.identlite.api.dto.mapping.BookingMapper;
import com.identlite.api.exceptions.BookingNotFoundException;
import com.identlite.api.model.Booking;
import com.identlite.api.repository.BookingRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Transactional
    public BookingDto getBookingById(Long id) {
        return bookingRepository.findById(id)
                .map(bookingMapper::toDto)
                .orElseThrow(() -> new BookingNotFoundException(id));
    }

    public List<BookingDto> findAll() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    public Booking findById(Long id) {
        return bookingRepository.findById(id)
                .orElse(null);
    }

    public void createBooking(Booking booking) {
        bookingRepository.save(booking);
    }

    public Booking updateBooking(Long id, Booking updatedBooking) {
        Optional<Booking> booking = bookingRepository.findById(id);

        if (booking.isEmpty()) {
            return null;
        }

        Booking bookingToUpdate = booking.get();

        bookingToUpdate.setUser(updatedBooking.getUser());
        bookingToUpdate.setHotel(updatedBooking.getHotel());
        bookingToUpdate.setStartDate(updatedBooking.getStartDate());
        bookingToUpdate.setEndDate(updatedBooking.getEndDate());

        return bookingRepository.save(bookingToUpdate);
    }

    public boolean deleteBookingById(long id) {
        if (!bookingRepository.existsById(id)) {
            return false;
        }
        bookingRepository.deleteById(id);
        return true;
    }
}
