package com.jennbowers.library.interfaces;

import com.jennbowers.library.models.Book;
import com.jennbowers.library.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Long>{
    List<Book> findAllByUser (User user);
    Iterable<Book> findAllByTitle(String title);
    Iterable<Book> findAllByUserAndTitle(User user, String title);
    Iterable<Book> findAllByAuthor(String author);
    Iterable<Book> findAllByUserAndAuthor(User user, String author);
}
