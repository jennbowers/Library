package com.jennbowers.library.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RequestController {

    @RequestMapping(value = "/requests", method = RequestMethod.GET)
    public String request() {
        return "requests";
    }
}
