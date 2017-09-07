package com.jennbowers.library.controllers;

import com.jennbowers.library.interfaces.BookRequestRepository;
import com.jennbowers.library.interfaces.FriendRequestRepository;
import com.jennbowers.library.interfaces.UserRepository;
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
import java.util.Date;

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
        return "requests";
    }

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

    @RequestMapping(value = "/requests/book/{requestId}", method = RequestMethod.POST)
    public String bookAnswer (@PathVariable("requestId") Long requestId,
                              @RequestParam("answer") String answer,
                              @RequestParam("dueDate") Date dueDate) {
        BookRequest bookRequest = bookRequestRepo.findOne(requestId);
        bookRequest.setPending(false);
        if(answer.equals("Accept")) {
            bookRequest.setActive(true);
            Date borrowedDate = new Date();
            bookRequest.setBorrowed(borrowedDate);
            bookRequest.setDue(dueDate);
        } else if (answer.equals("Deny")) {

        }
        bookRequestRepo.save(bookRequest);
        return "redirect:/requests";
    }
}
