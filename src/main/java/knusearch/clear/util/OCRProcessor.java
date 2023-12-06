package knusearch.clear.util;

import java.io.File;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class OCRProcessor {
    public static String extractTextFromImage(String imagePath) {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata"); // tessdata 경로 설정
        tesseract.setLanguage("kor"); // 한국어 설정

        try {
            String text = tesseract.doOCR(new File(imagePath));
            return text;
        } catch (TesseractException e) {
            e.printStackTrace();
            return null;
        }
    }
}
