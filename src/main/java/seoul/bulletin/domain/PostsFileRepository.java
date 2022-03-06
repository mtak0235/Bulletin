package seoul.bulletin.domain;

import seoul.bulletin.dto.PostOnFileDto;

import java.io.IOException;

public interface PostsFileRepository {

    boolean updateOnFile(PostOnFileDto post) throws IOException;

    void deleteOnFile(Long id) throws IOException;

    Long saveOnFile(PostOnFileDto post) throws IOException;

    PostOnFileDto findByIdOnFile(Long id) throws Exception;
}
