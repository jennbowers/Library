package com.jennbowers.library.interfaces;

import com.jennbowers.library.models.Shelf;
import com.jennbowers.library.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShelfRepository extends CrudRepository<Shelf, Long>{
    List<Shelf> findAllByUser (User user);
}
