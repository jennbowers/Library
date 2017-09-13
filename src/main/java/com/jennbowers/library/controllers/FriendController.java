package com.jennbowers.library.controllers;

import com.jennbowers.library.classes.Helpers;
import com.jennbowers.library.interfaces.*;
import com.jennbowers.library.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
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

    @Autowired
    BookRequestRepository bookRequestRepo;


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
        List<User> searchActiveFriends = new ArrayList<>();
        List<User> searchPendingFriends = new ArrayList<>();

        Iterable<User> searchUsers = userRepo.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName);

        Helpers helpers = new Helpers();
        List<User> allActiveFriends = helpers.findAllActiveFriends(currentUser, friendRequestRepo);
        List<User> allPendingFriends = helpers.findAllPendingFriends(currentUser, friendRequestRepo);

        for(User searchUser : searchUsers) {
            if(allActiveFriends.contains(searchUser)) {
                searchActiveFriends.add(searchUser);
            }
        }

        model.addAttribute("activeSearchFriends", searchActiveFriends);

        for(User searchUser : searchUsers) {
            if(allPendingFriends.contains(searchUser)) {
                searchPendingFriends.add(searchUser);
            }
        }

        model.addAttribute("pendingSearchFriends", searchPendingFriends);

        List<User> allNotFriends = helpers.findAllNotFriends(currentUser, friendRequestRepo,userRepo);

        for(User searchUser : searchUsers) {
            if(allNotFriends.contains(searchUser)) {
                notFriends.add(searchUser);
            }
        }

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

        if(allActiveFriends.contains(user)) {
            searchActiveFriends.add(user);
        }

        model.addAttribute("activeSearchFriends", searchActiveFriends);

        List<User> allPendingFriends = helpers.findAllPendingFriends(currentUser, friendRequestRepo);

        if(allPendingFriends.contains(user)) {
            searchPendingFriends.add(user);
        }

        model.addAttribute("pendingSearchFriends", searchPendingFriends);

        List<User> allNotFriends = helpers.findAllNotFriends(currentUser, friendRequestRepo, userRepo);

        if(allNotFriends.contains(user)){
            notFriends.add(user);
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
        return "redirect:/friends";
    }

//    POST request to remove a friend
    @RequestMapping(value = "/friends/remove", method = RequestMethod.POST)
    public String friendRemove (Principal principal,
                                @RequestParam("friendId") Long friendId) {
        String username = principal.getName();
        User currentUser = userRepo.findByUsername(username);

//        System.out.println(friendIdString);
//        Long friendId = Long.parseLong(friendIdString);
        User friendUser = userRepo.findOne(friendId);
        List<BookRequest> allRequests = new ArrayList<>();
//        find all book requests between the current user and the friendId
        List<BookRequest> allActiveRequestsFromCurrentUser = bookRequestRepo.findAllByTouserAndFromuserAndActive(currentUser, friendUser, true);
        allRequests.addAll(allActiveRequestsFromCurrentUser);

        List<BookRequest> allActiveRequestsFromFriendUser = bookRequestRepo.findAllByTouserAndFromuserAndActive(friendUser, currentUser,true);
        allRequests.addAll(allActiveRequestsFromFriendUser);

        List<BookRequest> allPendingRequestsFromCurrentUser = bookRequestRepo.findAllByTouserAndFromuserAndPending(currentUser, friendUser, true);
        allRequests.addAll(allPendingRequestsFromCurrentUser);

        List<BookRequest> allPendingRequestsFromFriendUser = bookRequestRepo.findAllByTouserAndFromuserAndPending(friendUser, currentUser, true);
        allRequests.addAll(allPendingRequestsFromFriendUser);

//        set all those book requests to false active and false pending
        for(BookRequest request : allRequests) {
            request.setActive(false);
            request.setPending(false);
            bookRequestRepo.save(request);
        }
//        set friend request to false active and false pending
        List<FriendRequest> allFriendRequests = new ArrayList<>();

        Iterable<FriendRequest> allActiveFriendsToUser = friendRequestRepo.findAllByTouserAndActive(currentUser, true);
        for(FriendRequest request : allActiveFriendsToUser) {
            allFriendRequests.add(request);
        }

        Iterable<FriendRequest> allActiveFriendsFromUser = friendRequestRepo.findAllByFromuserAndActive(currentUser, true);
        for(FriendRequest request : allActiveFriendsFromUser) {
            allFriendRequests.add(request);
        }

        Iterable<FriendRequest> allPendingFriendsToUser = friendRequestRepo.findAllByTouserAndPending(currentUser, true);
        for(FriendRequest request : allPendingFriendsToUser) {
            allFriendRequests.add(request);
        }

        Iterable<FriendRequest> allPendingFriendsFromUser = friendRequestRepo.findAllByFromuserAndPending(currentUser, true);
        for(FriendRequest request : allPendingFriendsFromUser) {
            allFriendRequests.add(request);
        }

        for(FriendRequest request : allFriendRequests) {
            if(request.getFromuser() == friendUser || request.getTouser() == friendUser){
                request.setActive(false);
                request.setPending(false);
                friendRequestRepo.save(request);
            }
        }

        return "redirect:/friends";
    }


