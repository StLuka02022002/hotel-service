package com.example.hotel_service.service.impl;

import com.example.hotel_service.repository.EventRepository;
import com.example.hotel_service.service.StatisticsService;
import com.example.hotel_service.statistics.EventType;
import com.example.hotel_service.statistics.ReportType;
import com.example.hotel_service.statistics.event.EventDocument;
import com.example.hotel_service.statistics.event.RoomBookedEvent;
import com.example.hotel_service.statistics.event.UserRegisteredEvent;
import com.example.hotel_service.statistics.report.csv.RoomBookedCsvReport;
import com.example.hotel_service.statistics.report.csv.UserRegisterCsvReport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticsService {

    private final EventRepository repository;
    private final UserRegisterCsvReport userRegisterCsvReport;
    private final RoomBookedCsvReport roomBookedCsvReport;

    @Override
    public EventDocument saveUserRegisteredEvent(UserRegisteredEvent event) {
        return this.saveEvent(event, EventType.USER_REGISTERED.name());
    }

    @Override
    public EventDocument saveRoomBookedEvent(RoomBookedEvent event) {
        return this.saveEvent(event, EventType.ROOM_BOOKED.name());
    }

    private EventDocument saveEvent(Object event, String eventType) {
        EventDocument document = EventDocument.builder()
                .eventType(eventType)
                .event(event)
                .build();
        return repository.save(document);
    }

    @Override
    public List<EventDocument> getAllEvents() {
        return repository.findAll();
    }

    @Override
    public String generateCsvReport() {
        List<EventDocument> events = getAllEvents();
        StringBuilder csv = new StringBuilder();
        csv.append("Id, Event Type,Timestamp\n");

        for (EventDocument event : events) {
            EventType eventType = EventType.valueOf(event.getEventType());
            csv.append(event.getId()).append(",").append(eventType).append(",")
                    .append(event.getTimestamp()).append("\n");
            switch (eventType) {
                case ROOM_BOOKED ->
                        csv.append(roomBookedCsvReport.report((RoomBookedEvent) event.getEvent())).append("\n");
                case USER_REGISTERED ->
                        csv.append(userRegisterCsvReport.report((UserRegisteredEvent) event.getEvent())).append("\n");
                default -> csv.append("Not found Type").append("\n");
            }
        }
        return csv.toString();
    }

    @Override
    public boolean saveStatistics(String filePath, ReportType reportType) {
        File file = this.getFile(filePath);
        if (file == null) {
            return false;
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
             OutputStreamWriter writer = new OutputStreamWriter(bufferedOutputStream, StandardCharsets.UTF_8)) {
            String data = switch (reportType) {
                case CSV -> this.generateCsvReport();
            };

            writer.write(data);
            writer.flush();
        } catch (IOException e) {
            //TODO Написать в log или выкинуть ошибку
            return false;
        }
        return true;
    }

    private File getFile(String filePath) {
        File file = new File(filePath);

        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                return null;
            }
        }

        try {
            if (!file.exists() && !file.createNewFile()) {
                return null;
            }
        } catch (IOException e) {
            //TODO Написать в log или выкинуть ошибку
            return null;
        }
        return file;
    }
}
