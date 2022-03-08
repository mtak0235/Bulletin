package seoul.bulletin.domain;

import seoul.bulletin.dto.PostOnEmailDto;

public interface PostsEmailRepository {
    String authUserName = "jesustark0235@gmail.com";
    String authPassword = "gxkogiajxttpmmjv";

    boolean updateOnEmail(PostOnEmailDto post);

    boolean deleteOnEmail(Long id);

    Long saveOnEmail(PostOnEmailDto posts);
}
