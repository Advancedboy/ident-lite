package com.identlite.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Панель администратора", description = "Служебные эндпоинты")
@RequestMapping("/api/admin")
public class AdminController {

    private static final Path LOG_DIR = Paths.get("logs").toAbsolutePath().normalize();
    private static final DateTimeFormatter DATE_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Operation(
            summary = "Получить лог-файл за указанную дату",
            description = "Возвращает лог-файл за дату в формате yyyy-MM-dd. "
                    + "Пример: ?date=2025-05-13"
    )
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Лог-файл найден и возвращен"),
                   @ApiResponse(responseCode = "400", description = "Неверный формат даты"),
                   @ApiResponse(responseCode = "404", description = "Файл не найден")
    })
    @GetMapping("/logs")
    public ResponseEntity<?> getLogsByDate(@RequestParam String date) {
        try {
            LocalDate requestedDate = LocalDate.parse(date, DATE_FORMATTER);
            LocalDate today = LocalDate.now();

            Path logFilePath;
            if (Objects.equals(requestedDate, today)) {
                logFilePath = LOG_DIR.resolve("app.log");
            } else {
                String fileName = String.format("app-%s.log", requestedDate);
                logFilePath = LOG_DIR.resolve(fileName);
            }

            if (!Files.exists(logFilePath)) {
                // return ResponseEntity.status(HttpStatus.NOT_FOUND)
                return ResponseEntity.badRequest()
                        .body("Лог-файл не найден за дату: " + date);
            }

            String logContent = Files.readString(logFilePath, StandardCharsets.UTF_8);
            return ResponseEntity.ok().body(logContent);

        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest()
                    .body("Неверный формат даты. Используй формат YYYY-MM-DD.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при чтении лог-файла.");
        }
    }
}
