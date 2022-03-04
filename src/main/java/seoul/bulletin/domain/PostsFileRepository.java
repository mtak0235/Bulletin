package seoul.bulletin.domain;

import seoul.bulletin.dto.PostOnFileDto;

import java.io.IOException;

public interface PostsFileRepository {

    boolean updateOnFile(PostOnFileDto post) throws IOException;

    void deleteOnFile(Long id) throws IOException;

    Long saveOnFile(PostOnFileDto post) throws IOException;

//추가 저장 매체에 공공으로 구현해야 하는 부분 -. https://blog.naver.com/PostView.nhn?isHttpsRedirect=true&blogId=hankk20&logNo=221542810406

}
