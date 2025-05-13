package com.identlite.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
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
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Operation(summary = "Получить лог-файл за указанную дату",
            description = "Возвращает лог-файл за указанную дату в формате 'yyyy-MM-dd'.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Лог-файл найден и возвращен"),
                   @ApiResponse(responseCode = "400", description = "Неверный формат даты"),
                   @ApiResponse(responseCode = "404",
                           description = "Лог-файл не найден для указанной даты")
    })
    @GetMapping("/logs")
    public ResponseEntity<Resource> getLogFile(
            @Parameter(description = "Дата в формате yyyy-MM-dd", required = true)
            @RequestParam String date) throws IOException {

        try {
            LocalDate parsedDate = LocalDate.parse(date, DATE_FORMATTER);

            // Construct a safe file name based on a validated date
            String fileName = "app-" + parsedDate.format(DATE_FORMATTER) + ".log";
            Path logPath = LOG_DIR.resolve(fileName).normalize();

            // Ensure the file is within the allowed directory
            if (!logPath.startsWith(LOG_DIR) || !Files.exists(logPath)) {
                return ResponseEntity.notFound().build();
            }

            Resource fileResource = new FileSystemResource(logPath.toFile());

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + fileName)
                    .body(fileResource);

        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
