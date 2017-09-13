package com.jennbowers.library.controllers;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;
import com.jennbowers.library.classes.GoogleBookRequestBuilder;
import com.jennbowers.library.classes.Helpers;
import com.jennbowers.library.interfaces.*;
import com.jennbowers.library.models.Book;
import com.jennbowers.library.models.BookRequest;
import com.jennbowers.library.models.Shelf;
import com.jennbowers.library.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    BookRepository bookRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    ShelfRepository shelfRepo;

    @Autowired
    FriendRequestRepository friendRequestRepo;

    @Autowired
    BookRequestRepository bookRequestRepo;

//    GET request for home page
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index (Model model,
                         Principal principal){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);

        Iterable<Book> books = bookRepo.findAllByUser(user);
        model.addAttribute("books", books);

        List<Book> allBorrowedBooks = new ArrayList<>();
        for(Book book : books) {
            List<BookRequest> ifBorrowed = bookRequestRepo.findAllByBookidAndActive(book, true);
            if(ifBorrowed != null) {
                allBorrowedBooks.add(book);
            }
        }
        model.addAttribute("allBorrowedBooks", allBorrowedBooks);

        Iterable<Shelf> shelves = shelfRepo.findAllByUser(user);
        model.addAttribute("shelves", shelves);

        return "index";
    }

