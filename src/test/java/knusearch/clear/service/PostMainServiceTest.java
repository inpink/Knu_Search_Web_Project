package knusearch.clear.service;

import static org.junit.Assert.assertEquals;

import knusearch.clear.jpa.domain.post.BasePost;
import knusearch.clear.jpa.domain.post.PostMain;
import knusearch.clear.jpa.service.CrawlService;
import knusearch.clear.jpa.service.post.BasePostService;
import knusearch.clear.jpa.service.post.PostMainService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PostMainServiceTest {

    @Mock
    private CrawlService crawlService;

    @InjectMocks
    private PostMainService postMainService;

    @Test
    public void updateClassification() {
        // Given
        String newClassification = "someClassification";
        BasePost basePost = new PostMain(); // Assuming a default constructor is available
        basePost.setClassification("oldClassification");

        // When
        postMainService.updateClassification(basePost, newClassification);

        // Then
        assertEquals(newClassification, basePost.getClassification());
    }
}
