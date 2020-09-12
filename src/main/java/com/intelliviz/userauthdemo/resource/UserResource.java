package com.intelliviz.userauthdemo.resource;

import com.intelliviz.userauthdemo.exception.domain.ExceptionHandling;
import com.intelliviz.userauthdemo.models.User;
import com.intelliviz.userauthdemo.models.UserDao;
import com.intelliviz.userauthdemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/user")
public class UserResource extends ExceptionHandling {
    @Autowired
    private UserService userService;

    public UserResource() {
    }

    @GetMapping("")
    public String getUser() {
        return "Ed Muhlestein";
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        System.out.println("id: " + id);
        return convertToUser(userService.findById(id));
    }

    @PostMapping("/")
    public User addNewUser(@RequestBody User insertUser) {
        if(insertUser.getFirstName() == null || insertUser.getFirstName().length() == 0) {
            throw new InvalidParameterException("First name is required");
        }
        if(insertUser.getLastName() == null || insertUser.getLastName().length() == 0) {
            throw new InvalidParameterException("Last name is required");
        }
        if(insertUser.getEmail() == null || insertUser.getEmail().length() == 0) {
            throw new InvalidParameterException("email is required");
        }
        if(insertUser.getPassword() == null || insertUser.getPassword().length() == 0) {
            throw new InvalidParameterException("password is required");
        }
        if(insertUser.getUserId() == null || insertUser.getUserId().length() == 0) {
            throw new InvalidParameterException("User id is required");
        }
        return convertToUser(userService.save(createNewEntity(insertUser)));
    }

    @PostMapping("/{id}")
    public User updateUser(@RequestBody User updateUser) {
        System.out.println(updateUser.toString());
        return convertToUser(userService.save(updateEntity(updateUser)));
    }

    private UserDao updateEntity(User user) {
        UserDao userDao = convertUserToUserDao(user);
        userDao.setLastLoginDate(LocalDateTime.now());
        userDao.setLastLoginDateDisplay(LocalDateTime.now());

//        userDao.setActive(true);
//        userDao.setLocked(false);
//        userDao.setAuthorities(new String[0]);
//        userDao.setRoles(new String[0]);

        return userDao;
    }

    private UserDao convertUserToUserDao(User user) {
        return new UserDao(user.getUserId(), user.getFirstName(), user.getLastName(), user.getUserName(),
                user.getPassword(), user.getEmail(), user.getProfileImageUrl());
    }

    private UserDao createNewEntity(User user) {
        UserDao userDao = convertUserToUserDao(user);
        userDao.setJoinDate(LocalDate.now());
        userDao.setLastLoginDate(LocalDateTime.now());
        userDao.setLastLoginDateDisplay(LocalDateTime.now());
        userDao.setActive(true);
        userDao.setNotLocked(false);
        userDao.setAuthorities(new String[0]);
        userDao.setRoles(new String[0]);

        return userDao;
    }

    private User convertToUser(UserDao userDao) {
        return new User(userDao.getUserId(), userDao.getFirstName(), userDao.getLastName(), userDao.getUserName(),
                userDao.getPassword(), userDao.getEmail(), userDao.getProfileImageUrl());
    }
}
