package com.example.hotel_service.statistics.report;

import com.example.hotel_service.statistics.event.Event;

public interface Report<T extends Event> {

    String report(T t);
}
