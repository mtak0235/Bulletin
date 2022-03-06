package seoul.bulletin.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostOnEmailDto {
    private Long id;
    private String title;
    private String content;
    private String author;

    @Builder
    public PostOnEmailDto(Long id, String title, String content, String author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
    }
}
