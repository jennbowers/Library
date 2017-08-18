package com.jennbowers.library.interfaces;

import com.jennbowers.library.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
