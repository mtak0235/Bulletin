package seoul.bulletin.domain.repositoryImpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import seoul.bulletin.domain.entity.Posts;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    @Query("SELECT p FROM Posts AS p ORDER BY p.id DESC")
    List<Posts> findAllDesc();
}
