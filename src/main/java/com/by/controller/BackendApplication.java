package com.by.controller;
 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
@SpringBootApplication
@RestController // 使用@RestController来代替@Controller和@ResponseBody
public class BackendApplication {

    @RequestMapping("/index")
    public String index() {
        return "say hello!";
    }

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    
}