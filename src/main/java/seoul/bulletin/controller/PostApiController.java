package seoul.bulletin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import seoul.bulletin.dto.PostsResponseDto;
import seoul.bulletin.dto.PostsSaveRequestDto;
import seoul.bulletin.exception.InsufficientException;
import seoul.bulletin.service.PostsService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostApiController {

    private final PostsService postsService;
    private ObjectMapper objectMapper = new ObjectMapper();
    private JSONParser jsonParser = new JSONParser();

    @GetMapping("/posts/all-type/{id}")
    public String getAllTypePostData(@PathVariable Long id) {
        return postsService.findByIdOnDBNFileNExcel(id);
    }
    /*
    * 데이터 가져오기(외부 api 입장)
    * input : id
    * output: json( id, title, author, content), 1row
    * */
    @GetMapping(value = "/posts/api")
    public PostsResponseDto getFormApiJson(@RequestParam("id") Long id) {
        PostsResponseDto post = null;
        try {
            post = postsService.findByIdOnDB(id);

        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
            throw new IllegalArgumentException("invalid id");
        }
        return post;
    }

    /*
     *데이터 저장하기 (외부 api 입장)
     * */
    @PostMapping(value = "/posts/api", consumes = "application/json")
    public ResponseEntity<Long> postSaveApi(@RequestBody String posts) throws ParseException {
        JSONObject post = (JSONObject) jsonParser.parse(posts);
        PostsSaveRequestDto post2Save = PostsSaveRequestDto.builder()
                .title((String) post.get("title"))
                .content((String) post.get("content"))
                .author((String) post.get("author"))
                .build();
        Long savedId = postsService.save(post2Save);
        return new ResponseEntity<>(savedId, HttpStatus.OK);
    }

    /*
     * mtak이 만든 데이터 가져오기 + 저장하기 api 저장하기 2번 호출하는 컨트롤러
     * */
    @GetMapping("/posts/mtak-api-twice/{id}")
    public Map<String, String> saveFormFromMtakApiCallTwice(@PathVariable Long id) {
        PostsResponseDto post = postsService.getPostInMtakPostAPI(id);
        ResponseEntity<Long> savedPostId = postsService.savePostInMtakPostAPI(post);
        Map<String, String> ret = new HashMap<>();
        ret.put("id", savedPostId.getBody().toString());
        return ret;
    }

    /*
     * mtak이 만든 데이터 가져오기 + 저장하기 api 저장하기 2번 호출하는 컨트롤러
     * */
    @GetMapping("/posts/mtak-api-once/{id}")
    public Map<String, String> saveFormFromMtakApiCallOnce(@PathVariable Long id) {
        PostsResponseDto givenPost = postsService.getPostInMtakPostAPI(id);
        PostsSaveRequestDto post2Save = PostsSaveRequestDto.builder()
                .title(givenPost.getTitle())
                .content(givenPost.getContent())
                .author(givenPost.getAuthor())
                .build();
        Long savedId = postsService.save(post2Save);
        Map<String, String> ret = new HashMap<>();
        ret.put("id",savedId.toString());
        return ret;
    }

    /*
     * 외부 api 호출해서 저장하기
     * */
    @GetMapping("/posts/outside-api/{target}")
    public Map<String, String> saveFormsFromOutsideApi(@PathVariable("target") String name) throws InsufficientException{
        PostsSaveRequestDto givenPost = postsService.getPostInOutsidePostApi(name);
        Long savedId = postsService.save(givenPost);
        Map<String, String> ret = new HashMap<>();
        ret.put("id", savedId.toString());
        return ret;
    }


    @GetMapping("/api/response-status-ex1")
    public String responseStatusEx1() {
        throw new InsufficientException("error");
    }

    @GetMapping("/api/response-status-ex2")
    public String responseStatusEx2() {
//        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
        throw new IllegalArgumentException("here");
    }

}
