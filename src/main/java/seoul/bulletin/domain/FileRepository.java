package seoul.bulletin.domain;

import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class FileRepository {

    private FileWriter dataFileWriter;
    private BufferedWriter dataFileBufferedWriter;
    private FileWriter indexFileWriter;
    private BufferedWriter indexFileBufferedWriter;

    public void openFile(String dataFileName, String indexFileName) throws IOException {
        this.dataFileWriter = new FileWriter(dataFileName, true);
        this.dataFileBufferedWriter = new BufferedWriter(dataFileWriter);
        this.indexFileWriter = new FileWriter(indexFileName, true);
        this.indexFileBufferedWriter = new BufferedWriter(indexFileWriter);
    }

    public void save(String toSave, Long id) throws IOException {
        dataFileBufferedWriter.write(toSave);
        dataFileBufferedWriter.newLine();
        dataFileBufferedWriter.flush();
        indexFileBufferedWriter.write(id.toString());
        indexFileBufferedWriter.newLine();
        indexFileWriter.flush();
    }

    public void delete(Long id) {

    }


}
