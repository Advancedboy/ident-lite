package com.identlite.api.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class VisitCounterService {
    private final Map<String, AtomicInteger> urlCounters = new ConcurrentHashMap<>();

    public void increment(String uri) {
        urlCounters.computeIfAbsent(uri, s -> new AtomicInteger(0)).incrementAndGet();
    }

    public int getCount(String uri) {
        return urlCounters.getOrDefault(uri, new AtomicInteger(0)).get();
    }

    public Map<String, Integer> getAllCounts() {
        return urlCounters.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    }

    public void reset() {
        urlCounters.clear();
    }
}
