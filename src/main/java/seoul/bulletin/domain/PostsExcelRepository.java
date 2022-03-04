package seoul.bulletin.domain;

import seoul.bulletin.dto.PostOnExcelDto;

public interface PostsExcelRepository {
    boolean updateOnExcel(PostOnExcelDto id);

    boolean deleteOnExcel(Long id);

    Long saveOnExcel(PostOnExcelDto posts);
}
