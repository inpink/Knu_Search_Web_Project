package knusearch.clear.service.post;

import knusearch.clear.domain.post.BasePost;

public interface BasePostService { //현재는 사용안하고 있음
    public void savePostMain(BasePost postMain);

    public String[] getAllBaseUrl();

}
