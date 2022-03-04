package seoul.bulletin.dto;

import lombok.Builder;

public class PostOnExcelDto {
    private Long id;
    private String title;
    private String content;
    private String author;

    @Builder
    public PostOnExcelDto(Long id, String title, String content, String author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
    }
}
