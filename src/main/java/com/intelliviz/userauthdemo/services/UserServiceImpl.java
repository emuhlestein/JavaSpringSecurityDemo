package com.intelliviz.userauthdemo.services;

import com.intelliviz.userauthdemo.models.UserDao;
import com.intelliviz.userauthdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository repo;

    @Override
    public UserDao findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public UserDao save(UserDao user) {
        return repo.save(user);
    }
}
