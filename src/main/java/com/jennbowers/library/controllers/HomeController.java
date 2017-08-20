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
    @RequestMapping(value = "/shelf/{shelfId}/remove", method = RequestMethod.POST)
    public String shelfRemove () {
        return "redirect:/shelf";
    }

//    GET request for shelf edit page
    @RequestMapping("/shelf/{shelfId}/edit")
    public String editShelf () {
        return "editShelf";
    }

//    POST request for shelf edit page
    @RequestMapping(value = "/shelf/{shelfId}/edit", method = RequestMethod.POST)
    public String editShelfPost () {
        return "redirect:/shelf/{shelfId}";
    }

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

//    GET request for search all page
    @RequestMapping("/search")
    public String search () {
        return "search";
    }

//    POST request for search all page
    @RequestMapping("/search")
    public String seachPost () {
        return "search";
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

//    GET request to see the details of a specific book owned by a friend
    @RequestMapping("/{userId}/book/{bookId}")
    public String friendBookDetail () {
        return "friendBookDetail";
    }
}
