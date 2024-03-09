package knusearch.clear.jpa.domain.dto;

import java.time.LocalDate;

public record SearchResult(String id, String title, String text,
                           String image, String url, LocalDate localDate,
                           String classification) {
}
