package com.identlite.api.services;

import com.identlite.api.dto.BookingDto;
import com.identlite.api.dto.UserDto;
import com.identlite.api.models.Booking;
import com.identlite.api.models.Room;
import com.identlite.api.models.User;
import com.identlite.api.repositories.BookingRepository;
import com.identlite.api.repositories.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository,
                           BookingRepository bookingRepository,
                           ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<BookingDto> getAllBookingsByUserId(Long userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);
        return bookings.stream()
                .map(booking -> modelMapper.map(booking, BookingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public BookingDto updateBooking(Long userId, Long bookingId, BookingDto updatedBookingDto) {
        if (!userRepository.existsById(userId)) {
            return null;
        }

        Optional<Booking> existingBookingOptional = bookingRepository.findById(bookingId);
        if (existingBookingOptional.isPresent()) {
            Booking existingBooking = existingBookingOptional.get();
            // Обновляем поля бронирования
            existingBooking.setStartDate(updatedBookingDto.getCheckInDate());
            existingBooking.setEndDate(updatedBookingDto.getCheckOutDate());
            existingBooking.setRoom(modelMapper.map(updatedBookingDto.getRoom(), Room.class));

            // Сохраняем обновленное бронирование
            Booking updatedBooking = bookingRepository.save(existingBooking);
            return modelMapper.map(updatedBooking, BookingDto.class);
        } else {
            return null;
        }
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        // Преобразуем UserDto в User
        User user = modelMapper.map(userDto, User.class);
        user = userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        return userRepository.findById(id)
                .map(existing -> {
                    existing.setUsername(userDto.getUsername());
                    existing.setEmail(userDto.getEmail());
                    existing.setPassword(userDto.getPassword());
                    User updatedUser = userRepository.save(existing);
                    return modelMapper.map(updatedUser, UserDto.class);
                }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> modelMapper.map(user, UserDto.class));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> modelMapper.map(user, UserDto.class));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookingDto> getBookingByUserIdAndBookingId(Long userId, Long bookingId) {
        Optional<Booking> booking = bookingRepository.findByIdAndUserId(bookingId, userId);
        return booking.map(b -> modelMapper.map(b, BookingDto.class));
    }

    @Override
    public boolean deleteBooking(Long userId, Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findByIdAndUserId(bookingId, userId);
        if (bookingOpt.isPresent()) {
            bookingRepository.delete(bookingOpt.get());
            return true;
        }
        return false;
    }
}
