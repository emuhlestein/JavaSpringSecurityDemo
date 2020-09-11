package com.intelliviz.userauthdemo.services;

import com.intelliviz.userauthdemo.models.UserDao;

public interface UserService {
    UserDao findUserByUsername(String username);
    UserDao findUserByEmail(String email);
    UserDao findById(Long id);
    UserDao save(UserDao user);
}
