package ru.kata.spring.boot_security.demo.example.service;

import ru.kata.spring.boot_security.demo.example.model.Role;
import ru.kata.spring.boot_security.demo.example.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> getAllUsers();
    void addUser(User user, Set<Role> roles);
    void updateUser(User user, Set<Role> roles);
    void deleteUser(Long id);
    User findById(Long id);
    User findByEmail(String email);

    Role findRoleByName(String roleName);

    User getUserById(Long id);
}
