package com.example.hotel_service.statistics.report.csv;

import com.example.hotel_service.statistics.event.RoomBookedEvent;
import com.example.hotel_service.statistics.report.Report;
import org.springframework.stereotype.Component;

@Component
public class RoomBookedCsvReport implements Report<RoomBookedEvent> {
    @Override
    public String report(RoomBookedEvent roomBookedEvent) {
        return roomBookedEvent.getEventId() + "," +
                roomBookedEvent.getId() + "," +
                roomBookedEvent.getUserId() + "," +
                roomBookedEvent.getCheckInDate() + "," +
                roomBookedEvent.getCheckOutDate() + "," +
                roomBookedEvent.getTimestamp();
    }
}
