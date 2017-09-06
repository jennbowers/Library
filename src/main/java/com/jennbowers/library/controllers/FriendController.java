package com.jennbowers.library.controllers;

import com.jennbowers.library.interfaces.BookRepository;
import com.jennbowers.library.interfaces.FriendRequestRepository;
import com.jennbowers.library.interfaces.ShelfRepository;
import com.jennbowers.library.interfaces.UserRepository;
import com.jennbowers.library.models.Book;
import com.jennbowers.library.models.FriendRequest;
import com.jennbowers.library.models.Shelf;
import com.jennbowers.library.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class FriendController {
    @Autowired
    BookRepository bookRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    ShelfRepository shelfRepo;

    @Autowired
    FriendRequestRepository friendRequestRepo;

//    GET request for seeing all friends
    @RequestMapping(value = "/friends", method = RequestMethod.GET)
    public String friendAll (Principal principal,
                             Model model) {
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        List<User> allFriends = new ArrayList<>();
        Iterable<FriendRequest> toFriends = friendRequestRepo.findAllByTouserAndActive(user, true);
        for(FriendRequest friend : toFriends) {
            User otherFriend = friend.getFromuser();
            allFriends.add(otherFriend);
        }
        Iterable<FriendRequest> fromFriends = friendRequestRepo.findAllByFromuserAndActive(user, true);
        for(FriendRequest friend : fromFriends) {
            User otherFriend = friend.getTouser();
            allFriends.add(otherFriend);
        }
        System.out.println(allFriends);
        model.addAttribute("friends", allFriends);
        return "friendAll";
    }

//    POST request for sending a friend request from a user search
    @RequestMapping(value = "/friends", method = RequestMethod.POST)
    public String addFriend (Principal principal,
                             @RequestParam("id") Long id){
        String username = principal.getName();
        User fromUser = userRepo.findByUsername(username);

        User toUser = userRepo.findOne(id);

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setTouser(toUser);
        friendRequest.setFromuser(fromUser);
        friendRequest.setActive(false);
        friendRequest.setPending(true);
        friendRequestRepo.save(friendRequest);
        return "friendAll";
    }

//    POST request for searching for a friend by name
    @RequestMapping(value = "/friends/search/name", method = RequestMethod.POST)
    public String friendSearchName (Model model,
                                    @RequestParam("firstName") String firstName,
                                    @RequestParam("lastName") String lastName) {
        Iterable<User> users = userRepo.findByFirstNameAndLastName(firstName, lastName);
        model.addAttribute("searchUsers", users);
        return "friendAll";
    }

//    POST request for searching for a friend by username
    @RequestMapping(value = "/friends/search/username", method = RequestMethod.POST)
    public String friendSearchUsername (Model model,
                                        @RequestParam("username") String username) {
        User user = userRepo.findByUsername(username);
        model.addAttribute("searchUsers", user);
        return "friendAll";
    }

//    GET request for friend's home page
    @RequestMapping("/{userId}")
    public String friendHome (Model model,
                              @PathVariable("userId") Long userId) {
        User user = userRepo.findOne(userId);
        model.addAttribute("user", user);
        Iterable<Book> books = bookRepo.findAllByUser(user);
        model.addAttribute("books", books);
        Iterable<Shelf> shelves = shelfRepo.findAllByUser(user);
        model.addAttribute("shelves", shelves);
        return "friendHome";
    }

//    POST request for friend's home page when searching for books
    @RequestMapping(value = "/{userId}", method = RequestMethod.POST)
    public String friendHomePost (Model model,
                                  @PathVariable("userId") Long userId,
                                  @RequestParam("searchQuery") String searchQuery,
                                  @RequestParam("searchType") String searchType) {
        User user = userRepo.findOne(userId);
        Iterable<Book> books = null;
        if(searchType.equals("title")) {
            books = bookRepo.findAllByUserAndTitle(user, searchQuery);
        } else if (searchType.equals("author")) {
            books = bookRepo.findAllByUserAndAuthor(user, searchQuery);
        }
        model.addAttribute("user", user);
        model.addAttribute("books", books);
        return "friendHome";
    }

//    GET request to see all of friend's shelves
    @RequestMapping("/{userId}/shelf")
    public String friendShelf (Model model,
                               Principal principal,
                               @PathVariable("userId") Long userId) {
        User shelfOwner = userRepo.findOne(userId);
        model.addAttribute("shelfOwner", shelfOwner);
        Iterable<Shelf> shelves = shelfRepo.findAllByUser(shelfOwner);
        model.addAttribute("shelves", shelves);
        String username = principal.getName();
        User currentUser = userRepo.findByUsername(username);
        model.addAttribute("currentUser", currentUser);
        return "shelf";
    }

//    GET request to see a specific shelf owned by a friend
//    @RequestMapping("/{userId}/shelf/{shelfId}")
//    public String friendShelfDetail () {
//        return "friendShelfDetail";
//    }

}
