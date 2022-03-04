package seoul.bulletin.domain.repositoryImpl;

import org.springframework.stereotype.Component;
import seoul.bulletin.domain.PostsFileRepository;
import seoul.bulletin.dto.PostOnFileDto;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class PostsFileRepositoryImpl implements PostsFileRepository {

    private FileWriter dataFileWriter;
    private BufferedWriter dataFileBufferedWriter;
    private FileReader dataFileReader;
    private BufferedReader dataFileBufferedReader;

    @Override
    public boolean updateOnFile(PostOnFileDto post) throws IOException {
        openFileReader("data.txt");
        BufferedWriter tmpBW = new BufferedWriter(new FileWriter("tmp.txt", true));
        String readLine = null;
        StringBuffer readLineB = new StringBuffer();
        while ((readLine = this.dataFileBufferedReader.readLine()) != null) {
            if (readLine.contains("{\"id\":" + post.getId()))
                readLine = getStringFromPostOnFileDto(post);
            readLineB.append(readLine + "\n");
        }
        tmpBW.write(readLineB.toString());
        this.dataFileBufferedReader.close();
        Path tmpFileName = Paths.get("tmp.txt");
        Path newFileName = Paths.get("data.txt");
        Files.move(tmpFileName, newFileName, StandardCopyOption.REPLACE_EXISTING);
        tmpBW.close();
        return false;
    }

    @Override
    public void deleteOnFile(Long id) throws IOException {
        openFileReader("data.txt");
        BufferedWriter tmpBW = new BufferedWriter(new FileWriter("tmp.txt", true));
        String readLine = null;
        StringBuffer readLineB = new StringBuffer();
        while ((readLine = this.dataFileBufferedReader.readLine()) != null) {
            if (readLine.contains("{\"id\":" + id)) continue;
            else
                readLineB.append(readLine + "\n");
        }
        tmpBW.write(readLineB.toString());
        tmpBW.newLine();
        tmpBW.flush();
        this.dataFileBufferedReader.close();
        Path tmpFileName = Paths.get("tmp.txt");
        Path newFileName = Paths.get("data.txt");
        Files.move(tmpFileName, newFileName, StandardCopyOption.REPLACE_EXISTING);
        tmpBW.close();
    }

    @Override
    public Long saveOnFile(PostOnFileDto post) throws IOException {
        openFileWriter("data.txt", "index.txt");
        String strJson = getStringFromPostOnFileDto(post);
        this.dataFileBufferedWriter.write(strJson);
        this.dataFileBufferedWriter.newLine();
        this.dataFileBufferedWriter.flush();
        this.dataFileBufferedWriter.close();
        return post.getId();
    }

    private String getStringFromPostOnFileDto(PostOnFileDto post) {
        String strJson = "{" +
                "\"id\":" + post.getId() +
                ",\"title\":" + post.getTitle() +
                ",\"content\":" + post.getContent() +
                ",\"author\":" + post.getAuthor() +
                "}";
        return strJson;
    }

    private void openFileWriter(String dataFileName, String indexFileName) throws IOException {
        this.dataFileWriter = new FileWriter(dataFileName, true);
        this.dataFileBufferedWriter = new BufferedWriter(dataFileWriter);
    }

    private void openFileReader(String dataFileName) throws IOException {
        this.dataFileReader = new FileReader(dataFileName);
        this.dataFileBufferedReader = new BufferedReader(dataFileReader);
    }
}
