package seoul.bulletin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import seoul.bulletin.service.PostsService;
import seoul.bulletin.dto.PostsResponseDto;
import seoul.bulletin.dto.PostsSaveRequestDto;
import seoul.bulletin.dto.PostsUpdateRequestDto;

@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("posts", postsService.findAllDesc());
        return "main";
    }

    @GetMapping("/posts/save")
    public String savePage(Model model) {
        return "post_save";
    }

    @PostMapping("/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto) {
        return postsService.save(requestDto);
    }

    @PutMapping("/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.update(id, requestDto);
    }

    @GetMapping("/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id, Model model) {
        return postsService.findById(id);
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);
        return "post_update";
    }

    @DeleteMapping("/posts/{id}")
    public Long delete(@PathVariable Long id) {
        if (postsService.delete(id) == false)
            return null;
        return id;
    }
}
