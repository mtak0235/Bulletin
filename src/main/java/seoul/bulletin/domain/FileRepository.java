package seoul.bulletin.domain;

import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class FileRepository {

    private FileWriter dataFileWriter;
    private BufferedWriter dataFileBufferedWriter;
    private FileWriter indexFileWriter;
    private BufferedWriter indexFileBufferedWriter;
    private FileReader indexFileReader;
    private BufferedReader indexFileBufferedReader;
    private FileReader dataFileReader;
    private BufferedReader dataFileBufferedReader;

    public void openFileWriter(String dataFileName, String indexFileName) throws IOException {
        this.dataFileWriter = new FileWriter(dataFileName, true);
        this.dataFileBufferedWriter = new BufferedWriter(dataFileWriter);
        this.indexFileWriter = new FileWriter(indexFileName, true);
        this.indexFileBufferedWriter = new BufferedWriter(indexFileWriter);
    }

    public void openFileReader(String dataFileName, String indexFileName) throws IOException {
        this.dataFileReader = new FileReader(dataFileName);
        this.dataFileBufferedReader = new BufferedReader(dataFileReader);
        this.indexFileReader = new FileReader(indexFileName);
        this.indexFileBufferedReader = new BufferedReader(indexFileReader);
    }

    public void save(String toSave, Long id) throws IOException {
        dataFileBufferedWriter.write(toSave);
        dataFileBufferedWriter.newLine();
        dataFileBufferedWriter.flush();

        indexFileBufferedWriter.write(String.valueOf(id));
        indexFileBufferedWriter.newLine();
        indexFileBufferedWriter.flush();
    }

    public void delete(Long id) {
        String readline = null;
        //index, test 파일 변경
    }

    public void update(String toSave, Long id) throws IOException {
        delete(id);
        save(toSave, id);
    }
}
