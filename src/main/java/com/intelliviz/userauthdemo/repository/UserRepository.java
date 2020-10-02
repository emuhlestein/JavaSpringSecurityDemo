package com.intelliviz.userauthdemo.repository;

import com.intelliviz.userauthdemo.models.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Repository
@Transactional
public class UserRepository {
    @Autowired
    EntityManager em;

    public UserDao findUserByUsername(String username) {
//        Query query = em.createNativeQuery("SELECT u FROM user u WHERE u.user_name = :username", UserDao.class);
//        query.setParameter("username", username);
//        Object obj = query.getSingleResult();
//        return (UserDao) query.getSingleResult();
        TypedQuery<UserDao> query1 = em.createQuery("SELECT u FROM UserDao u WHERE u.userName = :username", UserDao.class);
        query1.setParameter("username", username);
        return (UserDao) JpaResultHelper.getSingleResult(query1);
    }

    public UserDao findUserByEmail(String email) {
        TypedQuery<UserDao> query = em.createQuery("SELECT u FROM UserDao u WHERE u.email = :email", UserDao.class);
        query.setParameter("email", email);
        return (UserDao) JpaResultHelper.getSingleResult(query);
    }

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
