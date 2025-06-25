package com.example.hotel_service.statistics.kafka;

import com.example.hotel_service.statistics.event.RoomBookedEvent;
import com.example.hotel_service.statistics.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventProducer {

    @Value("${kafka.topics.user-registered-topic}")
    private String userRegisteredTopic;
    @Value("${kafka.topics.room-booked-topic}")
    private String roomBookedTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserRegisteredEvent(UserRegisteredEvent event) {
        kafkaTemplate.send(userRegisteredTopic, event);
    }

    public void sendRoomBookedEvent(RoomBookedEvent event) {
        kafkaTemplate.send(roomBookedTopic, event);
    }
}