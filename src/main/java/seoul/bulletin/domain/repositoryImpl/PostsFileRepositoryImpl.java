package seoul.bulletin.domain.repositoryImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import seoul.bulletin.domain.PostsFileRepository;
import seoul.bulletin.dto.PostOnFileDto;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Component
public class PostsFileRepositoryImpl implements PostsFileRepository {

    private FileWriter dataFileWriter;
    private BufferedWriter dataFileBufferedWriter;
    private FileReader dataFileReader;
    private BufferedReader dataFileBufferedReader;

    @Override
    public void updateOnFile(PostOnFileDto post){
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteOnFile(Long id){

        String readLine = null;
        try {
            openFileReader("data.txt");
            BufferedWriter tmpBW = new BufferedWriter(new FileWriter("tmp.txt", true));
            StringBuffer readLineB = new StringBuffer();
            while ((readLine = this.dataFileBufferedReader.readLine()) != null) {
                if (readLine.contains("{\"id\":" + id)) continue;
                else
                    readLineB.append(readLine + "\n");
            tmpBW.write(readLineB.toString());
            tmpBW.newLine();
            tmpBW.flush();
            this.dataFileBufferedReader.close();
            Path tmpFileName = Paths.get("tmp.txt");
            Path newFileName = Paths.get("data.txt");
            Files.move(tmpFileName, newFileName, StandardCopyOption.REPLACE_EXISTING);
            tmpBW.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
    }

    @Override
    public Long saveOnFile(PostOnFileDto post) {
        try {
            openFileWriter("data.txt", "index.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String strJson = getStringFromPostOnFileDto(post);
        try {
            this.dataFileBufferedWriter.write(strJson);
            this.dataFileBufferedWriter.newLine();
            this.dataFileBufferedWriter.flush();
            this.dataFileBufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        return post.getId();
    }

    @Override
    public PostOnFileDto findByIdOnFile(Long id) throws Exception {
        openFileReader("data.txt");
        String readLine = null;
        while ((readLine = this.dataFileBufferedReader.readLine()) != null) {
            if (readLine.contains("{\"id\":" + id))
                break;
        }
        this.dataFileBufferedReader.close();
        ObjectMapper mapper = new ObjectMapper();
        PostOnFileDto post = mapper.readValue(readLine, PostOnFileDto.class);
        return post;
    }

    private String getStringFromPostOnFileDto(PostOnFileDto post) {
        String strJson = "{" +
                "\"id\":" + post.getId() +
                ",\"title\":\"" + post.getTitle() +
                "\",\"content\":\"" + post.getContent() +
                "\",\"author\":\"" + post.getAuthor() +
                "\"}";
        return strJson;
    }

    /*
    * 어?
    *
    * */
    private void openFileWriter(String dataFileName, String indexFileName) throws IOException {
        try {
            this.dataFileBufferedWriter = new BufferedWriter(dataFileWriter);
            this.dataFileWriter = new FileWriter(dataFileName, true);
        } catch (IOException e) {
            this.dataFileWriter = new FileWriter(dataFileName, true);
            log.info(e.getMessage());
        }
    }

    private void openFileReader(String dataFileName) throws IOException {
        this.dataFileReader = new FileReader(dataFileName);
        this.dataFileBufferedReader = new BufferedReader(dataFileReader);
    }
}
