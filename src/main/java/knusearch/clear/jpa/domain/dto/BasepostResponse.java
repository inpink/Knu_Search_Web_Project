package knusearch.clear.jpa.domain.dto;

import knusearch.clear.jpa.domain.Classification;
import knusearch.clear.jpa.domain.post.BasePost;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class BasepostResponse {
    private final long id;
    private final String title;
    private final String text;
    private final String classification;

    public static BasepostResponse toBasepostResponse(BasePost basePost) {
        return BasepostResponse.builder()
                .id(basePost.getId())
                .title(basePost.getTitle())
                .text(basePost.getText())
                .classification(Classification.findDescription(basePost.getClassification()))
                .build();
    }

}
