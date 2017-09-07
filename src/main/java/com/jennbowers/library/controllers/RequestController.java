package com.jennbowers.library.controllers;

import com.jennbowers.library.interfaces.FriendRequestRepository;
import com.jennbowers.library.interfaces.UserRepository;
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

    @RequestMapping(value = "/requests/{requestId}", method = RequestMethod.POST)
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
}
