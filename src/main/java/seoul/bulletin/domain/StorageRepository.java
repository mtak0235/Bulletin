package seoul.bulletin.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class StorageRepository {
    private final PostsRepository postsRepository;
    private final FileRepository fileRepository;

    @Transactional
    public Long save(Object target) {
        try {
            fileRepository.openFileWriter("test.txt", "index.txt");
            Posts savedPost = postsRepository.save((Posts) target);
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
                String readline = null;
                while ((readline = fileRepository.getIndexFileBufferedReader().readLine()) != null) {
                    if (readline.equals(id)) {
                        postsRepository.delete(posts);
                        fileRepository.delete(id);
                        break;
                    }
                }
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
//
//    @Transactional
//    public Long udpate(Long id, T from) {
//
//    }


}
