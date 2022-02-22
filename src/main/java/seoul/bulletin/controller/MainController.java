package seoul.bulletin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import seoul.bulletin.service.PostsService;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final PostsService postsService;

    @GetMapping("/")
    public String welcome(Model model) {
        model.addAttribute("posts", postsService.findAllDesc());
        return "main";
    }
    @GetMapping("/posts/save")
    public String save(Model model) {
        return "post_save";
    }

    @GetMapping("hello-string")
    @ResponseBody
    public String helloString(@RequestParam("name") String name) {
        return "hello " + name;
    }

    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }
    static class Hello {
        private String name;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }

}
