package seoul.bulletin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seoul.bulletin.domain.Posts;
import seoul.bulletin.domain.PostsRepository;
import seoul.bulletin.dto.PostsListResponseDto;
import seoul.bulletin.dto.PostsResponseDto;
import seoul.bulletin.dto.PostsSaveRequestDto;
import seoul.bulletin.dto.PostsUpdateRequestDto;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntiy()).getId();
    }

    @Transactional
    public boolean delete (Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("그런 게시글 없다. id=" + id));
        postsRepository.delete(posts);
        return !postsRepository.findById(id).isPresent();
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

    @Transactional
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

}
