package seoul.bulletin.domain;

import seoul.bulletin.domain.entity.Posts;
import seoul.bulletin.dto.PostOnEmailDto;
import seoul.bulletin.dto.PostsUpdateRequestDto;

public interface PostsEmailRepository {
    boolean updateOnEmail(PostOnEmailDto post);

    boolean deleteOnEmail(Long id);

    Long saveOnEmail(PostOnEmailDto posts);
}
