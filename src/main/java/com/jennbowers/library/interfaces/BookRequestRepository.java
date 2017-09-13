package com.jennbowers.library.interfaces;

import com.jennbowers.library.models.Book;
import com.jennbowers.library.models.BookRequest;
import com.jennbowers.library.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookRequestRepository extends CrudRepository<BookRequest, Long>{
    Iterable<BookRequest> findAllByTouserAndActive(User user, Boolean booleanParam);
    Iterable<BookRequest> findAllByFromuserAndActive(User user, Boolean booleanParam);
    List<BookRequest> findAllByTouserAndFromuserAndActive(User touser, User fromuser, Boolean booleanParam);
    Iterable<BookRequest> findAllByTouserAndPending(User user, Boolean booleanParam);
    Iterable<BookRequest> findAllByFromuserAndPending(User user, Boolean booleanParam);
    List<BookRequest> findAllByTouserAndFromuserAndPending(User touser, User fromuser, Boolean booleanParam);
    List<BookRequest> findAllByBookidAndActive(Book book, Boolean booleanParam);
    List<BookRequest> findAllByBookid(Book book);
    List<BookRequest> findAllByBookidAndFromuserAndPending(Book book, User user, Boolean booleanParam);
    List<BookRequest> findAllByBookidAndFromuser(Book book, User user);
    List<BookRequest> findAllByActive(Boolean booleanParam);
}
