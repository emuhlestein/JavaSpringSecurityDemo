package com.intelliviz.userauthdemo.services;

import com.intelliviz.userauthdemo.domain.UserPrinciple;
import com.intelliviz.userauthdemo.exception.domain.EmailExistException;
import com.intelliviz.userauthdemo.exception.domain.UserNotFoundException;
import com.intelliviz.userauthdemo.exception.domain.UsernameExistException;
import com.intelliviz.userauthdemo.models.User;
import com.intelliviz.userauthdemo.models.UserDao;
import com.intelliviz.userauthdemo.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static com.intelliviz.userauthdemo.enumeration.Role.ROLE_USER;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private UserRepository repo;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repo, BCryptPasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
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
    public UserDao register(String firstName, String lastName, String username, String email) throws UserNotFoundException, UsernameExistException, EmailExistException {
        validateNewUser(StringUtils.EMPTY, username, email);

        UserDao userDao = new UserDao();
        userDao.setUserId(generateUserId());
        String password = generatePassword();
        String encodedPassword = encodePassword(password);
        userDao.setFirstName(firstName);
        userDao.setLastName(lastName);
        userDao.setUserName(username);
        userDao.setEmail(email);
        userDao.setJoinDate(LocalDateTime.now());
        userDao.setPassword(encodedPassword);
        userDao.setActive(true);
        userDao.setNotLocked(true);
        userDao.setRole(ROLE_USER.name());
        userDao.setAuthorities(ROLE_USER.getAuthorities());
        userDao.setProfileImageUrl(getTemporaryProfileImageUrl());

        repo.save(userDao);
        logger.info("New user password: " + password);
        return null;
    }

    private String getTemporaryProfileImageUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/image/profile/temp").toUriString();
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generateUserId() {
        return RandomStringUtils.random(10);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private UserDao validateNewUser(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
        if(StringUtils.isNotBlank(currentUsername)) {
            UserDao currentUser = findUserByUsername(currentUsername);
            if(currentUser == null) {
                throw new UserNotFoundException("No user found by username " + currentUsername);
            }
            UserDao userByNewUsername = findUserByUsername(newUsername);
            if(userByNewUsername != null && currentUser.getId().equals(userByNewUsername.getId())) {
                throw new UsernameExistException("Username already exists");
            }

            UserDao userByNewEmail = findUserByEmail(newEmail);
            if(userByNewEmail != null && currentUser.getId().equals(userByNewEmail.getId())) {
                throw new EmailExistException("Email already exists");
            }

            return currentUser;
        } else {
            // new user
            UserDao userByUsername = findUserByUsername(newUsername);
            if(userByUsername != null) {
                throw new UsernameExistException("Username already exists");
            }
            UserDao userByEmail = findUserByEmail(newEmail);
            if(userByEmail != null) {
                throw new EmailExistException("Email already exists");
            }
            return null;
        }
    }

    @Override
    public List<UserDao> getUsers() {
        return null;
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
