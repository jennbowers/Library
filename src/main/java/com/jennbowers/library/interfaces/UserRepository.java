package com.jennbowers.library.interfaces;

import com.jennbowers.library.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
    Iterable<User> findByFirstNameAndLastName (String firstName, String lastName);

}
