package com.identlite.api.service;

import com.identlite.api.dto.BookingDto;
import com.identlite.api.dto.CreateUserDto;
import com.identlite.api.dto.UserDto;
import com.identlite.api.model.Booking;
import com.identlite.api.model.Hotel;
import com.identlite.api.model.User;
import com.identlite.api.repository.BookingRepository;
import com.identlite.api.repository.HotelRepository;
import com.identlite.api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
    }

    public User createUser(CreateUserDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public void addBookingToUser(Long userId, BookingDto bookingDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Hotel hotel = hotelRepository.findById(bookingDto.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        Booking booking = new Booking();
        booking.setStartDate(bookingDto.getStartDate());
        booking.setEndDate(bookingDto.getEndDate());
        booking.setUser(user);
        booking.setHotel(hotel);

        bookingRepository.save(booking);

        user.getBookings().add(booking);

        userRepository.save(user);
    }

    public User updateUser(Long id, User user) {
        User existing = getUserById(id);
        user.setId(existing.getId());
        return userRepository.save(user);
    }

    public User patchUser(Long id, UserDto dto) {
        User existing = getUserById(id);

        if (dto.getName() != null) {
            existing.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            existing.setEmail(dto.getEmail());
        }

        return userRepository.save(existing);
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}