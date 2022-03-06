package seoul.bulletin.domain;

import seoul.bulletin.dto.PostOnExcelDto;

import java.io.FileNotFoundException;

public interface PostsExcelRepository {
    boolean updateOnExcel(PostOnExcelDto id);

    boolean deleteOnExcel(Long id);

    Long saveOnExcel(PostOnExcelDto posts) throws FileNotFoundException;

    PostOnExcelDto findByIdOnExcel(Long id);
}
