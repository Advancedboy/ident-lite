package com.identlite.api.controller;

import com.identlite.api.dto.BookingDto;
import com.identlite.api.dto.CreateBookingDto;
import com.identlite.api.dto.UpdateBookingDto;
import com.identlite.api.dto.mapping.BookingMapper;
import com.identlite.api.model.Booking;
import com.identlite.api.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Бронирование", description = "Эндпоинты для работы с бронированиями")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper bookingMapper;


    @Operation(summary = "Получить все бронирования",
            description = "Возвращает список всех бронирований")
    @GetMapping
    public ResponseEntity<List<BookingDto>> getAllBookings() {
        List<BookingDto> bookingDtos = bookingService.getAllBookings().stream()
                .map(bookingMapper::toDto)
                .toList();
        return ResponseEntity.ok(bookingDtos);
    }

    @Operation(summary = "Получить бронирование по id",
            description = "Возвращает бронирование с указанным id, иначе 404")
    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(bookingMapper.toDto(booking));
    }

    @Operation(summary = "Создать новое бронирование",
            description = "Создает новое бронирование")
    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestBody @Valid
                                                        CreateBookingDto createBookingDto) {
        Booking booking = bookingService.createBooking(createBookingDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingMapper.toDto(booking));
    }

    @Operation(summary = "Обновить информацию о бронировании с перезаписью старых значений",
            description = "Обновляет данные о бронировании с перезаписью старых значений")
    @PutMapping("/{id}")
    public ResponseEntity<BookingDto> updateBooking(@PathVariable Long id,
                                                    @RequestBody
                                                    UpdateBookingDto updateBookingDto) {
        Booking updated = bookingService.updateBooking(id, updateBookingDto);
        return ResponseEntity.ok(bookingMapper.toDto(updated));
    }

    @Operation(summary = "Удалить бронирование",
            description = "Удаляет бронирование с текущим id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}
