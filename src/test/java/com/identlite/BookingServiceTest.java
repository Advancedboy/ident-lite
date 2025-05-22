package com.identlite;

import com.identlite.api.dto.CreateBookingDto;
import com.identlite.api.dto.HotelDto;
import com.identlite.api.dto.UpdateBookingDto;
import com.identlite.api.dto.UserDto;
import com.identlite.api.model.Booking;
import com.identlite.api.model.Hotel;
import com.identlite.api.model.User;
import com.identlite.api.repository.BookingRepository;
import com.identlite.api.repository.HotelRepository;
import com.identlite.api.repository.UserRepository;
import com.identlite.api.service.BookingService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingService bookingService;

    private Booking booking;
    private CreateBookingDto createBookingDto;
    private UpdateBookingDto updateBookingDto;
    private User user;
    private Hotel hotel;
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        user = new User();
        user.setId(1L);
        user.setName("Test User");

        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Test Hotel");

        booking = new Booking();
        booking.setId(1L);
        booking.setStartDate(LocalDate.now().plusDays(1));
        booking.setEndDate(LocalDate.now().plusDays(4));
        booking.setUser(user);
        booking.setHotel(hotel);

        createBookingDto = new CreateBookingDto();
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        createBookingDto.setUser(userDto);

        HotelDto hotelDto = new HotelDto();
        hotelDto.setId(1L);
        createBookingDto.setHotel(hotelDto);

        createBookingDto.setStartDate(LocalDate.now().plusDays(1));
        createBookingDto.setEndDate(LocalDate.now().plusDays(4));

        updateBookingDto = new UpdateBookingDto();
        updateBookingDto.setStartDate(LocalDate.now().plusDays(2));
        updateBookingDto.setEndDate(LocalDate.now().plusDays(5));
    }

    @Test
    void getAllBookings_ShouldReturnAllBookings() {
        when(bookingRepository.findAll()).thenReturn(Collections.singletonList(booking));

        List<Booking> result = bookingService.getAllBookings();

        assertEquals(1, result.size());
        assertEquals(booking, result.get(0));
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void getBookingById_WithExistingId_ShouldReturnBooking() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        Booking result = bookingService.getBookingById(1L);

        assertEquals(booking, result);
        verify(bookingRepository, times(1)).findById(1L);
    }

    @Test
    void getBookingById_WithNonExistingId_ShouldThrowException() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.getBookingById(1L));
        verify(bookingRepository, times(1)).findById(1L);
    }

    @Test
    void createBooking_WithValidData_ShouldCreateBooking() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.createBooking(createBookingDto);

        assertNotNull(result);
        assertEquals(booking, result);
        verify(userRepository, times(1)).findById(1L);
        verify(hotelRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void createBooking_WithNullUser_ShouldThrowException() {
        createBookingDto.setUser(null);

        var violations = validator.validate(createBookingDto);
        assertEquals(1, violations.size());
        assertEquals("User must not be null", violations.iterator().next().getMessage());

        assertThrows(RuntimeException.class, () -> bookingService.createBooking(createBookingDto));
    }

    @Test
    void createBooking_WithNullHotel_ShouldThrowException() {
        createBookingDto.setHotel(null);

        var violations = validator.validate(createBookingDto);
        assertEquals(1, violations.size());
        assertEquals("Hotel must not be null", violations.iterator().next().getMessage());

        assertThrows(RuntimeException.class, () -> bookingService.createBooking(createBookingDto));
    }

    @Test
    void createBooking_WithPastStartDate_ShouldThrowException() {
        createBookingDto.setStartDate(LocalDate.now().minusDays(1));

        var violations = validator.validate(createBookingDto);
        assertEquals(1, violations.size());
        assertEquals("Start date must be in the future", violations.iterator().next().getMessage());

        assertThrows(RuntimeException.class, () -> bookingService.createBooking(createBookingDto));
    }

    @Test
    void createBooking_WithPastEndDate_ShouldThrowException() {
        createBookingDto.setEndDate(LocalDate.now().minusDays(1));

        var violations = validator.validate(createBookingDto);
        assertEquals(1, violations.size());
        assertEquals("End date must be in the future", violations.iterator().next().getMessage());

        assertThrows(RuntimeException.class, () -> bookingService.createBooking(createBookingDto));
    }

    @Test
    void createBooking_WithEndDateBeforeStartDate_ShouldThrowException() {
        // Arrange
        createBookingDto.setStartDate(LocalDate.now().plusDays(3));
        createBookingDto.setEndDate(LocalDate.now().plusDays(2));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.createBooking(createBookingDto)
        );

        assertEquals("End date must be after start date", exception.getMessage());

        // Проверяем, что не было обращений к репозиториям
        verify(userRepository, never()).findById(anyLong());
        verify(hotelRepository, never()).findById(anyLong());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void createBooking_WithEqualStartAndEndDates_ShouldThrowException() {
        // Arrange
        LocalDate sameDate = LocalDate.now().plusDays(3);
        createBookingDto.setStartDate(sameDate);
        createBookingDto.setEndDate(sameDate);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.createBooking(createBookingDto)
        );

        assertEquals("End date must be after start date", exception.getMessage());
    }

    @Test
    void createBooking_WithNonExistingUser_ShouldThrowException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookingService.createBooking(createBookingDto));
        verify(userRepository, times(1)).findById(1L);
        verify(hotelRepository, never()).findById(anyLong());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void createBooking_WithNonExistingHotel_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(hotelRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookingService.createBooking(createBookingDto));
        verify(userRepository, times(1)).findById(1L);
        verify(hotelRepository, times(1)).findById(1L);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void updateBooking_WithValidData_ShouldUpdateBooking() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.updateBooking(1L, updateBookingDto);

        assertNotNull(result);
        assertEquals(updateBookingDto.getStartDate(), result.getStartDate());
        verify(bookingRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void updateBooking_WithNonExistingBooking_ShouldThrowException() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.updateBooking(1L, updateBookingDto));
        verify(bookingRepository, times(1)).findById(1L);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void deleteBooking_WithExistingId_ShouldDeleteBooking() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        doNothing().when(bookingRepository).delete(booking);

        bookingService.deleteBooking(1L);

        verify(bookingRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).delete(booking);
    }

    @Test
    void deleteBooking_WithNonExistingId_ShouldThrowException() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.deleteBooking(1L));
        verify(bookingRepository, times(1)).findById(1L);
        verify(bookingRepository, never()).delete(any(Booking.class));
    }
}