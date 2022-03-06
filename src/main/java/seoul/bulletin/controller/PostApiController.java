package seoul.bulletin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import seoul.bulletin.dto.PostsResponseDto;
import seoul.bulletin.service.PostsService;

@Controller
@RequiredArgsConstructor
public class PostApiController {

    private final PostsService postsService;

    @ResponseBody
    @GetMapping("/posts/all-type/{id}")
    public PostsResponseDto getPostOnXY(@PathVariable("id") Long id) {
        PostsResponseDto post = postsService.findById(id);
        return post;
    }
}
