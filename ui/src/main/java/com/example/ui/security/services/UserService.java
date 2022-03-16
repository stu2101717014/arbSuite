package com.example.ui.security.services;

import com.example.ui.security.data.User;

public interface UserService {

    User saveUser(User user);

    User setDefaultRole(User user);
}
