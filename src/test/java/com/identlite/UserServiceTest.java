package com.identlite;

import com.identlite.api.dto.BookingDto;
import com.identlite.api.dto.CreateUserDto;
import com.identlite.api.dto.HotelDto;
import com.identlite.api.dto.UserDto;
import com.identlite.api.dto.mapping.UserMapper;
import com.identlite.api.exceptions.EmailAlreadyExistsException;
import com.identlite.api.model.Booking;
import com.identlite.api.model.Hotel;
import com.identlite.api.model.User;
import com.identlite.api.repository.BookingRepository;
import com.identlite.api.repository.HotelRepository;
import com.identlite.api.repository.UserRepository;
import com.identlite.api.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private CreateUserDto createUserDto;
    private UserDto userDto;
    private BookingDto bookingDto;
    private Hotel hotel;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        createUserDto = new CreateUserDto("Test User", "test@example.com", "password");
        userDto = new UserDto(1L, "Test User", "test@example.com");

        HotelDto hotelDto = new HotelDto();
        hotelDto.setId(1L);
        bookingDto = new BookingDto();
        bookingDto.setHotel(hotelDto);
        bookingDto.setStartDate(LocalDate.now());
        bookingDto.setEndDate(LocalDate.now().plusDays(3));

        hotel = new Hotel();
        hotel.setId(1L);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserById(1L);

        // Assert
        assertEquals(user, result);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_WhenUserNotExists_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void createUser_WithNewEmail_ShouldCreateUser() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = userService.createUser(createUserDto);

        // Assert
        assertEquals(user, result);
        verify(userRepository, times(1)).findByEmail(createUserDto.getEmail());
        verify(passwordEncoder, times(1)).encode(createUserDto.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_WithExistingEmail_ShouldThrowException() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(createUserDto));
        verify(userRepository, times(1)).findByEmail(createUserDto.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUsersBulk_ShouldCreateMultipleUsers() {
        // Arrange
        List<CreateUserDto> dtos = Arrays.asList(createUserDto, createUserDto);
        List<User> users = Arrays.asList(user, user);

        when(userMapper.toEntity(dtos)).thenReturn(users);
        when(userRepository.saveAll(users)).thenReturn(users);

        // Act
        List<User> result = userService.createUsersBulk(dtos);

        // Assert
        assertEquals(2, result.size());
        verify(userMapper, times(1)).toEntity(dtos);
        verify(userRepository, times(1)).saveAll(users);
    }

    @Test
    void addBookingToUser_WithValidData_ShouldAddBooking() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        userService.addBookingToUser(1L, bookingDto);

        // Assert
        verify(userRepository, times(1)).findById(1L);
        verify(hotelRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verify(userRepository, times(1)).save(user);
        assertEquals(1, user.getBookings().size());
    }

    @Test
    void addBookingToUser_WithInvalidUserId_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.addBookingToUser(1L, bookingDto));
        verify(userRepository, times(1)).findById(1L);
        verify(hotelRepository, never()).findById(anyLong());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void addBookingToUser_WithInvalidHotelId_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(hotelRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.addBookingToUser(1L, bookingDto));
        verify(userRepository, times(1)).findById(1L);
        verify(hotelRepository, times(1)).findById(1L);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void updateUser_ShouldUpdateExistingUser() {
        // Arrange
        User updatedUser = new User();
        updatedUser.setName("Updated Name");
        updatedUser.setEmail("updated@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // Act
        User result = userService.updateUser(1L, updatedUser);

        // Assert
        assertEquals(updatedUser, result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    void patchUser_WithName_ShouldUpdateOnlyName() {
        // Arrange
        UserDto patchDto = new UserDto();
        patchDto.setName("Patched Name");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = userService.patchUser(1L, patchDto);

        // Assert
        assertEquals("Patched Name", result.getName());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void patchUser_WithEmail_ShouldUpdateOnlyEmail() {
        // Arrange
        UserDto patchDto = new UserDto();
        patchDto.setEmail("patched@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = userService.patchUser(1L, patchDto);

        // Assert
        assertEquals("Test User", result.getName());
        assertEquals("patched@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void patchUser_WithNoChanges_ShouldNotUpdate() {
        // Arrange
        UserDto patchDto = new UserDto();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = userService.patchUser(1L, patchDto);

        // Assert
        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void deleteUser_ShouldDeleteUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteUser_WhenUserNotExists_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(1L));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).delete(any(User.class));
    }
}