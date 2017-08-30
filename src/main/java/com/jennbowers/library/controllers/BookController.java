package com.jennbowers.library.controllers;

import com.jennbowers.library.interfaces.BookRepository;
import com.jennbowers.library.interfaces.UserRepository;
import com.jennbowers.library.models.Book;
import com.jennbowers.library.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class BookController {
    @Autowired
    BookRepository bookRepo;

    @Autowired
    UserRepository userRepo;

//    GET request for user's book detail page
    @RequestMapping("/book/{bookId}")
    public String bookDetail (Model model,
                              @PathVariable("bookId") Long bookId,
                              Principal principal) {
        String currentUser = principal.getName();
        User user = userRepo.findByUsername(currentUser);
        model.addAttribute("currentUser", user);
        Book book = bookRepo.findOne(bookId);
        model.addAttribute("book", book);
        return "bookDetail";
    }

//    POST request for adding a book
    @RequestMapping(value = "/book/add", method = RequestMethod.POST)
    public String addBook () {
        return "redirect:/";
    }

//    GET request for book edit page
    @RequestMapping("/book/{bookId}/edit")
    public String editBook (Model model,
                            @PathVariable("bookId") Long bookId,
                            Principal principal) {
        String currentUser = principal.getName();
        User user = userRepo.findByUsername(currentUser);
        model.addAttribute("currentUser", user);

        Book book = bookRepo.findOne(bookId);
        User bookOwner = book.getUser();
        String bookOwnerString = bookOwner.getUsername();
        model.addAttribute("book", book);

        if(currentUser.equals(bookOwnerString)) {
            return "editBook";
        } else {
            return "bookDetail";
        }
    }

//    POST request for book edit page
    @RequestMapping(value = "/book/{bookId}/edit", method = RequestMethod.POST)
    public String editBookPost (@PathVariable("bookId") Long bookId,
                                @RequestParam("rating") Integer rating,
                                @RequestParam("copies") Integer copies) {
        Book book = bookRepo.findOne(bookId);
        book.setCopies(copies);
        if(rating != null) {
            book.setRating(rating);
        } else {
            book.setRating(0);
        }
        bookRepo.save(book);
        return "redirect:/book/{bookId}";
    }

//    POST request for removing a book
    @RequestMapping(value = "/book/{bookId}/remove", method = RequestMethod.POST)
    public String removeBookPost (@PathVariable("bookId") Long bookId) {
        bookRepo.delete(bookId);
        return "redirect:/";
    }
}
