package seoul.bulletin.domain.repositoryImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import seoul.bulletin.domain.PostsFileRepository;
import seoul.bulletin.dto.PostOnFileDto;
import seoul.bulletin.exception.UserException;

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
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());


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
        openFileWriter("data.txt");
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
    public PostOnFileDto findByIdOnFile(Long id) throws UserException, IOException {
        openFileReader("data.txt");
        String readLine = null;
        while ((readLine = this.dataFileBufferedReader.readLine()) != null) {
            if (readLine.contains("{\"id\":" + id))
                break;
        }
        this.dataFileBufferedReader.close();
        ObjectMapper mapper = new ObjectMapper();
        if (readLine == null) {
            throw new UserException("no such exception data in file");
        }
        PostOnFileDto post = mapper.readValue(readLine, PostOnFileDto.class);
        return post;
    }

    private String getStringFromPostOnFileDto(PostOnFileDto post) {
        String strJson = null;
        try {
            strJson = objectMapper.writeValueAsString(post);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.info("object to str에 실패했다.");
        }
        return strJson;
    }

    /*
    * 어?
    *
    * */
    private void openFileWriter(String dataFileName){
        try {
            this.dataFileWriter = new FileWriter(dataFileName, true);
            this.dataFileBufferedWriter = new BufferedWriter(dataFileWriter);
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    private void openFileReader(String dataFileName) throws IOException {
        this.dataFileReader = new FileReader(dataFileName);
        this.dataFileBufferedReader = new BufferedReader(dataFileReader);
    }
}
