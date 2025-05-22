package com.identlite;

import com.identlite.api.model.Hotel;
import com.identlite.api.repository.HotelRepository;
import com.identlite.api.service.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelService hotelService;

    private Hotel hotel;
    private Hotel updatedHotel;

    @BeforeEach
    void setUp() {
        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Grand Hotel");
        hotel.setAddress("123 Main St");
        hotel.setCity("New York");
        hotel.setDescription("Luxury hotel in downtown");

        updatedHotel = new Hotel();
        updatedHotel.setId(1L);
        updatedHotel.setName("Updated Grand Hotel");
        updatedHotel.setAddress("456 Broadway");
        updatedHotel.setCity("Manhattan");
        updatedHotel.setDescription("Renovated luxury hotel");
    }

    @Test
    void createHotel_ShouldSuccessfullyCreateHotel() {
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);

        Hotel result = hotelService.createHotel(hotel);

        assertNotNull(result);
        assertEquals(hotel.getId(), result.getId());
        verify(hotelRepository, times(1)).save(hotel);
    }

    @Test
    void updateHotel_WithExistingId_ShouldUpdateHotel() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelRepository.save(any(Hotel.class))).thenReturn(updatedHotel);

        Hotel result = hotelService.updateHotel(1L, updatedHotel);

        assertNotNull(result);
        assertEquals(updatedHotel.getName(), result.getName());
        verify(hotelRepository, times(1)).save(hotel);
    }

    // Остальные тесты остаются без изменений
    @Test
    void updateHotel_WithNonExistingId_ShouldThrowException() {
        when(hotelRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> hotelService.updateHotel(1L, updatedHotel));
        verify(hotelRepository, never()).save(any(Hotel.class));
    }

    @Test
    void deleteHotel_WithExistingId_ShouldDeleteHotel() {
        when(hotelRepository.existsById(1L)).thenReturn(true);
        doNothing().when(hotelRepository).deleteById(1L);

        hotelService.deleteHotel(1L);

        verify(hotelRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteHotel_WithNonExistingId_ShouldThrowException() {
        when(hotelRepository.existsById(1L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> hotelService.deleteHotel(1L));
        verify(hotelRepository, never()).deleteById(anyLong());
    }

    @Test
    void getHotelById_WithExistingId_ShouldReturnHotel() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        Hotel result = hotelService.getHotelById(1L);

        assertEquals(hotel, result);
    }

    @Test
    void getHotelById_WithNonExistingId_ShouldThrowException() {
        when(hotelRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> hotelService.getHotelById(1L));
    }

    @Test
    void getAllHotels_ShouldReturnAllHotels() {
        List<Hotel> hotels = Arrays.asList(hotel, new Hotel());
        when(hotelRepository.findAll()).thenReturn(hotels);

        List<Hotel> result = hotelService.getAllHotels();

        assertEquals(2, result.size());
    }
}