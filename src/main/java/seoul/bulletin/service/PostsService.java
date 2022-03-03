package seoul.bulletin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seoul.bulletin.domain.entity.Posts;
import seoul.bulletin.domain.repositoryImpl.MySQLPostsRepository;
import seoul.bulletin.dto.PostsListResponseDto;
import seoul.bulletin.dto.PostsResponseDto;
import seoul.bulletin.dto.PostsSaveRequestDto;
import seoul.bulletin.dto.PostsUpdateRequestDto;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final StorageRepository storageRepository;
    private final MySQLPostsRepository mySQLPostsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return storageRepository.save(requestDto.toEntiy());
    }

    @Transactional
    public boolean delete(Long id) {
        storageRepository.delete(id);
        return !storageRepository.findById(id).isPresent();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        return storageRepository.update(id, requestDto);
    }

    @Transactional
    public PostsResponseDto findById(Long id) {
        Optional<Posts> post = storageRepository.findById(id);
        return new PostsResponseDto(post.get());
    }

    @Transactional
    public List<PostsListResponseDto> findAllDesc() {
        return storageRepository.getPostsListResponseDtos();
    }
}
