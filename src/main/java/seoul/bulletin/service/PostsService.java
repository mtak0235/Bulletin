package seoul.bulletin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import seoul.bulletin.domain.PostsRepository;
import seoul.bulletin.domain.entity.Posts;
import seoul.bulletin.dto.*;
import seoul.bulletin.exception.InsufficientException;

import javax.transaction.Transactional;
import java.io.*;
import java.net.URI;
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

    /*
    * post를 데이터베이스, 파일, 엑셀에 저장 및 이메일 발송
    * input : [PostSaveRequestDto] title, content, author
    * output : [Long] id
    * */
    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        Posts savedPost = postsRepository.save(requestDto.toEntiy());
        postsRepository.saveOnEmail(post2XService.post2Email(savedPost));
        postsRepository.saveOnFile(post2XService.post2File(savedPost));
        postsRepository.saveOnExcel(post2XService.post2Excel(savedPost));
        return savedPost.getId();
    }

    /*
    * post를 데이터베이스, 파일, 엑셀에서 삭제 및 이메일 발송
    * input : [Long] post의 id
    * output : [boolean] db에서 삭제 되었는지 아닌지.
    * */
    @Transactional
    public boolean delete(Long id) {
        Posts postToDelete = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("그런 게시글 없다. id=" + id));
        postsRepository.delete(postToDelete);
        postsRepository.deleteOnFile(id);
        postsRepository.deleteOnEmail(id);
        postsRepository.deleteOnExcel(id);

        return !postsRepository.findById(id).isPresent();
    }

    /*
    * post를 데이터베이스, 파일, 엑셀에서 수정 및 이메일 발송
    * input : [PostUpdateRequestDto] title, content, id
    * output : [Long] post의 id
    * */
    @Transactional
    public Long update(PostsUpdateRequestDto requestDto) {
        Posts post = postsRepository.findById(requestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("그런 게시글 없다. id" + requestDto.getId()));
        post.update(requestDto.getTitle(), requestDto.getContent());
        postsRepository.updateOnFile(post2XService.post2File(post));
        postsRepository.updateOnExcel(post2XService.post2Excel(post));
        postsRepository.updateOnEmail(post2XService.post2Email(post));
        return post.getId();
    }

    /*
    >>아 이거 밑에 getDataFromDB랑 리턴 빼고 겹치는데...
    * post를 데이터베이스에서 찾기
    * input : [Long] post의 id
    * output : [PostsResponseDto] id, title, content, author
    * 비고 : 없으면 exception 던짐
    */
    @Transactional
    public PostsResponseDto findByIdOnDB(Long id) throws IllegalArgumentException {
        Posts postOnDB =  postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("그런 게시글 없다. id" + id));
        return new PostsResponseDto(postOnDB);
    }

    /*
    * post를 3개 저장소(엑셀, 파일, 데이터베이스)에서 데이터를 가져와서 합친다.
    * input : id(Long)
    * output :[String] 저장소들에서 가져온 post의 데이터
    * */
    @Transactional
    public String findByIdOnDBNFileNExcel(Long id) {
        JSONObject ret;
        ret = getDataFromStorage(id);
        return ret.toJSONString();
    }

    /*
    * post를 복수개의 저장소에서 가져와 합친다.
    * input : [Long] id
    * output : [JSONObject] 'post - [storage name]' - 'data'
    * 비고 : 한 저장소에 없더라도 남은 데이터라도 리턴한다.
     */
    private JSONObject getDataFromStorage(Long id) {
        JSONObject ret = new JSONObject();

        ret.put("post-db", getDataFromDB(id));
        ret.put("post-excel",getDataFromFile(id));
        ret.put("post-file", getDatafromExcel(id));
        return ret;
    }

    /*
    * post를 데이터베이스에서 가져온다.
    * input : id
    * output : [JSONObject] id, content, title author
    * */
    private JSONObject getDataFromDB(Long id) throws IllegalArgumentException {
        JSONObject data = null;
        try {
            Posts post = postsRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("데이터베이스에 그런 post 없음."));
            data = (JSONObject) jsonParser.parse(objectMapper.writeValueAsString(post));
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        } finally {
            return data;
        }
    }

    /*
     * post를 파일에서 가져온다.
     * input : id
     * output : [JSONObject] id, content, title author
     * */
    private JSONObject getDataFromFile(Long id)
    {
        JSONObject data = null;
        try {
            PostOnFileDto post = null;
            post = postsRepository.findByIdOnFile(id);
            data = (JSONObject) jsonParser.parse(objectMapper.writeValueAsString(post));
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
       finally {
            return data;
        }
    }

    /*
     * post를 엑셀에서 가져온다.
     * input : id
     * output : [JSONObject] id, content, title author
     * */
    private JSONObject getDatafromExcel(Long id)
    {
        JSONObject data = null;
        try {
            PostOnExcelDto post = postsRepository.findByIdOnExcel(id);
            data = (JSONObject) jsonParser.parse(objectMapper.writeValueAsString(post));
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        } finally {
            return data;
        }
    }

    /*
    * db에 저장된 post의 리스트 찾기
    * input : none
    * output :  [List<PostsResponse4ListDto>] title, content, author, id, modifiedDate
    * */
    @Transactional
    public List<PostsResponse4ListDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsResponse4ListDto::new)
                .collect(Collectors.toList());
    }

    /*
    * mtak api에 post 정보 조회 요청
    * input : [Long] post의 아이디
    * output : [PostsResponseDto] id, title, content, author
    * */
    @Transactional
    public PostsResponseDto getPostInMtakPostAPI(Long id) {
        return restTemplate.getForObject("http://localhost:8080/posts/api?id={id}",
                PostsResponseDto.class, id);
    }

    /*
    * mtak api에 post 정보 저장 요청
    * input : [PostsResponseDto] id, title, content, author
    * output : [ResponseEntity<Long>] post의 아이디
    * */
    @Transactional
    public ResponseEntity<Long> savePostInMtakPostAPI(PostsResponseDto post) {
        return restTemplate.postForEntity("http://localhost:8080/posts/api", post, Long.class);
    }

    /*
    * food api에 음식 정보 조회 요청
    * input : 음식 이름
    * output : [PostSaveRequestDto] title, content, author
    * */
    @Transactional
    public PostsSaveRequestDto getPostInOutsidePostApi(String name) {
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
        PostsSaveRequestDto givenPost = null;
        try {
            givenPost = json2XService.json2PostsSaveRequestDto(givenData.getBody());
        } catch (ParseException e) {
            e.printStackTrace();
            throw new InsufficientException("failed to get food data", 404);
        }
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
            log.info(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        return readlineB.toString();
    }
}
