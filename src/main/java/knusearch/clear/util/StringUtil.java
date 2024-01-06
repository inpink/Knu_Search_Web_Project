package knusearch.clear.util;

public class StringUtil {
    private StringUtil() {

    }

    public static String deleteLineSeparator(String targetStr) {
        return targetStr.replaceAll("(\r\n|\r|\n|\n\r)", "");
    }
}
