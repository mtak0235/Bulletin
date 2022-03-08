package seoul.bulletin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import seoul.bulletin.dto.PostsResponseDto;
import seoul.bulletin.dto.PostsSaveRequestDto;
import seoul.bulletin.service.PostsService;

@Controller
@RequiredArgsConstructor
public class PostApiController {

    private final PostsService postsService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @ResponseBody
    @GetMapping("/posts/all-type/{id}")
    public PostsResponseDto getPostOnXY(@PathVariable("id") Long id) {
        PostsResponseDto post = postsService.findById(id);
        return post;
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
