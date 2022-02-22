package seoul.bulletin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import seoul.bulletin.service.PostsService;
import seoul.bulletin.web.PostsResponseDto;
import seoul.bulletin.web.PostsSaveRequestDto;
import seoul.bulletin.web.PostsUpdateRequestDto;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostsService postsService;

    @PostMapping("/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto) {
        return postsService.save(requestDto);
    }

    @PutMapping("/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.update(id, requestDto);
    }

    @GetMapping("/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id) {
        return postsService.findById(id);
    }
}
