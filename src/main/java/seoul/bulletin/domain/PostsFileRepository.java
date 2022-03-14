package seoul.bulletin.domain;

import seoul.bulletin.dto.PostOnFileDto;

public interface PostsFileRepository {

    void updateOnFile(PostOnFileDto post);

    void deleteOnFile(Long id);

    Long saveOnFile(PostOnFileDto post);

    PostOnFileDto findByIdOnFile(Long id) throws Exception;
}
