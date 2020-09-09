package com.intelliviz.userauthdemo.services;

import com.intelliviz.userauthdemo.models.UserDao;

public interface UserService {
    UserDao findById(Long id);
    UserDao save(UserDao user);
}
