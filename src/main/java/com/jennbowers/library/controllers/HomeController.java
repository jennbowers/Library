package com.jennbowers.library.controllers;

import com.jennbowers.library.interfaces.BookRepository;
import com.jennbowers.library.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
    @Autowired
    BookRepository bookRepo;

    @Autowired
    UserRepository userRepo;

//    GET request for home page
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index (){
        return "index";
    }

//    POST request for home page when searching for books
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String indexPost() {
        return "index";
    }

}
