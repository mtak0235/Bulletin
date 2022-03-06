package seoul.bulletin.domain.repositoryImpl;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import seoul.bulletin.domain.PostsExcelRepository;
import seoul.bulletin.dto.PostOnExcelDto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

public class PostsExcelRepositoryImpl implements PostsExcelRepository {
    private Workbook workbook;
    private Sheet sheet;
    private File dataFile;
    private FileOutputStream fos;
    private int rowNUm;

    @Override
    public boolean updateOnExcel(PostOnExcelDto id) {
        return false;
    }

    @Override
    public boolean deleteOnExcel(Long id) {
        return false;
    }

    @Override
    public Long saveOnExcel(PostOnExcelDto posts) throws FileNotFoundException {
        this.fos = new FileOutputStream(dataFile);
        try {
            Row newRow = sheet.createRow(rowNUm++);
            newRow.createCell(0).setCellValue(posts.getId());
            newRow.createCell(1).setCellValue(posts.getTitle());
            newRow.createCell(2).setCellValue(posts.getContent());
            newRow.createCell(3).setCellValue(posts.getAuthor());
            workbook.write(fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return posts.getId();
    }

    @Override
    public PostOnExcelDto findByIdOnExcel(Long id) {
        return null;
    }

    public PostsExcelRepositoryImpl() throws FileNotFoundException {
        createExcel("data.xls");
    }

    private void createExcel(String fileName) throws FileNotFoundException {
        this.workbook = new HSSFWorkbook();
        this.sheet = workbook.createSheet("posts");
        this.dataFile = new File(fileName);
        this.rowNUm = sheet.getPhysicalNumberOfRows();
    }
}
