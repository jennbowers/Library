package com.jennbowers.library.controllers;

import com.jennbowers.library.interfaces.BookRequestRepository;
import com.jennbowers.library.interfaces.FriendRequestRepository;
import com.jennbowers.library.interfaces.UserRepository;
import com.jennbowers.library.models.Book;
import com.jennbowers.library.models.BookRequest;
import com.jennbowers.library.models.FriendRequest;
import com.jennbowers.library.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class RequestController {
    @Autowired
    UserRepository userRepo;

    @Autowired
    FriendRequestRepository friendRequestRepo;

    @Autowired
    BookRequestRepository bookRequestRepo;

    @RequestMapping(value = "/requests", method = RequestMethod.GET)
    public String request(Principal principal,
                          Model model) {
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        Iterable<FriendRequest> friendRequests = friendRequestRepo.findAllByTouserAndPending(user, true);
        model.addAttribute("friendRequests", friendRequests);

        Iterable<BookRequest> bookRequests = bookRequestRepo.findAllByTouserAndPending(user, true);
        model.addAttribute("bookRequests", bookRequests);

        Iterable<BookRequest> pendingBooksFromMe = bookRequestRepo.findAllByFromuserAndPending(user, true);
        model.addAttribute("pendingBooksFromMe", pendingBooksFromMe);

        Iterable<BookRequest> booksIBorrowed = bookRequestRepo.findAllByFromuserAndActive(user, true);
        model.addAttribute("booksIBorrowed", booksIBorrowed);

        Iterable<BookRequest> myBooksThatAreBorrowed = bookRequestRepo.findAllByTouserAndActive(user, true);
        model.addAttribute("myBooksThatAreBorrowed", myBooksThatAreBorrowed);
        return "requests";
    }

//    POST request to respond to friend request
    @RequestMapping(value = "/requests/friend/{requestId}", method = RequestMethod.POST)
    public String friendAnswer (@PathVariable("requestId") Long requestId,
                                @RequestParam("answer") String answer) {
        FriendRequest friendRequest = friendRequestRepo.findOne(requestId);
        friendRequest.setPending(false);
        if(answer.equals("Accept")) {
            friendRequest.setActive(true);
        } else if (answer.equals("Deny")) {

        }
        friendRequestRepo.save(friendRequest);
        return "redirect:/requests";
    }

//    POST request to respond to book request—if accept
    @RequestMapping(value = "/requests/book/accept/{requestId}", method = RequestMethod.POST)
    public String bookAnswer (Model model,
                              @PathVariable("requestId") Long requestId,
                              @RequestParam("dueDate") java.sql.Date dueDate) {
        BookRequest bookRequest = bookRequestRepo.findOne(requestId);
        Book book = bookRequest.getBookid();
        BookRequest activeBorrow = bookRequestRepo.findAllByBookidAndActive(book, true);
        if (activeBorrow == null) {
            bookRequest.setPending(false);
            bookRequest.setActive(true);
            Date borrowedDate = new Date();
            bookRequest.setBorrowed(borrowedDate);
            bookRequest.setDue(dueDate);
        }
        bookRequestRepo.save(bookRequest);
        return "redirect:/requests";
    }

//    POST request to respond to book request—if deny
    @RequestMapping(value = "/requests/book/deny/{requestId}", method = RequestMethod.POST)
    public String bookAnswer (Model model,
                              @PathVariable("requestId") Long requestId) {
        BookRequest bookRequest = bookRequestRepo.findOne(requestId);
        bookRequest.setPending(false);
        bookRequest.setActive(false);
        bookRequestRepo.save(bookRequest);
        return "redirect:/requests";
    }
}
