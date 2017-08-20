package com.jennbowers.library.controllers;

import com.jennbowers.library.interfaces.BookRepository;
import com.jennbowers.library.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FriendController {
    @Autowired
    BookRepository bookRepo;

    @Autowired
    UserRepository userRepo;

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

//    GET request to see the details of a specific book owned by a friend
    @RequestMapping("/{userId}/book/{bookId}")
    public String friendBookDetail () {
        return "friendBookDetail";
    }
}
