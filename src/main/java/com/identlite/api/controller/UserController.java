package com.identlite.api.controller;

import com.identlite.api.cache.BookingCache;
import com.identlite.api.dto.BookingDto;
import com.identlite.api.dto.CreateUserDto;
import com.identlite.api.dto.UserDto;
import com.identlite.api.dto.mapping.UserMapper;
import com.identlite.api.model.User;
import com.identlite.api.repository.UserRepository;
import com.identlite.api.service.UserService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final BookingCache cacheService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> dtos = userService.getAllUsers().stream()
                .map(userMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/with-bookings")
    public List<User> getUsersWithBookings(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(required = false) Long hotelId) {

        String cacheKey = buildCacheKey(startDate, endDate, hotelId);

        if (cacheService.contains(cacheKey)) {
            return cacheService.getFromCache(cacheKey);
        }

        try {
            logger.info("Обращение к БД... 3 секунды");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }

        List<User> users = userRepository.findUserWithBookingsInPeriod(startDate, endDate, hotelId);
        cacheService.putInCache(cacheKey, users);
        logger.info("Результат запроса сохранен в кэш по ключу: {}", cacheKey);

        return users;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid CreateUserDto createUserDto) {
        User user = userService.createUser(createUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(user));
    }

    @PostMapping("/{userId}/addBooking")
    public ResponseEntity<Void> addBooking(@PathVariable Long userId,
                                           @RequestBody BookingDto bookingDto) {
        userService.addBookingToUser(userId, bookingDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User updated = userService.updateUser(id, user);
        return ResponseEntity.ok(userMapper.toDto(updated));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> patchUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        User patched = userService.patchUser(id, userDto);
        return ResponseEntity.ok(userMapper.toDto(patched));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cache/clear")
    public void clearCache() {
        cacheService.cleanCache();
    }

    private String buildCacheKey(LocalDate startDate, LocalDate endDate, Long hotelId) {
        return startDate + "_" + endDate + "_" + (hotelId != null ? hotelId : "any");
    }
}
