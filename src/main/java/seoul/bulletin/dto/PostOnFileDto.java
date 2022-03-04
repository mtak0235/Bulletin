package seoul.bulletin.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostOnFileDto {
    private Long id;
    private String title;
    private String content;
    private String author;

    @Builder
    public PostOnFileDto(Long id, String title, String content, String author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
    }
}
