package com.samsolutions.employeesdep.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/hello")
public class HelloController {
    public static Map<String, String> getQueryMap(String query) {
        if (query != null) {
            String[] params = query.split("&");
            Map<String, String> map = new HashMap<>();
            for (String param : params) {
                String name = param.split("=")[0];
                String value = param.split("=")[1];
                map.put(name, value);
            }
            return map;
        } else
            return Map.of("Hello", "Mary");
    }

    @GetMapping
    public String helloGet(Model model, HttpServletRequest request) {
        //model.addAttribute("name","Машуня");
        String queryString = request.getQueryString();
        Map<String, String> map = getQueryMap(queryString);
        model.addAttribute("name", map.get("name"));

        String[] flowers = new String[]{"Rose", "Lily", "Tulip", "Carnation", "Hyacinth"};
        model.addAttribute("flowers", flowers);
        return "hello";
    }
}
