package knusearch.clear.service.post;

import knusearch.clear.domain.post.BasePost;
import knusearch.clear.domain.post.PostIct;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface BasePostService<T extends BasePost> {


    void checkAndSave(BasePost basePost,String finalURL);

    T getNewPostInstance();

    void savePost(BasePost postMain);

    String getBaseUrl();

    String[] getAllPostUrl();

}
