package com.jennbowers.library.controllers;

import com.jennbowers.library.classes.Helpers;
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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

        Helpers helpers = new Helpers();
        List<User> allFriends = helpers.findAllActiveFriends(user, friendRequestRepo);

        model.addAttribute("friends", allFriends);
        return "friendAll";
    }

//    POST request for searching for a friend by name
    @RequestMapping(value = "/friends/search/name", method = RequestMethod.POST)
    public String friendSearchName (Principal principal,
                                    Model model,
                                    @RequestParam("firstName") String firstName,
                                    @RequestParam("lastName") String lastName) {
        String username = principal.getName();
        User currentUser = userRepo.findByUsername(username);

        List<User> notFriends = new ArrayList<>();
        List<User> notActiveFriends = new ArrayList<>();
        List<User> searchActiveFriends = new ArrayList<>();
        List<User> searchPendingFriends = new ArrayList<>();

        Iterable<User> searchUsers = userRepo.findByFirstNameAndLastName(firstName, lastName);

        Helpers helpers = new Helpers();
        List<User> allActiveFriends = helpers.findAllActiveFriends(currentUser, friendRequestRepo);
        List<User> allPendingFriends = helpers.findAllPendingFriends(currentUser, friendRequestRepo);

        for(User activeFriend : allActiveFriends) {
            for(User searchUser : searchUsers) {
                if(activeFriend == searchUser) {
                    searchActiveFriends.add(searchUser);
                }
            }
        }

        model.addAttribute("activeSearchFriends", searchActiveFriends);

        for(User pendingFriend : allPendingFriends) {
            for(User searchUser : searchUsers) {
                if(pendingFriend == searchUser) {
                    searchPendingFriends.add(searchUser);
                }
            }
        }

        model.addAttribute("pendingSearchFriends", searchPendingFriends);

        List<User> allNotFriends = helpers.findAllNotFriends(currentUser, friendRequestRepo,userRepo);
        for(User notFriend: allNotFriends) {
            for(User searchUser : searchUsers) {
                if(notFriend == searchUser) {
                    notFriends.add(searchUser);
                    System.out.println(searchUser.getUsername());
                }
            }
        }

//        for(User searchUser : searchUsers) {
//            if(allActiveFriends.contains(searchUser)) {
//                searchActiveFriends.add(searchUser);
//            }
//        }

        model.addAttribute("notFriends", notFriends);

        return "friendAll";
    }


//    POST request for searching for a friend by username

    @RequestMapping(value = "/friends/search/username", method = RequestMethod.POST)
    public String friendSearchUsername (Principal principal,
                                        Model model,
                                        @RequestParam("username") String username) {
        String currentUsername = principal.getName();
        User currentUser = userRepo.findByUsername(currentUsername);

        List<User> notFriends = new ArrayList<>();
        List<User> searchActiveFriends = new ArrayList<>();
        List<User> searchPendingFriends = new ArrayList<>();

        User user = userRepo.findByUsername(username);

        Helpers helpers = new Helpers();
        List<User> allActiveFriends = helpers.findAllActiveFriends(currentUser, friendRequestRepo);

        for(User activeFriend : allActiveFriends) {
            if(activeFriend == user) {
                searchActiveFriends.add(user);
            }
        }

        model.addAttribute("activeSearchFriends", searchActiveFriends);

        List<User> allPendingFriends = helpers.findAllPendingFriends(currentUser, friendRequestRepo);
        for(User pendingFriend : allPendingFriends) {
            if(pendingFriend == user) {
                searchPendingFriends.add(user);
            }
        }

        model.addAttribute("pendingSearchFriends", searchPendingFriends);

        List<User> allNotFriends = helpers.findAllNotFriends(currentUser, friendRequestRepo, userRepo);
        for(User friend: allNotFriends) {
            if(friend == user){
                notFriends.add(user);
            }
        }

        model.addAttribute("notFriends", notFriends);

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
