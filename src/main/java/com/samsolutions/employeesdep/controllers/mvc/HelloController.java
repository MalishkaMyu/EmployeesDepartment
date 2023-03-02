package com.samsolutions.employeesdep.controllers.mvc;

import com.samsolutions.employeesdep.exception.ErrorResponse;
import com.samsolutions.employeesdep.exception.GlobalErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
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
            return Map.of("name", "Mary");
    }

    @GetMapping(value = "./")
    public String homePage(Principal principal) {
        return "It's default page for all users." +
                ((principal != null) ? " Hello " + principal.getName() : "");
    }

    @GetMapping(value = "/forbidden")
    public ResponseEntity<Object> accessForbidden(Principal principal) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .code(GlobalErrorCode.ERROR_ACCESS_FORBIDDEN)
                        .userLogin(principal.getName())
                        .message("You are not authorised to view this page")
                        .build(), HttpStatus.FORBIDDEN);
    }

    @GetMapping(value = "/hello")
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
