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
    @RequestMapping("/")
    public String index (){
        return "index";
    }

//    POST request for home page when searching for books
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String indexPost() {
        return "index";
    }

//    GET request for friend's home page
    @RequestMapping("/{userId}")
    public String friendHome () {
        return "friendHome";
    }

//    POST request for friend's home page when searching for books
    @RequestMapping(value = "/{userId}", method = RequestMethod.POST)
    public String friendHomePost () {
        return "friendHome";
    }

//    GET request for displaying all books on shelf
    @RequestMapping("/shelf/{shelfId}")
    public String shelf () {
        return "shelf";
    }

//    POST request for searching through books on shelf
    @RequestMapping("/shelf/{shelfId}")
    public String shelfPost () {
        return "shelf";
    }
}
