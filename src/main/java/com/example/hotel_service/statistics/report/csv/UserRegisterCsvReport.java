package com.example.hotel_service.statistics.report.csv;

import com.example.hotel_service.statistics.event.UserRegisteredEvent;
import com.example.hotel_service.statistics.report.Report;
import org.springframework.stereotype.Component;

@Component
public class UserRegisterCsvReport implements Report<UserRegisteredEvent> {
    @Override
    public String report(UserRegisteredEvent userRegisteredEvent) {
        return userRegisteredEvent.getEventId() + "," +
                userRegisteredEvent.getId() + "," +
                userRegisteredEvent.getUsername() + "," +
                userRegisteredEvent.getEmail() + "," +
                userRegisteredEvent.getTimestamp();
    }
}
