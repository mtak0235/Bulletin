package seoul.bulletin.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostsUpdateRequestDto {
    private String title;
    private String content;
    private Long id;

    @Builder
    public PostsUpdateRequestDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

}
