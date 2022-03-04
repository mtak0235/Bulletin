package seoul.bulletin.service;

import org.springframework.stereotype.Component;
import seoul.bulletin.domain.entity.Posts;
import seoul.bulletin.dto.PostOnEmailDto;
import seoul.bulletin.dto.PostOnExcelDto;
import seoul.bulletin.dto.PostOnFileDto;

@Component
public class Post2XService {

    public PostOnEmailDto post2Email(Posts post) {
        return PostOnEmailDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor())
                .build();
    }

    public PostOnExcelDto post2Excel(Posts post) {
        return PostOnExcelDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor())
                .build();

    }

    public PostOnFileDto post2File(Posts post) {
        return PostOnFileDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor())
                .build();
    }
}
