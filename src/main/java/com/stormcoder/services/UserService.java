package com.stormcoder.services;

import com.stormcoder.entities.authenticate.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);
}
