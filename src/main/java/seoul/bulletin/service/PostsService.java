package seoul.bulletin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seoul.bulletin.domain.entity.Posts;
import seoul.bulletin.domain.PostsRepository;
import seoul.bulletin.dto.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final Post2XService post2XService;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        Posts savedPost = postsRepository.save(requestDto.toEntiy());
        try {
            postsRepository.saveOnFile(post2XService.post2File(savedPost));
            postsRepository.saveOnEmail(post2XService.post2Email(savedPost));
            postsRepository.saveOnExcel(post2XService.post2Excel(savedPost));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return savedPost.getId();
    }

    @Transactional
    public boolean delete(Long id) {
        Posts postToDelete = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("그런 게시글 없다. id=" + id));
        postsRepository.delete(postToDelete);
        try{
            postsRepository.deleteOnFile(id);
            postsRepository.deleteOnEmail(id);
            postsRepository.deleteOnExcel(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return !postsRepository.findById(id).isPresent();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts post = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("그런 게시글 없다. id" + id));
        post.update(requestDto.getTitle(), requestDto.getContent());
        try {
            postsRepository.updateOnFile(post2XService.post2File(post));
            postsRepository.updateOnEmail(post2XService.post2Email(post));
            postsRepository.updateOnExcel(post2XService.post2Excel(post));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post.getId();
    }

    @Transactional
    public PostsResponseDto findById(Long id) {
        Posts postOnDB = postsRepository.findById(id).get();
        PostOnFileDto postOnFile = null;
        PostOnExcelDto postOnExcel = null;
        try {
            postOnFile = postsRepository.findByIdOnFile(id);
            postOnExcel = postsRepository.findByIdOnExcel(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //post format = id + title + content + author
        //무슨 데이터를 줄지 조작 가능했으면 좋겠다.
        PostsResponseDto post = new PostsResponseDto(postOnDB, postOnFile, postOnExcel);
        return post;
    }

    @Transactional
    public List<PostsResponse4ListDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsResponse4ListDto::new)
                .collect(Collectors.toList());
    }
}
