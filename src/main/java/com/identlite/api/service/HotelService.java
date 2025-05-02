package com.identlite.api.service;

import com.identlite.api.model.Hotel;
import com.identlite.api.repository.HotelRepository;
import java.util.List;
import java.util.Optional;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class HotelService {
    private final HotelRepository hotelRepository;

    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public Hotel findById(long id) {
        return hotelRepository.findById(id)
                .orElse(null);
    }


    public void createHotel(Hotel hotel) {
        hotelRepository.save(hotel);
    }

    public Hotel updateHotel(Long id, Hotel updatedHotel) {
        Optional<Hotel> hotel = hotelRepository.findById(id);

        if (hotel.isEmpty()) {
            return null;
        }

        Hotel hotelToUpdate = hotel.get();

        hotelToUpdate.setName(updatedHotel.getName());
        hotelToUpdate.setAddress(updatedHotel.getAddress());
        hotelToUpdate.setCity(updatedHotel.getCity());
        hotelToUpdate.setDescription(updatedHotel.getDescription());

        return hotelRepository.save(hotelToUpdate);
    }

    public boolean deleteHotelById(long id) {
        if (!hotelRepository.existsById(id)) {
            return false;
        }
        hotelRepository.deleteById(id);
        return true;
    }
}
