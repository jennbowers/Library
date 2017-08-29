package com.jennbowers.library.controllers;

import com.jennbowers.library.interfaces.BookRepository;
import com.jennbowers.library.interfaces.ShelfRepository;
import com.jennbowers.library.interfaces.UserRepository;
import com.jennbowers.library.models.Book;
import com.jennbowers.library.models.Shelf;
import com.jennbowers.library.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class FriendController {
    @Autowired
    BookRepository bookRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    ShelfRepository shelfRepo;

//    GET request for seeing all friends
    @RequestMapping(value = "/friends", method = RequestMethod.GET)
    public String friendAll (Model model) {
        Iterable<User> users = userRepo.findAll();
        model.addAttribute("users", users);
        return "friendAll";
    }

//    POST request for searching for a friend by name
    @RequestMapping(value = "/friends/search/name", method = RequestMethod.POST)
    public String friendSearchName (Model model,
                                    @RequestParam("firstName") String firstName,
                                    @RequestParam("lastName") String lastName) {
        Iterable<User> users = userRepo.findByFirstNameAndLastName(firstName, lastName);
        model.addAttribute("users", users);
        return "friendAll";
    }

//    POST request for searching for a friend by username
    @RequestMapping(value = "/friends/search/username", method = RequestMethod.POST)
    public String friendSearchUsername (Model model,
                                        @RequestParam("username") String username) {
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        return "friendAll";
    }

//    GET request for friend's home page
    @RequestMapping("/{userId}")
    public String friendHome (Model model,
                              @PathVariable("userId") Long userId) {
        User user = userRepo.findOne(userId);
        model.addAttribute("user", user);
        Iterable<Book> books = bookRepo.findAllByUser(user);
        model.addAttribute("books", books);
        Iterable<Shelf> shelves = shelfRepo.findAllByUser(user);
        model.addAttribute("shelves", shelves);
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
