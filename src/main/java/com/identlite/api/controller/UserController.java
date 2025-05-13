package com.identlite.api.controller;

import com.identlite.api.cache.BookingCache;
import com.identlite.api.dto.BookingDto;
import com.identlite.api.dto.CreateUserDto;
import com.identlite.api.dto.UserDto;
import com.identlite.api.dto.mapping.UserMapper;
import com.identlite.api.model.User;
import com.identlite.api.repository.UserRepository;
import com.identlite.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/users")
@Tag(name = "Пользователь", description = "Эндпоинты для работы с пользователем")
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final BookingCache cacheService;

    @Operation(summary = "Получить всех пользователей",
            description = "Возвращает список всех пользователей системы")
    @ApiResponse(responseCode = "200", description = "Успешный запрос")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> dtos = userService.getAllUsers().stream()
                .map(userMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary
            = "Выбрать пользователей, у которых даты записей находятся в указанном диапазоне",
            description
                    = "Возвращает массив пользователей с бронированиями,"
                    + "даты которых находятся в указанном временом диапазоне")
    @GetMapping("/with-bookings")
    public List<User> getUsersWithBookings(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(required = false) Long hotelId) {

        String cacheKey = buildCacheKey(startDate, endDate, hotelId);

        if (cacheService.contains(cacheKey)) {
            return cacheService.getFromCache(cacheKey);
        }

        try {
            logger.info("Обращение к БД... 3 секунды");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }

        List<User> users = userRepository.findUserWithBookingsInPeriod(startDate, endDate, hotelId);
        cacheService.putInCache(cacheKey, users);
        logger.info("Результат запроса сохранен в кэш по ключу: {}", cacheKey);

        return users;
    }

    @Operation(summary = "Получить пользователя по id",
            description = "Возвращает пользователя с указанным id, иначе 404")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @Operation(summary = "Создать нового пользователя",
            description = "Создает новый пользователя")
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid CreateUserDto createUserDto) {
        User user = userService.createUser(createUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(user));
    }

    @Operation(summary = "Добавить запись к пользователю с id",
            description = "создает новую запись для пользователя с указанным id")
    @PostMapping("/{userId}/addBooking")
    public ResponseEntity<Void> addBooking(@PathVariable Long userId,
                                           @RequestBody BookingDto bookingDto) {
        userService.addBookingToUser(userId, bookingDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Обновить информацию о пользователе с перезаписью старых значений",
            description = "Обновляет данные о пользователе с перезаписью старых значений")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id,
                                              @Valid @RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User updated = userService.updateUser(id, user);
        return ResponseEntity.ok(userMapper.toDto(updated));
    }

    @Operation(summary = "Обновить информацию о пользователе без перезаписи старых значений",
            description = "Обновляет данные о пользователе без перезаписи старых значений")
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> patchUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        User patched = userService.patchUser(id, userDto);
        return ResponseEntity.ok(userMapper.toDto(patched));
    }

    @Operation(summary = "Удалить пользователя",
            description = "Удаляет пользователя с текущим id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Очистить кэш",
            description = "Удаляет все записи из кэша")
    @DeleteMapping("/cache/clear")
    public void clearCache() {
        cacheService.cleanCache();
    }

    private String buildCacheKey(LocalDate startDate, LocalDate endDate, Long hotelId) {
        return startDate + "_" + endDate + "_" + (hotelId != null ? hotelId : "any");
    }
}
