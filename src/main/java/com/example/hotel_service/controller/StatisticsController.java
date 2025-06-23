package com.example.hotel_service.controller;

import com.example.hotel_service.service.StatisticsService;
import com.example.hotel_service.statistics.ReportType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Value("${file.path.csv}")
    private String filePath;

    @GetMapping("/export")
    public ResponseEntity<Resource> exportStatsToCsv(@RequestParam(required = false) Boolean update) {
        if (update != null && update) {
            statisticsService.saveStatistics(filePath, ReportType.CSV);
        }

        File file = new File(filePath);

        if (!file.exists() || !file.canRead()) {
            return ResponseEntity.notFound().build();
        }

        if (!file.isFile()) {
            return ResponseEntity.badRequest().build();
        }

        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(file.getName(), StandardCharsets.UTF_8)
                                .build()
                                .toString())
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .contentLength(file.length())
                .body(resource);
    }
}
