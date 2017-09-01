package com.jennbowers.library.controllers;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;
import com.jennbowers.library.classes.GoogleBookRequestBuilder;
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

import java.io.IOException;
import java.security.Principal;
import java.util.List;

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
    @RequestMapping(value = "/searchToAdd", method = RequestMethod.GET)
    public String indexPost(Model model,
                            Principal principal,
                            @RequestParam("searchText") String searchText,
                            @RequestParam("searchBy") String searchBy,
                            @RequestParam("searchIn") String searchIn) {
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
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
                JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

                String prefixParam = null;
//                https://stackoverflow.com/questions/5455794/removing-whitespace-from-strings-in-java
                String searchTextModified = searchText.replaceAll("\\s+", "\\+");
                if (searchBy.equals("title")) {
                    prefixParam = "intitle:";
                } else if(searchBy.equals("author")){
                    prefixParam = "inauthor:";
                }

                try {
//                Query format: "[<inauthor|intitle>:]<query>"
                    String query = searchTextModified;

                    if (prefixParam != null) {
                        query = prefixParam + query;
                    }

                    try {
                        Volumes volumes = GoogleBookRequestBuilder.queryGoogleBooks(jsonFactory, query);
                        List<Volume> volumesList = volumes.getItems();
                        model.addAttribute("volumes", volumesList);
                        // Success!
                        return "search";
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                break;
            default:
                break;
        }

        return "searchApi";
    }

}
