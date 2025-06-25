package com.example.hotel_service.dto.responce;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Builder
public class PaginatedResponse<T> {

    private List<T> items;
    private int currentPage;
    private int pageSize;
    private long totalItems;
    private int totalPages;

    public static <T, R> PaginatedResponse<R> of(Page<T> page, Function<? super T, ? extends R> mapper) {
        List<R> list = page.getContent()
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
        return PaginatedResponse.<R>builder()
                .items(list)
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
