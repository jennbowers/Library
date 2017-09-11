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
    Iterable<BookRequest> findAllByTouserAndPending(User user, Boolean booleanParam);
    Iterable<BookRequest> findAllByFromuserAndPending(User user, Boolean booleanParam);
    BookRequest findAllByBookidAndActive(Book book, Boolean booleanParam);
    List<BookRequest> findAllByActive(Boolean booleanParam);
}
