package com.identlite.api.services;


import com.identlite.api.models.Booking;
import com.identlite.api.repositories.BookingRepository;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;

    public BookingServiceImpl(BookingRepository bookingRepository, ModelMapper modelMapper) {
        this.bookingRepository = bookingRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookingsByUserAndStatus(String username, String status) {
        return bookingRepository.findBookingsByUserAndStatus(username, status);
    }

    @Override
    public List<Booking> getBookingsByUserAndStatusNative(String username, String status) {
        return bookingRepository.findBookingsByUserAndStatusNative(username, status);
    }

    @Override
    public Booking updateBooking(Long id, Booking booking) {
        return bookingRepository.findById(id)
                .map(existing -> {
                    existing.setStartDate(booking.getStartDate());
                    existing.setEndDate(booking.getEndDate());
                    existing.setUser(booking.getUser());
                    existing.setRoom(booking.getRoom());
                    return bookingRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    public boolean deleteBooking(Long id) {
        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        if (bookingOpt.isPresent()) {
            bookingRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public List<Booking> getBookingsByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }
}
