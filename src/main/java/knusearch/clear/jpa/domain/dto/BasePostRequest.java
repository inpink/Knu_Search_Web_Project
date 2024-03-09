package knusearch.clear.jpa.domain.dto;

import java.time.LocalDate;

public record BasePostRequest(Long id, String url, String title,
                              String text, String image, LocalDate dateTime,
                              String classification) {
}
