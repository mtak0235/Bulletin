package seoul.bulletin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seoul.bulletin.domain.Posts;
import seoul.bulletin.domain.PostsRepository;
import seoul.bulletin.web.PostsResponseDto;
import seoul.bulletin.web.PostsSaveRequestDto;
import seoul.bulletin.web.PostsUpdateRequestDto;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntiy()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("그런 게시글 없다. id" + id));
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    @Transactional
    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("그런 게시글 없다. id" + id));
        return new PostsResponseDto(entity);
    }
}
