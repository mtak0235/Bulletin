package seoul.bulletin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import seoul.bulletin.domain.PostsRepository;
import seoul.bulletin.domain.entity.Posts;
import seoul.bulletin.dto.*;

import javax.transaction.Transactional;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class PostsService {

    private final PostsRepository postsRepository;
    private final Post2XService post2XService;
    private final Json2XService json2XService;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final JSONParser jsonParser = new JSONParser();


    private RestTemplate restTemplate = new RestTemplate();



    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        Posts savedPost = postsRepository.save(requestDto.toEntiy());
        try {
            postsRepository.saveOnFile(post2XService.post2File(savedPost));
            postsRepository.saveOnEmail(post2XService.post2Email(savedPost));
            postsRepository.saveOnExcel(post2XService.post2Excel(savedPost));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return savedPost.getId();
    }

    @Transactional
    public boolean delete(Long id) {
        Posts postToDelete = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("그런 게시글 없다. id=" + id));
        postsRepository.delete(postToDelete);
        try{
            postsRepository.deleteOnFile(id);
            postsRepository.deleteOnEmail(id);
            postsRepository.deleteOnExcel(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return !postsRepository.findById(id).isPresent();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts post = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("그런 게시글 없다. id" + id));
        post.update(requestDto.getTitle(), requestDto.getContent());
        try {
            postsRepository.updateOnFile(post2XService.post2File(post));
            postsRepository.updateOnEmail(post2XService.post2Email(post));
            postsRepository.updateOnExcel(post2XService.post2Excel(post));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post.getId();
    }

    @Transactional
    public PostsResponseDto findById(Long id) {
        Posts postOnDB = postsRepository.findById(id).get();
        PostsResponseDto post = new PostsResponseDto(postOnDB);
        return post;
    }
    /*
    아이디로 3개 저장소에서 데이터를 가져온다
    * input : id(Long)
    * output : jsonString(Excel(id, title, author, content),
    *  file(id, title, author, content)
    * , database(id, title, author, content))
    * */
    @Transactional
    public String findByIdOnDBNFileNExcel(Long id) throws JsonProcessingException, ParseException {
        Posts postOnDB = null;
        PostOnFileDto postOnFile = null;
        PostOnExcelDto postOnExcel = null;
        try {
            postOnDB = postsRepository.findById(id).get();
            postOnFile = postsRepository.findByIdOnFile(id);
            postOnExcel = postsRepository.findByIdOnExcel(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String DBData = objectMapper.writeValueAsString(postOnDB);
        String excelData = objectMapper.writeValueAsString(postOnExcel);
        String fileData = objectMapper.writeValueAsString(postOnFile);
        JSONObject ret = new JSONObject();
        ret.put("post-db", (JSONObject) jsonParser.parse(DBData));
        ret.put("post-excel", (JSONObject) jsonParser.parse(excelData));
        ret.put("post-file", (JSONObject) jsonParser.parse(fileData));
        return ret.toJSONString();
    }

    void getData(){
        getDataFromDB();
        getDatafromExcel();
        getDataFromFile();

    }

    void getDataFromDB(id) {
        postOnDB = postsRepository.findById(id).get();
        if (삑사리면)
            // log 남기고
            else { 넘겨주자..ㅍ}
    }
    void getDataFromFile()
    {
        postOnFile = postsRepository.findByIdOnFile(id);
    }
    void getDatafromExcel()
    {
        postOnExcel = postsRepository.findByIdOnExcel(id);
    }

    object getdb() {

    }
    @Transactional
    public List<PostsResponse4ListDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsResponse4ListDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostsResponseDto getPostInMtakPostAPI(Long id) {
        return restTemplate.getForObject("http://localhost:8080/posts/api?id={id}",
                PostsResponseDto.class, id);
    }

    @Transactional
    public ResponseEntity<Long> savePostInMtakPostAPI(PostsResponseDto post) {
        return restTemplate.postForEntity("http://localhost:8080/posts/api", post, Long.class);
    }

    @Transactional
    public PostsSaveRequestDto getPostInOutsidePostApi(String name) throws IOException, ParseException, URISyntaxException {
        URI uri = UriComponentsBuilder
                .fromUriString("http://apis.data.go.kr/1390802/AgriFood/FdImage/getKoreanFoodFdImageList")
                .queryParam("serviceKey", "csd/9isLnOcfaTZ9sdpArdEmVSBmX2L2Ml2Upn348u0yPkPYDAqp/LkA1zWCvUKMk8/1CZIiPuDhKxvp/JmuCw==")
                .queryParam("service_Type", "json")
                .queryParam("Page_No", "1")
                .queryParam("Page_Size", "2")
                .queryParam("food_Name", name)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();
        RequestEntity request = RequestEntity
                .get(uri)
                .header("Content-type", "application/json")
                .build();
        ResponseEntity<String> givenData = restTemplate.exchange(request, String.class);
        PostsSaveRequestDto givenPost = json2XService.json2PostsSaveRequestDto(givenData.getBody());
        return givenPost;
    }

    public String getStringFromFile(String fileName) {
        String readline = null;
        StringBuffer readlineB = new StringBuffer();
        File file = new File(fileName);
        if (!file.isFile()) {
            return null;
        }
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(fileName);
            br = new BufferedReader(fr);
            while ((readline = br.readLine()) != null) {
                readlineB.append(readline);
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readlineB.toString();
    }
}
