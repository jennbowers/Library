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

//    POST request for friend's home page when searching for books
    @RequestMapping(value = "/{userId}", method = RequestMethod.POST)
    public String friendHomePost () {
        return "friendHome";
    }

//    GET request for seeing all the user's shelves
    @RequestMapping("/shelf")
    public String shelf () {
        return "shelf";
    }

//    GET request for displaying all books on shelf... shelf detail page
    @RequestMapping("/shelf/{shelfId}")
    public String shelfDetail () {
        return "shelfDetail";
    }

//    POST request for searching through books on shelf... on shelf detail page
    @RequestMapping("/shelf/{shelfId}")
    public String shelfPost () {
        return "shelfDetail";
    }

//    POST request for adding a shelf... this form will be at the bottom of the list of all the shelves with just a simple name input in the sidebar and a plus icon to add it
    @RequestMapping(value = "/shelf/add", method = RequestMethod.POST)
    public String shelfAdd () {
//        REDIRECT BACK TO PAGE THAT USER CAME FROM
//        NEED TO LOOK THIS UP
        return "redirect:/";
    }

//    POST request for removing a shelf...this will be on the shelf detail page
    @RequestMapping(value = "/shelf/remove", method = RequestMethod.POST)
    public String shelfRemove () {
        return "redirect:/shelf";
    }

//    GET request for friend's home page
    @RequestMapping("/{userId}")
    public String friendHome () {
        return "friendHome";
    }

//    GET request to see all of friend's shelves
    @RequestMapping("/{userId}/shelf")
    public String friendShelf () {
        return "friendShelf";
    }

//    GET request to see a specific shelf owned by a friend
    @RequestMapping("/{userId}/shelf/{shelfId}")
    public String friendShelfDetail () {
        return "friendShelfDetail";
    }
}
