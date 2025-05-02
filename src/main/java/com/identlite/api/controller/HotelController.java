package com.identlite.api.controller;

import com.identlite.api.model.Hotel;
import com.identlite.api.service.HotelService;
import java.util.List;
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
@RequestMapping("api/hotels")
public class HotelController {
    private final HotelService hotelService;

    @GetMapping
    public List<Hotel> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> findById(@PathVariable long id) {
        Hotel hotel = hotelService.findById(id);
        if (hotel == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(hotel);
    }

    @PostMapping
    public void createHotel(@RequestBody Hotel hotel) {
        hotelService.createHotel(hotel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable Long id,
                                             @RequestBody Hotel updatedHotel) {
        Hotel updated = hotelService.updateHotel(id, updatedHotel);
        if (updated == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable long id) {
        boolean isDeleted = hotelService.deleteHotelById(id);
        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

}
