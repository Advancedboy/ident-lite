package com.identlite.api.controller;

import com.identlite.api.dto.BookingDto;
import com.identlite.api.dto.UserDto;
import com.identlite.api.dto.mapping.BookingMapper;
import com.identlite.api.dto.mapping.UserMapper;
import com.identlite.api.model.User;
import com.identlite.api.repository.BookingRepository;
import com.identlite.api.repository.UserRepository;
import com.identlite.api.service.UserService;
import java.util.List;
import java.util.Optional;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Data
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final UserMapper userMapper;
    private final BookingMapper bookingMapper;

    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<BookingDto>> getUserBookings(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserBookings(id));
    }

    @GetMapping("/{id}/with-bookings")
    public ResponseEntity<UserDto> getUserWithBookings(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserWithBookings(id));
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable long id) {
        Optional<UserDto> userDto = userService.findById(id);
        return userDto
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public void createUser(@RequestBody User user) {
        userService.createUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,
                                           @RequestBody User updatedUser) {
        User updated = userService.updateUser(id, updatedUser);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        if (!userService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
