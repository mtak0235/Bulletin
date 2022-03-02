package seoul.bulletin.domain;

import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
@RequiredArgsConstructor
public class FileRepository {

    private final JSONParser jsonParser;
    private FileWriter dataFileWriter;
    private BufferedWriter dataFileBufferedWriter;
    private FileWriter indexFileWriter;
    private BufferedWriter indexFileBufferedWriter;
    private FileReader indexFileReader;

    private BufferedReader indexFileBufferedReader;
    private FileReader dataFileReader;
    private BufferedReader dataFileBufferedReader;

    public BufferedReader getIndexFileBufferedReader() {
        return indexFileBufferedReader;
    }

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

        indexFileBufferedWriter.write(id.toString());
        indexFileBufferedWriter.newLine();
        indexFileWriter.flush();
    }

    public void delete(Long id) throws IOException {
        String readline = null;
        String dummy = "";
        while ((readline = dataFileBufferedReader.readLine()) != null) {
            Object 
            JSONPObject jsonpObject = (JSONPObject)
            if (readline.)
        }

        File file = new File(sumFilePath);

        String dummy = "";

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            //BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));


            //1. 삭제하고자 하는 position 이전까지는 이동하며 dummy에 저장

            String line;

            for (int i = 0; i < position; i++) {

                line = br.readLine(); //읽으며 이동

                dummy += (line + "\r\n");

            }


            //2. 삭제하고자 하는 데이터는 건너뛰기

            String delData = br.readLine();

            Log.d("mstag", "삭제되는 데이터 = " + delData);


            //3. 삭제하고자 하는 position 이후부터 dummy에 저장

            while ((line = br.readLine()) != null) {

                dummy += (line + "\r\n");

            }


            //4. FileWriter를 이용해서 덮어쓰기

            FileWriter fw = new FileWriter(sumFilePath);

            fw.write(dummy);


            //bw.close();

            fw.close();

            br.close();

        } catch (Exception e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }
    }


}
