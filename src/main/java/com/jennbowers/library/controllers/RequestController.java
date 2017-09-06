package com.jennbowers.library.controllers;

import com.jennbowers.library.interfaces.FriendRequestRepository;
import com.jennbowers.library.interfaces.UserRepository;
import com.jennbowers.library.models.FriendRequest;
import com.jennbowers.library.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class RequestController {
    @Autowired
    UserRepository userRepo;

    @Autowired
    FriendRequestRepository friendRequestRepo;

    @RequestMapping(value = "/requests", method = RequestMethod.GET)
    public String request(Principal principal,
                          Model model) {
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        Iterable<FriendRequest> friendRequests = friendRequestRepo.findAllByTouserAndPending(user, true);
        model.addAttribute("friendRequests", friendRequests);
        return "requests";
    }
}
