package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById (Long id);
    User getUserByUsername (String username);
    void saveUser (User user);
    void removeUserById(Long id);
    void updateUser (Long Id, User user);


}
