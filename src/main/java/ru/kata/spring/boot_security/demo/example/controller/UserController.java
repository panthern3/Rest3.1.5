package ru.kata.spring.boot_security.demo.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.example.model.Role;
import ru.kata.spring.boot_security.demo.example.model.User;
import ru.kata.spring.boot_security.demo.example.service.UserService;


import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @PostMapping("/add")
    public String addUser(@RequestParam("name") String name,
                          @RequestParam("email") String email,
                          @RequestParam("password") String password,
                          @RequestParam("roles") Set<Role> roles) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        userService.addUser(user, roles);
        return "redirect:/users";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    @PostMapping("/update")
    public String updateUser(@RequestParam("id") Long id,
                             @RequestParam("name") String name,
                             @RequestParam("email") String email,
                             @RequestParam("password") String password,
                             @RequestParam("roles") String rolesString,
                             Model model) {
        Set<Role> roles = Arrays.stream(rolesString.split(","))
                .filter(role -> !role.trim().isEmpty())
                .map(userService::findRoleByName)
                .collect(Collectors.toSet());

        if (roles.isEmpty()) {
            model.addAttribute("error", "User must have at least one role.");
            model.addAttribute("user", new User(id, name, email, password)); // Add the current user details
            model.addAttribute("roles", rolesString); // Add the entered roles back to the model
            return "updateUserForm"; // Assuming this is the name of your update form template
        }

        userService.updateUser(new User(id, name, email, password), roles);
        return "redirect:/users";
    }

}
