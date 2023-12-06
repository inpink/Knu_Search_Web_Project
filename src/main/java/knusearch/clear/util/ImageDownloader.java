package knusearch.clear.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.io.Resource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ImageDownloader {
    public static void downloadImage(String url, String filename) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        Resource resource = restTemplate.getForObject(url, Resource.class);
        if (resource != null) {
            try (InputStream in = resource.getInputStream()) {
                BufferedImage image = ImageIO.read(in);

                if (image != null) {
                    // 이미지를 JPEG 형식으로 저장
                    File outputFile = new File(filename + ".jpg");
                    ImageIO.write(image, "jpg", outputFile);
                }
            }
        }
    }
}