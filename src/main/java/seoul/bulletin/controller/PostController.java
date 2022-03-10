package seoul.bulletin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import seoul.bulletin.dto.PostsResponseDto;
import seoul.bulletin.dto.PostsSaveRequestDto;
import seoul.bulletin.dto.PostsUpdateRequestDto;
import seoul.bulletin.service.PostsService;

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
    public String postForm(Model model) {
        model.addAttribute("postsSaveRequestDto", new PostsSaveRequestDto());
        return "post_save";
    }

    @PostMapping("/posts")
    public String postSubmit(@ModelAttribute("postsSaveRequestDto") PostsSaveRequestDto postsSaveRequestDto) {
        postsService.save(postsSaveRequestDto);
        return "redirect:/";
    }

    @GetMapping("/posts/{id}")
    public String resultForm(@PathVariable Long id, Model model) {
        model.addAttribute("post", postsService.findById(id));
        return "post_result";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {

        PostsResponseDto dto = postsService.findById(id);
        PostsUpdateRequestDto postsUpdateRequestDto = new PostsUpdateRequestDto();
        postsUpdateRequestDto.setId(dto.getId());
        postsUpdateRequestDto.setContent(dto.getContent());//
        postsUpdateRequestDto.setTitle(dto.getTitle());//
        model.addAttribute("form", postsUpdateRequestDto);
        model.addAttribute("prev", dto);
        return "post_update";
    }

    @PostMapping("/posts/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("form") PostsUpdateRequestDto requestDto) {
        postsService.update(id, requestDto);
        return "redirect:/";
    }

    @DeleteMapping("/posts/{id}")
    public String delete(@PathVariable Long id) {
        if (postsService.delete(id) == false)
            return null;
        return "redirect:/";
    }

}
