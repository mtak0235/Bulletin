package seoul.bulletin.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import seoul.bulletin.domain.entity.Posts;
import seoul.bulletin.domain.repositoryImpl.FileRepository;
import seoul.bulletin.domain.repositoryImpl.PostsRepository;
import seoul.bulletin.dto.PostsListResponseDto;
import seoul.bulletin.dto.PostsUpdateRequestDto;

import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class StorageRepository {
    private final PostsRepository postsRepository;
    private final FileRepository fileRepository;

    @Transactional
    public Long save(Posts target) {
        try {
            fileRepository.openFileWriter("test.txt", "index.txt");
            Posts savedPost = postsRepository.save(target);
            String strJson = getStringFromPost(savedPost);
            fileRepository.save(strJson, savedPost.getId());
            return savedPost.getId();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getStringFromPost(Posts savedPost) {
        String strJson = "{" +
                "\"id\":" + savedPost.getId() +
                ",\"title\":" + savedPost.getTitle() +
                ",\"content\":" + savedPost.getContent() +
                ",\"author\":" + savedPost.getAuthor() +
                "}";
        return strJson;
    }

    @Transactional
    public Optional<Posts> findById(Long id) {
        return postsRepository.findById(id);
    }

    @Transactional
    public boolean delete(Long id) {
        try {
            fileRepository.openFileWriter("test.txt", "index.txt");
            Posts posts = postsRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("그런 게시글 없다. id=" + id));
            File indexFile = new File("index.txt");
            if (indexFile.isFile()) {
                fileRepository.openFileReader("test.txt", "index.txt");
                postsRepository.delete(posts);
                fileRepository.delete(id);
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("그런 게시글 없다. id" + id));
        posts.update(requestDto.getTitle(), requestDto.getContent());
        try {
            fileRepository.update(getStringFromPost(posts), id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Transactional
    public List<PostsListResponseDto> getPostsListResponseDtos() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }
}
