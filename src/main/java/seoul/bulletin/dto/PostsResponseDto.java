package seoul.bulletin.dto;

import lombok.Getter;
import seoul.bulletin.domain.entity.Posts;

import java.util.Optional;

@Getter
public class PostsResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private PostOnFileDto postOnFileDto;
    private PostOnExcelDto postOnExcelDto;

    public PostsResponseDto(Posts postOnDB) {
        this.id = postOnDB.getId();
        this.title = postOnDB.getTitle();
        this.content = postOnDB.getContent();
        this.author = postOnDB.getAuthor();
//        this.postOnFileDto = postOnFile.orElse(null);
//        this.postOnExcelDto = postOnExcel.orElse(null);
    }

    public PostsResponseDto(Posts postOnDB, PostOnFileDto postOnFile, PostOnExcelDto postOnExcel) {
        this.id = postOnDB.getId();
        this.title = postOnDB.getTitle();
        this.content = postOnDB.getContent();
        this.author = postOnDB.getAuthor();
        this.postOnFileDto = postOnFile;
        this.postOnExcelDto = postOnExcel;
    }
}
