package com.azurealstn.alog.controller.api.hello;

import com.azurealstn.alog.dto.hello.HelloDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/helloDto")
    public HelloDto helloDto(@ModelAttribute HelloDto helloDto) {
        return new HelloDto(helloDto.getName(), helloDto.getAge());
    }
}
