package seoul.bulletin.domain.repositoryImpl;

import seoul.bulletin.domain.PostsExcelRepository;
import seoul.bulletin.dto.PostOnExcelDto;

import java.util.Optional;

public class PostsExcelRepositoryImpl implements PostsExcelRepository {
    @Override
    public boolean updateOnExcel(PostOnExcelDto id) {
        return false;
    }

    @Override
    public boolean deleteOnExcel(Long id) {
        return false;
    }

    @Override
    public Long saveOnExcel(PostOnExcelDto posts) {
        return null;
    }

    @Override
    public PostOnExcelDto findByIdOnExcel(Long id) {
        return null;
    }
}
