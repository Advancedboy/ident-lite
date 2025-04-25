package com.identlite.api.controllers;

import com.identlite.api.dto.BookingDto;
import com.identlite.api.dto.UserDto;
import com.identlite.api.services.UserService;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> {
                    UserDto userDto = modelMapper.map(user, UserDto.class);
                    return ResponseEntity.ok(userDto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Получение пользователя по email
    @GetMapping("/search")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam("email") String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)  // Если пользователь найден, возвращаем его DTO
                .orElse(ResponseEntity.notFound().build());  // Если не найден, возвращаем 404
    }

    // Получение всех пользователей
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Получение всех бронирований пользователя
    @GetMapping("/{userId}/bookings")
    public ResponseEntity<List<BookingDto>> getAllBookingsByUserId(@PathVariable Long userId) {
        List<BookingDto> bookings = userService.getAllBookingsByUserId(userId);
        if (bookings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(bookings);  // Возвращаем список всех бронирований
    }

    @GetMapping("/{userId}/bookings/{bookingId}")
    public ResponseEntity<BookingDto> getBookingByUserIdAndBookingId(@PathVariable Long userId,
                                                                     @PathVariable Long bookingId) {
        return userService.getBookingByUserIdAndBookingId(userId, bookingId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    // Обновление бронирования пользователя
    @PutMapping("/{userId}/bookings/{bookingId}")
    public ResponseEntity<BookingDto> updateBooking(@PathVariable Long userId,
                                                    @PathVariable Long bookingId,
                                                    @RequestBody BookingDto updatedBookingDto) {
        BookingDto updatedBooking = userService.updateBooking(userId, bookingId, updatedBookingDto);
        return ResponseEntity.ok(updatedBooking);  // Возвращаем обновленное бронирование
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);  // Удаляем пользователя
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}/bookings/{bookingId}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long userId,
                                              @PathVariable Long bookingId) {
        boolean deleted = userService.deleteBooking(userId, bookingId);
        if (!deleted) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Booking not found with id " + bookingId);
        }
        return ResponseEntity.noContent().build();  // Возвращаем успешный статус 204 (No Content)
    }
}
