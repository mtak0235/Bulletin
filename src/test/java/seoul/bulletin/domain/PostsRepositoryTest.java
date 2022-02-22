package seoul.bulletin.domain;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.
        Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {
    @Autowired
    PostsRepository postsRepository;

    @After
    public void cleanup() {
        postsRepository.deleteAll();
    }

    @Test
    public void 게시글_저장() {
        String title = "이것은 제목";
        String content = "이것은 내용";
        String author = "이것은 글쓴이";
        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .build());
        List<Posts> postsList = postsRepository.findAll();
        Posts posts = postsList.get(0);
        Assertions.assertThat(posts.getTitle()).isEqualTo(title);
        Assertions.assertThat(posts.getContent()).isEqualTo(content);
        Assertions.assertThat(posts.getAuthor()).isEqualTo(author);
    }

    @Test
    public void BaseTimeEntity_등록() {
        LocalDateTime now = LocalDateTime.of(2022, 2, 23, 0, 0, 0);
        postsRepository.save(Posts.builder()
                .title("이건 제목")
                .content("이건 내용")
                .author("이것은 글쓴이")
                .build());
        List<Posts> postsList = postsRepository.findAll();
        Posts posts = postsList.get(0);
        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }
}