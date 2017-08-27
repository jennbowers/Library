package com.jennbowers.library.controllers;

import com.jennbowers.library.interfaces.BookRepository;
import com.jennbowers.library.interfaces.RoleRepository;
import com.jennbowers.library.interfaces.UserRepository;
import com.jennbowers.library.models.Role;
import com.jennbowers.library.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Autowired
    BookRepository bookRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    //    GET request for login page
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

//    @RequestMapping(value = "/logout", method = RequestMethod.GET)
//    public String logout () {
//        return "redirect:/";
//    }

//    GET request for signing up
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signup (Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

//    POST request for signing up
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signupPost (@RequestParam("firstName") String firstName,
                              @RequestParam("lastName") String lastName,
                              @RequestParam("email") String email,
                              @RequestParam("username") String username,
                              @RequestParam("password") String password,
                              Model model) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setUsername(username);
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        Role userRole = roleRepo.findByName("ROLE_USER");
        user.setRole(userRole);
        user.setActive(true);
        userRepo.save(user);
        return "redirect:/login";
    }
}
