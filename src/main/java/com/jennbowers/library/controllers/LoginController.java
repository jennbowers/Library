package com.jennbowers.library.controllers;

import com.jennbowers.library.interfaces.BookRepository;
import com.jennbowers.library.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

    @Autowired
    BookRepository bookRepo;

    @Autowired
    UserRepository userRepo;

    //    GET request for login/signup page
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    //    POST request for logging in
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginPost () {
        return "redirect:/";
    }

    //    POST request for signing up
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signupPost () {
        return "redirect:/";
    }
}
