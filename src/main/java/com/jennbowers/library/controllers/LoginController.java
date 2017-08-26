package com.jennbowers.library.controllers;

import com.jennbowers.library.interfaces.BookRepository;
import com.jennbowers.library.interfaces.UserRepository;
import com.jennbowers.library.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Autowired
    BookRepository bookRepo;

    @Autowired
    UserRepository userRepo;

    //    GET request for login/signup page
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, HttpServletRequest request) {
        model.addAttribute("user", new User());
        try {
            Object message = request.getSession().getAttribute("error");
            model.addAttribute("error", message);
            request.getSession().removeAttribute("error");
        } catch (Exception ex) {}
        return "login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout () {
        return "redirect:/";
    }

    //    POST request for signing up
//    @RequestMapping(value = "/signup", method = RequestMethod.POST)
//    public String signupPost () {
//        return "redirect:/";
//    }
}
