package com.samsolutions.employeesdep.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hello")
public class HelloController {

    @GetMapping
    public String helloGet(Model model) {
        model.addAttribute("name","Машуня");
        return "hello";
    }
}