//    GET request for friend's home page
    @RequestMapping("/{userId}")
    public String friendHome (Model model,
                              Principal principal,
                              @PathVariable("userId") Long userId) {
        String username = principal.getName();
        User currentUser = userRepo.findByUsername(username);
        User user = userRepo.findOne(userId);
        model.addAttribute("user", user);
        Iterable<Book> books = bookRepo.findAllByUser(user);
        model.addAttribute("books", books);
        List<Book> allBorrowedBooks = new ArrayList<>();
        List<Book> allPendingBooks = new ArrayList<>();
        for(Book book : books) {
            List<BookRequest> ifBorrowed = bookRequestRepo.findAllByBookid(book);
            if(ifBorrowed != null) {
                for(BookRequest borrow : ifBorrowed){
                    if(borrow.isActive()){
                        allBorrowedBooks.add(book);
                    }
                }
            }
            List<BookRequest> ifPending = bookRequestRepo.findAllByBookidAndFromuser(book, currentUser);

            if(ifPending == null) {
                System.out.println("null");
            }


            for(BookRequest ifpendingbook : ifPending) {
                System.out.println(ifpendingbook.getBookid().getTitle());
                System.out.println(ifpendingbook.isPending());
            }

            if(ifPending != null) {
                for(BookRequest pending : ifPending){
                    if(pending.isPending()){
                        allPendingBooks.add(book);
                    }
                }
            }
        }

        for(Book book : allPendingBooks){
            System.out.println(book.getTitle());
        }
        model.addAttribute("allBorrowedBooks", allBorrowedBooks);
        model.addAttribute("allPendingBooks", allPendingBooks);
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
            books = bookRepo.findAllByUserAndTitleIgnoreCase(user, searchQuery);
        } else if (searchType.equals("author")) {
            books = bookRepo.findAllByUserAndAuthorIgnoreCase(user, searchQuery);
        }
        model.addAttribute("user", user);
        model.addAttribute("books", books);

        List<Book> allBorrowedBooks = new ArrayList<>();
        List<Book> allPendingBooks = new ArrayList<>();
        for(Book book : books) {
            List<BookRequest> ifBorrowed = bookRequestRepo.findAllByBookid(book);
            if(ifBorrowed != null) {
                for(BookRequest borrow : ifBorrowed){
                    if(borrow.isActive()){
                        allBorrowedBooks.add(book);
                    }
                }
            }
            List<BookRequest> ifPending = bookRequestRepo.findAllByBookidAndFromuser(book, user);
            if(ifPending != null) {
                for(BookRequest pending : ifPending){
                    if(pending.isPending()){
                        allPendingBooks.add(book);
                    }
                }
            }
        }
        model.addAttribute("allBorrowedBooks", allBorrowedBooks);
        model.addAttribute("allPendingBooks", allPendingBooks);
        return "friendHome";
    }

    //    GET request for all Books page
    @RequestMapping(value = "/{userId}/bookAll", method = RequestMethod.GET)
    public String index (Model model,
                         Principal principal,
                         @PathVariable("userId") Long userId){
        String username = principal.getName();
        User currentUser = userRepo.findByUsername(username);
        model.addAttribute("user", currentUser);

        User user = userRepo.findOne(userId);
//        model.addAttribute("user", user);

        Iterable<Book> books = bookRepo.findAllByUser(user);
        model.addAttribute("books", books);

        List<Book> allBorrowedBooks = new ArrayList<>();
        List<Book> allPendingBooks = new ArrayList<>();
        for(Book book : books) {
            List<BookRequest> ifBorrowed = bookRequestRepo.findAllByBookid(book);
            if(ifBorrowed != null) {
                for(BookRequest borrow : ifBorrowed){
                    if(borrow.isActive()){
                        allBorrowedBooks.add(book);
                    }
                }
            }
            List<BookRequest> ifPending = bookRequestRepo.findAllByBookidAndFromuser(book, user);
            if(ifPending != null) {
                for(BookRequest pending : ifPending){
                    if(pending.isPending()){
                        allPendingBooks.add(book);
                    }
                }
            }
        }
        model.addAttribute("allBorrowedBooks", allBorrowedBooks);
        model.addAttribute("allPendingBooks", allPendingBooks);

        return "book";
    }

//    GET request to see all of friend's shelves
    @RequestMapping("/{userId}/shelf")
    public String friendShelf (Model model,
                               Principal principal,
                               @PathVariable("userId") Long userId) {
        String username = principal.getName();
        User currentUser = userRepo.findByUsername(username);
        model.addAttribute("currentUser", currentUser);
        User shelfOwner = userRepo.findOne(userId);
        model.addAttribute("shelfOwner", shelfOwner);
        Iterable<Shelf> shelves = shelfRepo.findAllByUser(shelfOwner);
        model.addAttribute("shelves", shelves);
        List<Book> allBorrowedBooks = new ArrayList<>();
        List<Book> allPendingBooks = new ArrayList<>();
        for(Shelf shelf : shelves) {
            List<Book> books = shelf.getBooks();
            for(Book book : books) {
                List<BookRequest> ifBorrowed = bookRequestRepo.findAllByBookid(book);
                if(ifBorrowed != null) {
                    for(BookRequest borrow : ifBorrowed){
                        if(borrow.isActive()){
                            allBorrowedBooks.add(book);
                        }
                    }
                }
                List<BookRequest> ifPending = bookRequestRepo.findAllByBookidAndFromuser(book, currentUser);
                if(ifPending != null) {
                    for(BookRequest pending : ifPending){
                        if(pending.isPending()){
                            allPendingBooks.add(book);
                        }
                    }
                }
            }
        }

        for(Book book : allPendingBooks){
            System.out.println(book.getTitle());
        }
        model.addAttribute("allBorrowedBooks", allBorrowedBooks);
        model.addAttribute("allPendingBooks", allPendingBooks);
        return "shelf";
    }

//    GET request to see a specific shelf owned by a friend
//    @RequestMapping("/{userId}/shelf/{shelfId}")
//    public String friendShelfDetail () {
//        return "friendShelfDetail";
//    }

}
