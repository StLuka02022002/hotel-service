package com.example.hotel_service.service;

import com.example.hotel_service.statistics.ReportType;
import com.example.hotel_service.statistics.event.EventDocument;
import com.example.hotel_service.statistics.event.RoomBookedEvent;
import com.example.hotel_service.statistics.event.UserRegisteredEvent;

import java.io.File;
import java.util.List;

public interface StatisticsService {

    File exportStatsToCsv(Boolean update);

    EventDocument saveUserRegisteredEvent(UserRegisteredEvent event);

    EventDocument saveRoomBookedEvent(RoomBookedEvent event);

    List<EventDocument> getAllEvents();

    String generateCsvReport();

    boolean saveStatistics(String filePath, ReportType reportType);
}
