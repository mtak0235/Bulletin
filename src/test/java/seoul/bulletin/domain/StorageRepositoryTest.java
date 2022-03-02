package seoul.bulletin.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import seoul.bulletin.domain.repositoryImpl.PostsRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional

class StorageRepositoryTest {

    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private PostsRepository postsRepository;

    @AfterEach
    public void cleanup() {
        postsRepository.deleteAll();
    }


}