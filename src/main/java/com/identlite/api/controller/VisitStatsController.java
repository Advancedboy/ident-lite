package com.identlite.api.controller;

import com.identlite.api.service.VisitCounterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/visits")
@Tag(name = "Администрирование проектов", description = "Создание, редактирование и удаление печей")
public class VisitStatsController {
    private final VisitCounterService visitCounterService;

    public VisitStatsController(VisitCounterService visitCounterService) {
        this.visitCounterService = visitCounterService;
    }

    @GetMapping
    public Map<String, Integer> getAllVisitStats() {
        return visitCounterService.getAllCounts();
    }

    @GetMapping("/{uri}")
    public int getVisitCount(@PathVariable String uri) {
        String fullPath = String.join("/","", uri);
        return visitCounterService.getCount(fullPath);
    }

    @DeleteMapping
    public void resetAll() {
        visitCounterService.reset();
    }
}
