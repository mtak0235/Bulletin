package seoul.bulletin.domain.repositoryImpl;

import seoul.bulletin.domain.PostsEmailRepository;
import seoul.bulletin.dto.PostOnEmailDto;

public class PostsEmailRepositoryImpl implements PostsEmailRepository {
    @Override
    public boolean updateOnEmail(PostOnEmailDto post) {
        return false;
    }

    @Override
    public boolean deleteOnEmail(Long id) {
        return false;
    }

    @Override
    public Long saveOnEmail(PostOnEmailDto posts) {
        return null;
    }
}