//    POST request for home page when searching for books
    @RequestMapping(value = "/searchToAdd", method = RequestMethod.GET)
    public String searchGet(Model model,
                            Principal principal,
                            @RequestParam("searchText") String searchText,
                            @RequestParam("searchBy") String searchBy,
                            @RequestParam("searchIn") String searchIn) {
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        List<Book> allOwnedBooks = bookRepo.findAllByUser(user);
        List<String> ownedBooksGoogleId = new ArrayList<>();
        for(Book book : allOwnedBooks) {
            String googleId = book.getGoogleId();
            ownedBooksGoogleId.add(googleId);
        }
        model.addAttribute("ownedBooks", ownedBooksGoogleId);
        List<Book> allBorrowedBooks = new ArrayList<>();
        List<Book> allPendingBooks = new ArrayList<>();
        model.addAttribute("searchText", searchText);
        model.addAttribute("searchBy", searchBy);
        Iterable<Book> books = null;
        switch (searchIn) {
//        Search in my books
            case "mine":
                if (searchBy.equals("title")){
                    books = bookRepo.findAllByUserAndTitleIgnoreCase(user, searchText);
                } else if (searchBy.equals("author")) {
                    books = bookRepo.findAllByUserAndAuthorIgnoreCase(user, searchText);
                }
                model.addAttribute("books", books);
                for(Book book : books) {
                    List<BookRequest> ifBorrowed = bookRequestRepo.findAllByBookidAndActive(book, true);
                    if(ifBorrowed != null) {
                        allBorrowedBooks.add(book);
                    }
                    List<BookRequest> ifPending = bookRequestRepo.findAllByBookidAndFromuserAndPending(book, user, true);
                    if(ifPending != null) {
                        allPendingBooks.add(book);
                    }
                }
                model.addAttribute("allBorrowedBooks", allBorrowedBooks);
                model.addAttribute("allPendingBooks", allPendingBooks);

                return "search";
//        Search in friends & others books
            case "borrow":
                if (searchBy.equals("title")){
                    books = bookRepo.findAllByTitleIgnoreCase(searchText);
                } else if (searchBy.equals("author")) {
                    books = bookRepo.findAllByAuthorIgnoreCase(searchText);
                }
                model.addAttribute("books", books);

                for(Book book : books) {
                    List<BookRequest> ifBorrowed = bookRequestRepo.findAllByBookidAndActive(book, true);
                    if(ifBorrowed != null) {
                        allBorrowedBooks.add(book);
                    }

                    List<BookRequest> ifPending = bookRequestRepo.findAllByBookidAndFromuserAndPending(book, user, true);
                    if(ifPending != null) {
                        allPendingBooks.add(book);
                    }
                }
                model.addAttribute("allBorrowedBooks", allBorrowedBooks);
                model.addAttribute("allPendingBooks", allPendingBooks);

                String searchInBorrow = "borrow";
                model.addAttribute("searchInBorrow", searchInBorrow);

                Helpers helpers = new Helpers();
                List<User> notFriends = helpers.findAllNotFriends(user, friendRequestRepo, userRepo);
                model.addAttribute("notFriends", notFriends);

                List<User> pendingFriends = helpers.findAllPendingFriends(user, friendRequestRepo);
                model.addAttribute("pendingFriends", pendingFriends);

                List<User> allFriends = helpers.findAllActiveFriends(user, friendRequestRepo);
                model.addAttribute("activeFriends", allFriends);

                return "search";
//        Search API
            case "add":
                JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//                System.out.println("jsonFactory" + jsonFactory);
                String prefixParam = null;
//                https://stackoverflow.com/questions/5455794/removing-whitespace-from-strings-in-java
                String searchTextModified = searchText.replaceAll("\\s+", "\\+");
                if (searchBy.equals("title")) {
                    prefixParam = "intitle:";
                } else if(searchBy.equals("author")){
                    prefixParam = "inauthor:";
                }

                try {
                    String query = searchTextModified;

                    if (prefixParam != null) {
                        query = prefixParam + query;
                    }
                    try {
                        Volumes volumes = GoogleBookRequestBuilder.queryGoogleBooks(jsonFactory, query);
                        List<Volume> volumesList = volumes.getItems();
//                        System.out.println("Something's working!" + volumesList);
                        model.addAttribute("volumes", volumesList);
                        // Success!
                        return "searchApi";
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }

                return "searchApi";
            default:
                break;
        }

        return "searchApi";
    }

//    GET request for search detail page
    @RequestMapping(value = "/searchDetail/{searchIndex}", method = RequestMethod.GET)
    public String searchDetailGet(Model model,
                        Principal principal,
                        @PathVariable("searchIndex") Integer searchIndex,
                        @RequestParam("searchText") String searchText,
                        @RequestParam("searchBy") String searchBy) {
        String searchIn = "add";
        model.addAttribute(("searchIn"), searchIn);
        model.addAttribute("searchText", searchText);
        model.addAttribute("searchBy", searchBy);
        String username = principal.getName();
        User currentUser = userRepo.findByUsername(username);
        model.addAttribute("currentUser", currentUser);
        List<Book> allOwnedBooks = bookRepo.findAllByUser(currentUser);
        List<String> ownedBooksGoogleId = new ArrayList<>();
        for(Book book : allOwnedBooks) {
            String googleId = book.getGoogleId();
            ownedBooksGoogleId.add(googleId);
        }
        model.addAttribute("ownedBooks", ownedBooksGoogleId);
//        Search API
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        String prefixParam = null;
//        https://stackoverflow.com/questions/5455794/removing-whitespace-from-strings-in-java
        String searchTextModified = searchText.replaceAll("\\s+", "\\+");
        if (searchBy.equals("title")) {
            prefixParam = "intitle:";
        } else if(searchBy.equals("author")){
            prefixParam = "inauthor:";
        }

        try {
            String query = searchTextModified;

            if (prefixParam != null) {
                query = prefixParam + query;
            }
            try {
                Volumes volumes = GoogleBookRequestBuilder.queryGoogleBooks(jsonFactory, query);
                List<Volume> volumesList = volumes.getItems();
//                System.out.println("Something's working!" + volumesList);
                Volume detailVolume = volumesList.get(searchIndex);
//                System.out.println("one volume" + detailVolume);
                model.addAttribute("volume", detailVolume);
                // Success!
                return "searchDetail";
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return "searchApi";
    }

}
