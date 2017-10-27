package com.jennbowers.library.controllers;

import com.jennbowers.library.classes.Helpers;
import com.jennbowers.library.interfaces.BookRepository;
import com.jennbowers.library.interfaces.BookRequestRepository;
import com.jennbowers.library.interfaces.ShelfRepository;
import com.jennbowers.library.interfaces.UserRepository;
import com.jennbowers.library.models.Book;
import com.jennbowers.library.models.BookRequest;
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
import java.util.List;

@Controller
public class ShelfController {
    @Autowired
    BookRepository bookRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    ShelfRepository shelfRepo;

    @Autowired
    BookRequestRepository bookRequestRepo;

//    GET request for seeing all the user's shelves
    @RequestMapping("/shelf")
    public String shelf (Principal principal,
                         Model model) {
        String username = principal.getName();
        User currentUser = userRepo.findByUsername(username);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("shelfOwner", currentUser);
        Iterable<Shelf> shelves = shelfRepo.findAllByUser(currentUser);
        model.addAttribute("shelves", shelves);
        List<Book> allBorrowedBooks = new ArrayList<>();
        Helpers helpers = new Helpers();

        for(Shelf shelf : shelves) {
            List<Book> books = shelf.getBooks();
            for(Book book : books) {
//                System.out.println(shelf.getName() + ": " + book.getTitle());
                helpers.ifBorrowed(book, allBorrowedBooks, bookRequestRepo);
            }
        }

        model.addAttribute("allBorrowedBooks", allBorrowedBooks);
        return "shelf";
    }

    //    POST request for adding a shelf
    @RequestMapping(value = "/shelf", method = RequestMethod.POST)
    public String shelfAdd (Principal principal,
                            @RequestParam("name") String name) {
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        Shelf shelf = new Shelf();
        shelf.setUser(user);
        shelf.setName(name);
        shelfRepo.save(shelf);
        return "redirect:/shelf";
    }
//    GET request for displaying all books on shelf... shelf detail page

    @RequestMapping(value = "/shelf/{shelfId}", method = RequestMethod.GET)
    public String shelfDetail (Model model,
                               Principal principal,
                               @PathVariable("shelfId") Long shelfId) {
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("currentUser", user);
        Shelf shelf = shelfRepo.findOne(shelfId);
        model.addAttribute("shelf", shelf);
        List<Book> allBorrowedBooks = new ArrayList<>();
        Helpers helpers = new Helpers();
        List<Book> books = shelf.getBooks();

        for(Book book : books) {
//                System.out.println(shelf.getName() + ": " + book.getTitle());
            helpers.ifBorrowed(book, allBorrowedBooks, bookRequestRepo);
        }

        model.addAttribute("allBorrowedBooks", allBorrowedBooks);
        return "shelfDetail";
    }

//    POST request for removing a shelf
    @RequestMapping(value = "/shelf/{shelfId}/remove", method = RequestMethod.POST)
    public String shelfRemove (@PathVariable("shelfId") Long shelfId) {
        shelfRepo.delete(shelfId);
        return "redirect:/shelf";
    }

//    GET request for shelf edit page
    @RequestMapping("/shelf/{shelfId}/edit")
    public String editShelf (Principal principal,
                             Model model,
                             @PathVariable("shelfId") Long shelfId) {
        String username = principal.getName();
        User user = userRepo.findByUsername(username);

        Shelf shelf = shelfRepo.findOne(shelfId);
        model.addAttribute("shelf", shelf);

        List<Book> allBooks = bookRepo.findAllByUser(user);
        model.addAttribute("allBooks", allBooks);

        List<Book> booksOnShelf = shelf.getBooks();
        model.addAttribute("booksOnShelf", booksOnShelf);
        return "shelfEdit";
    }

//    POST request for shelf edit page
    @RequestMapping(value = "/shelf/{shelfId}/edit", method = RequestMethod.POST)
    public String editShelfPost (@PathVariable("shelfId") Long shelfId,
                                 @RequestParam("name") String name,
                                 @RequestParam("books") List<Book> books) {
        Shelf shelf = shelfRepo.findOne(shelfId);
        shelf.setName(name);

        for(Book book : books){
            List<Shelf> shelvesThatEachBookIsOn = book.getShelves();
            if(!shelvesThatEachBookIsOn.contains(shelf)) {
                shelvesThatEachBookIsOn.add(shelf);
                book.setShelves(shelvesThatEachBookIsOn);
                bookRepo.save(book);
            }
        }

        List<Book> booksThatAreOnTheShelf = shelf.getBooks();
        for(Book book : booksThatAreOnTheShelf) {
            if(!books.contains(book)){
                List<Shelf> newShelves = book.getShelves();
                newShelves.remove(shelf);
                bookRepo.save(book);
            }
        }

        shelf.setBooks(books);

        shelfRepo.save(shelf);
        return "redirect:/shelf/{shelfId}";
    }

}
