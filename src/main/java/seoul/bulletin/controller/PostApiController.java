package seoul.bulletin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import seoul.bulletin.dto.PostsResponseDto;
import seoul.bulletin.dto.PostsSaveRequestDto;
import seoul.bulletin.service.PostsService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostApiController {

    private final PostsService postsService;
    private ObjectMapper objectMapper = new ObjectMapper();

    /*
    * 데이터 가져오기(외부 api 입장)
    * */
    @ResponseBody
    @GetMapping(value = "/posts/api")
    public PostsResponseDto getFormApiJson(@RequestParam("id") Long id) {
        PostsResponseDto post = postsService.findById(id);
        return post;
    }

    /*
     *데이터 저장하기 (외부 api 입장)
     * */
    @ResponseBody
    @PostMapping(value = "/posts/api", consumes = "application/json")
    public ResponseEntity<Long> postSaveApi(@RequestBody String posts) throws JsonProcessingException {//졸라 다 섞인 postResponseDto
        //post 1개인지 여러개인지 확인
        //잘 파싱
        //개수만큼 차래대로 save
        PostsResponseDto givenPost = objectMapper.readValue(posts, PostsResponseDto.class);
        PostsSaveRequestDto post2Save = PostsSaveRequestDto.builder()
                .title(givenPost.getTitle())
                .content(givenPost.getContent())
                .author(givenPost.getAuthor())
                .build();
        Long savedId = postsService.save(post2Save);
        return new ResponseEntity<>(savedId, HttpStatus.OK);
    }

    /*
     * mtak이 만든 데이터 가져오기 + 저장하기 api 저장하기 2번 호출하는 컨트롤러
     * */
    @ResponseBody
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
    @ResponseBody
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
    @ResponseBody
    @GetMapping("/posts/outside-api/{target}")
    public Map<String, String> saveFormsFromOutsideApi(@PathVariable("target") String name) throws IOException, ParseException {
        PostsSaveRequestDto givenPost = postsService.getPostInOutsidePostApi(name);
        Long savedId = postsService.save(givenPost);
        Map<String, String> ret = new HashMap<>();
        ret.put("id", savedId.toString());
        return ret;
    }

    /*
    json return
    */
    @GetMapping("/posts/api/{id}")
    public String postData(@PathVariable Long id) throws JsonProcessingException {
        PostsResponseDto post = postsService.findById(id);
        return saveData(objectMapper.writeValueAsString(post));
    }

    /*
     * json get redirect tp main page
     * */
    @ResponseBody
    @PostMapping("/posts/api/save")
    public String saveData(@RequestBody String msg) throws JsonProcessingException {
        System.out.println("msg = " + msg);
        PostsResponseDto req = objectMapper.readValue(msg, PostsResponseDto.class);
        PostsSaveRequestDto post2Save = new PostsSaveRequestDto();
        post2Save.setTitle(req.getTitle());
        post2Save.setContent(req.getContent());
        post2Save.setAuthor(req.getAuthor());
        postsService.save(post2Save);
        return "redirect:/";
    }

    /*
     * 파일을 읽어와서 saveData 호출하면 됨/
     * */

    @GetMapping("/posts/file/{file}")
    public String postFile(@PathVariable String file) throws JsonProcessingException {
        String post = postsService.getStringFromFile(file);
        return saveData(post);
    }
}
