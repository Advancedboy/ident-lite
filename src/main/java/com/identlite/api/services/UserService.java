package com.identlite.api.services;

import com.identlite.api.dto.BookingDto;
import com.identlite.api.dto.UserDto;
import java.util.List;
import java.util.Optional;


public interface UserService {

    // Создание пользователя
    UserDto createUser(UserDto userDto);

    // Обновление пользователя
    UserDto updateUser(Long id, UserDto userDto);

    // Удаление пользователя
    void deleteUser(Long id);

    // Получение пользователя по email
    Optional<UserDto> getUserByEmail(String email);

    // Получение всех бронирований пользователя по его ID
    List<BookingDto> getAllBookingsByUserId(Long userId);

    // Обновление бронирования
    BookingDto updateBooking(Long userId, Long bookingId, BookingDto updatedBookingDto);

    // Получение пользователя по ID
    Optional<UserDto> getUserById(Long id);

    // Получение всех пользователей
    List<UserDto> getAllUsers();

    // Получение конкретного бронирования по ID пользователя и бронирования
    Optional<BookingDto> getBookingByUserIdAndBookingId(Long userId, Long bookingId);

    // Удаление бронирования
    boolean deleteBooking(Long userId, Long bookingId);
}
