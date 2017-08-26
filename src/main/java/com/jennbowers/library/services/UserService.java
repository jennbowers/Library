package com.jennbowers.library.services;

import com.jennbowers.library.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService{
    User findByUsername (String username);
}
