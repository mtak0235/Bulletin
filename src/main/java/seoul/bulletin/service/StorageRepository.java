package seoul.bulletin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoul.bulletin.domain.entity.Posts;
import seoul.bulletin.domain.repositoryImpl.FilePostsRepository;
import seoul.bulletin.domain.repositoryImpl.MySQLPostsRepository;
import seoul.bulletin.dto.PostsListResponseDto;
import seoul.bulletin.dto.PostsUpdateRequestDto;

import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StorageRepository {
    private final MySQLPostsRepository mySQLPostsRepository;
    private final FilePostsRepository filePostsRepository;

    @Transactional
    public Long save(Posts target) {
        try {
            filePostsRepository.openFileWriter("test.txt", "index.txt");
            Posts savedPost = mySQLPostsRepository.save(target);
            String strJson = getStringFromPost(savedPost);
            filePostsRepository.save(strJson, savedPost.getId());
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
        return mySQLPostsRepository.findById(id);
    }

    @Transactional
    public boolean delete(Long id) {
        try {
            filePostsRepository.openFileWriter("test.txt", "index.txt");
            Posts posts = mySQLPostsRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("그런 게시글 없다. id=" + id));
            File indexFile = new File("index.txt");
            if (indexFile.isFile()) {
                filePostsRepository.openFileReader("test.txt", "index.txt");
                mySQLPostsRepository.delete(posts);
                filePostsRepository.delete(id);
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
        Posts posts = mySQLPostsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("그런 게시글 없다. id" + id));
        posts.update(requestDto.getTitle(), requestDto.getContent());
        try {
            filePostsRepository.update(getStringFromPost(posts), id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Transactional
    public List<PostsListResponseDto> getPostsListResponseDtos() {
        return mySQLPostsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }
}
