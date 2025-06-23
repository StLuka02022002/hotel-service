package com.example.hotel_service.specification;

import com.example.hotel_service.entity.Hotel;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HotelSpecification {

    public static Specification<Hotel> hasId(UUID id) {
        return (root, query, criteriaBuilder) ->
                id == null ? null : criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<Hotel> hasName(String name) {
        return createLikeSpecification("name", name);
    }

    public static Specification<Hotel> hasTitle(String title) {
        return createLikeSpecification("title", title);
    }

    public static Specification<Hotel> hasAddress(String address) {
        return (root, query, criteriaBuilder) -> {
            if (!(address != null && !address.isBlank())) {
                return null;
            }
            String[] searches = address.toLowerCase().split("\\W+");
            List<Predicate> predicates = new ArrayList<>();
            for (String searchWord : searches) {
                if (searchWord.isBlank()) {
                    continue;
                }
                String search = "%" + searchWord + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("city").get("name")), search),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("city").get("country")), search),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("address").get("district")), search),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("address").get("street")), search),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("address").get("postalCode")), search),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("address").get("apartment")), search)
                ));

                if (searchWord.matches("\\d+")) {
                    predicates.add(criteriaBuilder.equal(root.get("address").get("buildingNumber"), Integer.parseInt(searchWord)));
                }
            }
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Hotel> hasDistanceFromCenter(Double distance) {
        return (root, query, criteriaBuilder) -> distance == null ? null :
                criteriaBuilder.lessThanOrEqualTo(root.get("distanceFromCentreCity"), distance);
    }

    public static Specification<Hotel> hasMinRating(Double minRating) {
        return createGreaterThanOrEqualToSpecification("rating", minRating);
    }

    public static Specification<Hotel> hasMinEstimationCount(Integer minCount) {
        return createGreaterThanOrEqualToSpecification("countEstimation", minCount == null ? null : Double.valueOf(minCount));
    }

    private static Specification<Hotel> createLikeSpecification(String field, String value) {
        return (root, query, criteriaBuilder) -> !(value != null && !value.isBlank()) ? null :
                criteriaBuilder.like(criteriaBuilder.lower(root.get(field)), "%" + value.toLowerCase() + "%");
    }

    private static Specification<Hotel> createGreaterThanOrEqualToSpecification(String field, Double value) {
        return (root, query, criteriaBuilder) -> value == null ? null :
                criteriaBuilder.greaterThanOrEqualTo(root.get(field), value);
    }
}
