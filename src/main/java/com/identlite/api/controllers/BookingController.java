package com.identlite.api.controllers;


import com.identlite.api.models.Booking;
import com.identlite.api.services.BookingService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Booking>> getBookingsByUserAndStatus(
            @RequestParam("username") String username,
            @RequestParam("status") String status) {
        List<Booking> bookings = bookingService.getBookingsByUserAndStatus(username, status);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/filter-native")
    public ResponseEntity<List<Booking>> getBookingsByUserAndStatusNative(
            @RequestParam("username") String username,
            @RequestParam("status") String status) {
        List<Booking> bookings = bookingService.getBookingsByUserAndStatusNative(username, status);
        return ResponseEntity.ok(bookings);
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        return ResponseEntity.ok(bookingService.createBooking(booking));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id,
                                                 @RequestBody Booking booking) {
        return ResponseEntity.ok(bookingService.updateBooking(id, booking));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        boolean deleted = bookingService.deleteBooking(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-user")
    public ResponseEntity<List<Booking>> getBookingsByUserId(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok(bookingService.getBookingsByUserId(userId));
    }

    @GetMapping("/by-room")
    public ResponseEntity<List<Booking>> getBookingsByRoomId(@RequestParam("roomId") Long roomId) {
        return ResponseEntity.ok(bookingService.getBookingsByRoomId(roomId));
    }
}
