package com.jennbowers.library.controllers;

import com.jennbowers.library.interfaces.BookRepository;
import com.jennbowers.library.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SearchAllController {
    @Autowired
    BookRepository bookRepo;

    @Autowired
    UserRepository userRepo;

//    GET request for search all page
    @RequestMapping("/search")
    public String search () {
        return "search";
    }

//    POST request for search all page
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String searchPost () {
        return "search";
    }
}
