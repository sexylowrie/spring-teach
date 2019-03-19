package xin.sunce.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DemoController {

    @ResponseBody
    @RequestMapping("/demo")
    public String demo() {
        return "demo";
    }

    @ResponseBody
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/")
    public ModelAndView welcome(ModelAndView modelAndView) {
        modelAndView.setViewName("index");
        return modelAndView;
    }
}
