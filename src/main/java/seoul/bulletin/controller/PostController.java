package seoul.bulletin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import seoul.bulletin.dto.PostsResponseDto;
import seoul.bulletin.dto.PostsSaveRequestDto;
import seoul.bulletin.dto.PostsUpdateRequestDto;
import seoul.bulletin.service.PostsService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        model.addAttribute("post", postsService.findByIdOnDB(id));
        return "post_result";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {

        PostsResponseDto dto = postsService.findByIdOnDB(id);
        PostsUpdateRequestDto postsUpdateRequestDto = new PostsUpdateRequestDto();
        postsUpdateRequestDto.setId(dto.getId());
        postsUpdateRequestDto.setContent(dto.getContent());//
        postsUpdateRequestDto.setTitle(dto.getTitle());//
        model.addAttribute("form", postsUpdateRequestDto);
        model.addAttribute("prev", dto);
        return "post_update";
    }

    @PostMapping("/posts/update")
    public String update(@ModelAttribute("form") PostsUpdateRequestDto requestDto) {
        postsService.update(requestDto);
        return "redirect:/";
    }

    @DeleteMapping("/posts/{id}")
    public String delete(@PathVariable Long id) {
        if (postsService.delete(id) == false)
            return null;
        return "redirect:/";
    }

    @GetMapping("/error-ex")
    public void errorEx() {
        throw new RuntimeException("예외 발생!");//ㄴㄴ 화면임.
    }

    @GetMapping("/error-404")
    public void error404(HttpServletResponse response) throws IOException {
        response.sendError(404, "404 오류!");// 페이지
    }
    @GetMapping("/error-500")
    public void error500(HttpServletResponse response) throws IOException, IOException {
        response.sendError(500);
    }
}
