package com.jennbowers.library.interfaces;

import com.jennbowers.library.models.BookRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRequestRepository extends CrudRepository<BookRequest, Long>{
}
