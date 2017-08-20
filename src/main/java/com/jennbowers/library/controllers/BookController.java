package com.jennbowers.library.controllers;

import com.jennbowers.library.interfaces.BookRepository;
import com.jennbowers.library.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BookController {
    @Autowired
    BookRepository bookRepo;

    @Autowired
    UserRepository userRepo;

//    GET request for user's book detail page
    @RequestMapping("/book/{bookId}")
    public String bookDetail () {
        return "bookDetail";
    }

//    POST request for adding a book
    @RequestMapping(value = "/book/add", method = RequestMethod.POST)
    public String addBook () {
        return "redirect:/";
    }

//    GET request for book edit page
    @RequestMapping("/book/{bookId}/edit")
    public String editBook () {
        return "editBook";
    }

//    POST request for book edit page
    @RequestMapping(value = "/book/{boookId}/edit", method = RequestMethod.POST)
    public String editBookPost () {
        return "redirect:/book/{bookId}";
    }

//    POST request for removing a book
    @RequestMapping(value = "/book/{bookId}/remove", method = RequestMethod.POST)
    public String removeBookPost () {
        return "redirect:/";
    }
}
