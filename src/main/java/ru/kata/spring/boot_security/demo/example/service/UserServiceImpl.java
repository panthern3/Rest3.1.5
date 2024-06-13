package ru.kata.spring.boot_security.demo.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.example.model.Role;
import ru.kata.spring.boot_security.demo.example.model.User;
import ru.kata.spring.boot_security.demo.example.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.example.repository.UserRepository;



import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void addUser(User user, Set<Role> roles) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> persistentRoles = new HashSet<>();
        for (Role role : roles) {
            Role persistentRole = roleRepository.findByName(role.getName());
            if (persistentRole == null) {
                persistentRole = roleRepository.save(role);
            }
            persistentRoles.add(persistentRole);
        }
        user.setRoles(persistentRoles);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUser(User user, Set<Role> roles) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Флаг для определения, был ли пароль изменен
        boolean passwordChanged = user.getPassword() != null && !user.getPassword().isEmpty() && !passwordEncoder.matches(user.getPassword(), existingUser.getPassword());

        // Если пароль изменился, хешируем его, иначе оставляем текущий хеш
        if (passwordChanged) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(existingUser.getPassword());
        }


        Set<Role> combinedRoles = new HashSet<>(existingUser.getRoles());
        for (Role role : roles) {
            Role persistentRole = roleRepository.findByName(role.getName());
            if (persistentRole == null) {
                persistentRole = roleRepository.save(role);
            }
            combinedRoles.add(persistentRole);
        }
        user.setRoles(combinedRoles);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public Role findRoleByName(String roleName) {
        // Пример реализации метода поиска роли
        switch (roleName.trim()) {
            case "ROLE_USER":
                return new Role("ROLE_USER");
            case "ROLE_ADMIN":
                return new Role("ROLE_ADMIN");
            default:
                throw new IllegalArgumentException("Unknown role: " + roleName);
        }
    }

}
