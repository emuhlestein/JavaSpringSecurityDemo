package com.intelliviz.userauthdemo.repository;

import com.intelliviz.userauthdemo.models.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Repository
@Transactional
public class UserRepository {
    @Autowired
    EntityManager em;

    public UserDao findById(Long id) {
        return em.find(UserDao.class, id);
    }

    public UserDao save(UserDao user) {
        if(user.getId() == null) {
            // add a new user
            em.persist(user);
        } else {
            // update existing user
            em.merge(user);
        }
        return user;
    }
}
