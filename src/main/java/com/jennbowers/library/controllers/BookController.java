package com.jennbowers.library.controllers;

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
public class BookController {
    @Autowired
    BookRepository bookRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    ShelfRepository shelfRepo;

    @Autowired
    BookRequestRepository bookRequestRepo;

    //    GET request for all Books page
    @RequestMapping(value = "/bookAll", method = RequestMethod.GET)
    public String index (Model model,
                         Principal principal){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);

        Iterable<Book> books = bookRepo.findAllByUser(user);
        model.addAttribute("books", books);

        List<Book> allBorrowedBooks = new ArrayList<>();
        for(Book book : books) {
            List<BookRequest> ifBorrowed = bookRequestRepo.findAllByBookidAndActive(book, true);
            if(ifBorrowed != null) {
                allBorrowedBooks.add(book);
            }
        }
        model.addAttribute("allBorrowedBooks", allBorrowedBooks);

        return "book";
    }

//    GET request for user's book detail page
    @RequestMapping("/book/{bookId}")
    public String bookDetail (Model model,
                              @PathVariable("bookId") Long bookId,
                              Principal principal) {
        String currentUser = principal.getName();
        User user = userRepo.findByUsername(currentUser);
        model.addAttribute("currentUser", user);
        Book book = bookRepo.findOne(bookId);
        model.addAttribute("book", book);
        List<Shelf> shelves = book.getShelves();
        model.addAttribute("shelves", shelves);
        List<BookRequest> ifBorrowed = bookRequestRepo.findAllByBookidAndActive(book, true);
        model.addAttribute("ifBorrowed", ifBorrowed);
        List<BookRequest> ifPending = bookRequestRepo.findAllByBookidAndFromuserAndPending(book, user, true);
        model.addAttribute("ifPending", ifPending);
        return "bookDetail";
    }

//    POST request for adding a book
    @RequestMapping(value = "/bookAdd", method = RequestMethod.POST)
    public String addBook (Model model,
                           Principal principal,
                           @RequestParam("title") String title,
                           @RequestParam("authors")ArrayList<String> authors,
                           @RequestParam("img") String img,
                           @RequestParam("summary") String summary,
                           @RequestParam("publishedDate") String publishedDate,
                           @RequestParam("googleId") String googleId) {
        String username = principal.getName();
        User user = userRepo.findByUsername(username);

        Shelf unshelved = shelfRepo.findByUserAndNameIgnoreCase(user,"Unshelved");
        List<Shelf> starter = new ArrayList<>();
        starter.add(unshelved);

        Book book = new Book();
        book.setUser(user);
        book.setTitle(title);
        String allAuthors;
        if(authors.size() > 1) {
            allAuthors = String.join(", ", authors);
//            System.out.println(allAuthors);
        } else {
            allAuthors = authors.toString();
//            System.out.println(allAuthors);
        }
        allAuthors = allAuthors.replace("[", "").replace("]", "");
        book.setAuthor(allAuthors);
        book.setImg(img);
        book.setSummary(summary);
        book.setDatePublished(publishedDate);
        book.setGoogleId(googleId);
        book.setCopies(1);
        book.setActive(true);
        book.setShelves(starter);
        bookRepo.save(book);

        return "redirect:/";
    }

//    POST request for requesting to borrow a book
    @RequestMapping("/book/{bookId}/borrow")
    public String borrowBook (Principal principal,
                              @PathVariable("bookId") long bookId) {
        BookRequest bookRequest = new BookRequest();

        String username = principal.getName();
        User fromUser = userRepo.findByUsername(username);
        bookRequest.setFromuser(fromUser);

        Book book = bookRepo.findOne(bookId);
        bookRequest.setBookid(book);

        User toUser = book.getUser();
        bookRequest.setTouser(toUser);

        bookRequest.setActive(false);
        bookRequest.setPending(true);

        bookRequestRepo.save(bookRequest);
        return "redirect:/book/" + bookId;
    }

//    GET request for book edit page
    @RequestMapping("/book/{bookId}/edit")
    public String editBook (Model model,
                            @PathVariable("bookId") Long bookId,
                            Principal principal) {
        String currentUser = principal.getName();
        User user = userRepo.findByUsername(currentUser);
        model.addAttribute("currentUser", user);

        Book book = bookRepo.findOne(bookId);
        User bookOwner = book.getUser();
        String bookOwnerString = bookOwner.getUsername();
        model.addAttribute("book", book);
        List<Shelf> shelves = book.getShelves();
        model.addAttribute("selectedShelves", shelves);
        List<Shelf> allShelves = shelfRepo.findAllByUser(user);
        model.addAttribute("allShelves", allShelves);
        List<BookRequest> ifBorrowed = bookRequestRepo.findAllByBookidAndActive(book, true);
        model.addAttribute("ifBorrowed", ifBorrowed);
        if(currentUser.equals(bookOwnerString)) {
            return "editBook";
        } else {
            return "bookDetail";
        }
    }

//    POST request for book edit page
    @RequestMapping(value = "/book/{bookId}/edit", method = RequestMethod.POST)
    public String editBookPost (@PathVariable("bookId") Long bookId,
                                @RequestParam("rating") Integer rating,
                                @RequestParam("copies") Integer copies,
                                @RequestParam("shelves") List<Shelf> shelves) {
        Book book = bookRepo.findOne(bookId);
        book.setCopies(copies);
        if(rating != null) {
            book.setRating(rating);
        } else {
            book.setRating(0);
        }

        List<Shelf> oldShelves = book.getShelves();
        for(Shelf oldShelf : oldShelves) {
            if (!shelves.contains(oldShelf)) {
                List<Book> oldShelfBooks = oldShelf.getBooks();
                oldShelfBooks.remove(book);
                oldShelf.setBooks(oldShelfBooks);
                shelfRepo.save(oldShelf);
            }
        }

        for(Shelf shelf : shelves) {
            List<Book> booksOnShelf = shelf.getBooks();
            if (!booksOnShelf.contains(book)) {
                booksOnShelf.add(book);
                shelf.setBooks(booksOnShelf);
                shelfRepo.save(shelf);
            }
        }

        book.setShelves(shelves);
        bookRepo.save(book);
        return "redirect:/book/{bookId}";
    }

//    POST request for removing a book
    @RequestMapping(value = "/book/{bookId}/remove", method = RequestMethod.POST)
    public String removeBookPost (@PathVariable("bookId") Long bookId) {
        bookRepo.delete(bookId);
        return "redirect:/";
    }
}
