package com.identlite.api.service;

import com.identlite.api.model.Hotel;
import com.identlite.api.repository.HotelRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;

    @Transactional
    @Retryable(value = JpaOptimisticLockingFailureException.class,
            maxAttempts = 3, backoff = @Backoff(delay = 100))
    public Hotel createHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Transactional
    @Retryable(value = JpaOptimisticLockingFailureException.class,
            maxAttempts = 3, backoff = @Backoff(delay = 100))
    public Hotel updateHotel(Long id, Hotel hotel) {
        Hotel existingHotel = hotelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Hotel not found with id: " + id));
        existingHotel.setName(hotel.getName());
        existingHotel.setAddress(hotel.getAddress());
        existingHotel.setCity(hotel.getCity());
        existingHotel.setDescription(hotel.getDescription());
        return hotelRepository.save(existingHotel);
    }

    @Transactional
    public void deleteHotel(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new NoSuchElementException("Hotel not found with id: " + id);
        }
        hotelRepository.deleteById(id);
    }

    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Hotel not found with id: " + id));
    }

    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }
}