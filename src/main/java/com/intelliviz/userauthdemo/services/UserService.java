package com.intelliviz.userauthdemo.services;

import com.intelliviz.userauthdemo.exception.domain.EmailExistException;
import com.intelliviz.userauthdemo.exception.domain.UserNotFoundException;
import com.intelliviz.userauthdemo.exception.domain.UsernameExistException;
import com.intelliviz.userauthdemo.models.User;
import com.intelliviz.userauthdemo.models.UserDao;

import java.util.List;

public interface UserService {
    UserDao findUserByUsername(String username);
    UserDao findUserByEmail(String email);
    UserDao findById(Long id);
    UserDao save(UserDao user);
    UserDao register(String firstName, String lastName, String username, String email) throws UserNotFoundException, UsernameExistException, EmailExistException;
    List<UserDao> getUsers();
}
