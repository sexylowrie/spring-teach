package xin.sunce.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import xin.sunce.spring.namespace.Config;

import javax.annotation.Resource;

//@Controller
public class DemoController {

    @Resource
    private Config programmer;

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

    @ResponseBody
    @RequestMapping("/user")
    public String user() {
        return programmer.toString();
    }

    @RequestMapping("/")
    public ModelAndView welcome(ModelAndView modelAndView) {
        modelAndView.setViewName("index");
        return modelAndView;
    }
}
