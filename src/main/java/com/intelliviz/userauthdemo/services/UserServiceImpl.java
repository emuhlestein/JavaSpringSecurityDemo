package com.intelliviz.userauthdemo.services;

import com.intelliviz.userauthdemo.domain.UserPrinciple;
import com.intelliviz.userauthdemo.models.User;
import com.intelliviz.userauthdemo.models.UserDao;
import com.intelliviz.userauthdemo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private UserRepository repo;

    @Autowired
    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDao findUserByUsername(String username) {
        return repo.findUserByUsername(username);
    }

    @Override
    public UserDao findUserByEmail(String email) {
        return repo.findUserByEmail(email);
    }

    @Override
    public UserDao findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public UserDao save(UserDao user) {
        return repo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDao userDao = repo.findUserByUsername(username);
        if(userDao == null) {
            logger.error("User not found by username: " + username);
            throw new UsernameNotFoundException("User not found by username: " + username);
        } else {
            userDao.setLastLoginDateDisplay(userDao.getLastLoginDate());
            userDao.setLastLoginDate(LocalDateTime.now());
            repo.save(userDao);
            UserPrinciple userPrinciple = new UserPrinciple(userDao);
            logger.info("Returning user found by username: " + username);
            return userPrinciple;
        }
    }
}
