package com.example.hotel_service.repository;

import com.example.hotel_service.statistics.event.EventDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventRepository extends MongoRepository<EventDocument, String> {

    List<EventDocument> findByEventType(String eventType);
}
