package com.jennbowers.library.interfaces;

import com.jennbowers.library.models.Book;
import com.jennbowers.library.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Long>{
    List<Book> findAllByUser (User user);
    Iterable<Book> findAllByTitleIgnoreCase(String title);
    Iterable<Book> findAllByTitleContaining(String title);
    Iterable<Book> findAllByUserAndTitleIgnoreCase(User user, String title);
    Iterable<Book> findAllByUserAndTitleContaining(User user, String title);
    Iterable<Book> findAllByAuthorIgnoreCase(String author);
    Iterable<Book> findAllByAuthorContaining(String author);
    Iterable<Book> findAllByUserAndAuthorIgnoreCase(User user, String author);
    Iterable<Book> findAllByUserAndAuthorContaining(User user, String author);
}
