package com.identlite.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
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

    private static final String LOG_DIR = "logs";

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
            // Преобразуем строку в дату, чтобы проверить корректность формата
            LocalDate.parse(date);

            // Формируем имя файла лога на основе даты
            String fileName = "app-" + date + ".log";
            Path logPath = Paths.get(LOG_DIR, fileName);

            // Проверка существования файла
            File logFile = logPath.toFile();
            if (!logFile.exists()) {
                return ResponseEntity.notFound().build();  // Возвращаем 404, если файл не найден
            }

            // Создаем ресурс для лог-файла
            Resource fileResource = new FileSystemResource(logFile);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + fileName)
                    .body(fileResource);
        } catch (Exception e) {
            // Возвращаем ошибку, если дата была в неверном формате
            return ResponseEntity.badRequest().body(null);
        }
    }
}
