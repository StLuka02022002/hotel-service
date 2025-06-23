package com.example.hotel_service.statistics.kafka;

import com.example.hotel_service.statistics.event.RoomBookedEvent;
import com.example.hotel_service.statistics.event.UserRegisteredEvent;
import com.example.hotel_service.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventConsumer {
    private final StatisticsService statsService;

    @Value("${kafka.topics.user-registered-topic}")
    private String userRegisteredTopic;
    @Value("${kafka.topics.room-booked-topic}")
    private String roomBookedTopic;

    @KafkaListener(topics = "${kafka.topics.user-registered-topic}", groupId = "${kafka.groups.statistics-group}")
    public void consumeUserRegisteredEvent(UserRegisteredEvent event) {
        statsService.saveUserRegisteredEvent(event);
    }

    @KafkaListener(topics = "${kafka.topics.room-booked-topic}", groupId = "${kafka.groups.statistics-group}")
    public void consumeRoomBookedEvent(RoomBookedEvent event) {
        statsService.saveRoomBookedEvent(event);
    }
}
