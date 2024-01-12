package knusearch.clear.jpa.domain.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ClassificationUpdateRequest {
    private final Long postId;
    private final String classification;
}
