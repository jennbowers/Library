package com.jennbowers.library.controllers;

import com.jennbowers.library.classes.GoogleBookTest;
import com.jennbowers.library.interfaces.BookRepository;
import com.jennbowers.library.interfaces.ShelfRepository;
import com.jennbowers.library.interfaces.UserRepository;
import com.jennbowers.library.models.Book;
import com.jennbowers.library.models.Shelf;
import com.jennbowers.library.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class HomeController {
    @Autowired
    BookRepository bookRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    ShelfRepository shelfRepo;

//    GET request for home page
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index (Model model,
                         Principal principal){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        Iterable<Book> books = bookRepo.findAllByUser(user);
        model.addAttribute("books", books);
        Iterable<Shelf> shelves = shelfRepo.findAllByUser(user);
        model.addAttribute("shelves", shelves);
        return "index";
    }

//    POST request for home page when searching for books
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String indexPost(Model model,
                            @RequestParam("userId") Long userId,
                            @RequestParam("searchText") String searchText,
                            @RequestParam("searchBy") String searchBy,
                            @RequestParam("searchIn") String searchIn) {
        User user = userRepo.findOne(userId);
        model.addAttribute("user", user);
        Iterable<Book> books = null;
        switch (searchIn) {
//        Search in my books
            case "mine":
                if (searchBy.equals("title")){
                    books = bookRepo.findAllByUserAndTitle(user, searchText);
                } else if (searchBy.equals("author")) {
                    books = bookRepo.findAllByUserAndAuthor(user, searchText);
                }
                model.addAttribute("books", books);
                break;
//        Search in friends & others books
            case "borrow":
                if (searchBy.equals("title")){
                    books = bookRepo.findAllByTitle(searchText);
                } else if (searchBy.equals("author")) {
                    books = bookRepo.findAllByAuthor(searchText);
                }
                model.addAttribute("books", books);
                break;
//        Search API
            case "add":

                break;
            default:
                break;
        }

        return "search";
    }

}
