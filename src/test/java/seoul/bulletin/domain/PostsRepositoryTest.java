package seoul.bulletin.domain;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.
        Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import seoul.bulletin.dto.PostsUpdateRequestDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PostsRepositoryTest {
    @Autowired
    PostsRepository postsRepository;

    @AfterEach
    public void cleanup() {
        postsRepository.deleteAll();
    }

    @Test
    public void 게시글_저장() {
        String title = "이것은 제목";
        String content = "이것은 내용";
        String author = "이것은 글쓴이";
        Posts tmp = postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .build());
        List<Posts> postsList = postsRepository.findAll();
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
        assertThat(posts.getAuthor()).isEqualTo(author);
    }

    @Test
    public void 게시글_삭제() {
        String title = "이것은 제목";
        String content = "이것은 내용";
        String author = "이것은 글쓴이";
        Posts tmp;
        tmp = postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .build());
        List<Posts> postsList = postsRepository.findAll();
        postsRepository.delete(postsList.get(0));
        org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class, () -> postsRepository.findById(tmp.getId()).get());
    }

    @Test
    public void 게시글_수정() {
        String title = "이것은 제목";
        String content = "이것은 내용";
        String author = "이것은 글쓴이";
        Posts savedPosts = postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .build());
        Long udpatedId = savedPosts.getId();
        String expectedTitle = "바뀐 제목";
        String expectedContent = "바뀐 내용";
        savedPosts.update(expectedTitle, expectedContent);
        assertThat(savedPosts.getTitle()).isEqualTo(expectedTitle);
        assertThat(savedPosts.getContent()).isEqualTo(expectedContent);
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