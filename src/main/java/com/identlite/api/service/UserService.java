package com.identlite.api.service;

import com.identlite.api.dto.BookingDto;
import com.identlite.api.dto.UserDto;
import com.identlite.api.dto.mapping.BookingMapper;
import com.identlite.api.dto.mapping.UserMapper;
import com.identlite.api.exceptions.UserNotFoundException;
import com.identlite.api.model.User;
import com.identlite.api.repository.BookingRepository;
import com.identlite.api.repository.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.stereotype.Service;


@Data
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final UserMapper userMapper;
    private final BookingMapper bookingMapper;

    public UserDto getUserWithBookings(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return userMapper.toDto(user);
    }

    public List<BookingDto> getUserBookings(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        return bookingRepository.findByUserId(userId).stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserDto> findById(long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto);
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(updatedUser.getName());
                    existingUser.setEmail(updatedUser.getEmail());
                    existingUser.setPassword(updatedUser.getPassword());
                    return userRepository.save(existingUser);
                })
                .orElse(null);
    }

    public boolean existsById(long id) {
        return userRepository.existsById(id);
    }

    public void deleteUserById(long id) {
        userRepository.deleteById(id);
    }
}
