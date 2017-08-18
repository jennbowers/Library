package com.jennbowers.library.interfaces;

import com.jennbowers.library.models.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long>{
}
