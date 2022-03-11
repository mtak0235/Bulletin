package seoul.bulletin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import seoul.bulletin.domain.entity.Posts;

import java.util.Optional;

@NoArgsConstructor
@Getter
public class PostsResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;

    public PostsResponseDto(Posts postOnDB) {
        this.id = postOnDB.getId();
        this.title = postOnDB.getTitle();
        this.content = postOnDB.getContent();
        this.author = postOnDB.getAuthor();
    }

}
