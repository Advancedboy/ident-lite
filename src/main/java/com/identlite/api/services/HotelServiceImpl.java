package com.identlite.api.services;

import com.identlite.api.dto.HotelDto;
import com.identlite.api.dto.HotelRequestDto;
import com.identlite.api.models.Hotel;
import com.identlite.api.repositories.HotelRepository;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;

    public HotelServiceImpl(HotelRepository hotelRepository, ModelMapper modelMapper) {
        this.hotelRepository = hotelRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public HotelDto getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id).orElse(null);
        return hotel != null ? modelMapper.map(hotel, HotelDto.class) : null;
    }

    @Override
    public HotelDto createHotel(HotelRequestDto hotelDto) {
        Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
        Hotel savedHotel = hotelRepository.save(hotel);
        return modelMapper.map(savedHotel, HotelDto.class);
    }

    @Override
    public HotelDto updateHotel(Long id, HotelRequestDto hotelDto) {
        Hotel updated = hotelRepository.findById(id)
                .map(existing -> {
                    existing.setName(hotelDto.getName());
                    existing.setAddress(hotelDto.getAddress());
                    existing.setDescription(hotelDto.getDescription());
                    // можно также обновить amenities и rooms, если нужно
                    return hotelRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        return modelMapper.map(updated, HotelDto.class);
    }

    @Override
    public boolean deleteHotel(Long id) {
        if (hotelRepository.existsById(id)) {
            hotelRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<HotelDto> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(hotel -> modelMapper.map(hotel, HotelDto.class))
                .toList();
    }

    @Override
    public Optional<HotelDto> getHotelByName(String name) {
        return hotelRepository.findByName(name)
                .map(hotel -> modelMapper.map(hotel, HotelDto.class));
    }
}
