package seoul.bulletin.dto;

import lombok.Getter;
import seoul.bulletin.domain.entity.Posts;

import java.time.LocalDateTime;

@Getter
public class PostsResponse4ListDto {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime modifiedDate;

    public PostsResponse4ListDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.modifiedDate = entity.getModifiedDate();
    }
}
