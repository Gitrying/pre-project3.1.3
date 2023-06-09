package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.security.Principal;
import java.util.List;

@Controller
public class UsersController {
    private final UserService userService;
    @Autowired
    public UsersController( UserService userService ) {
        this.userService = userService;
    }
    @GetMapping("/user")
    public String userInfo(Model model, Principal principal) {
        User user = userService.findByUserEmail(principal.getName());
        model.addAttribute("user", user);
        return "user-info";
    }

    @GetMapping("/admin/{id}")
    public String userById(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.userById(id));
        return "user-info";
    }

    @GetMapping("/admin-info")
    public String adminInfo(Model model, Principal principal){
        User user = userService.findByUserEmail(principal.getName());
        model.addAttribute("user", user);
        return "admin-info";
    }

    @GetMapping("/admin")
    public String findAll(Model model, Principal principal){
        List<User> users = userService.usersAll();
        User user = userService.findByUserEmail(principal.getName());
        model.addAttribute("users", users);
        model.addAttribute("user", user);
        return "admin-panel";
    }

    @PostMapping("/admin/user-create")
    public String createUser(User user, @RequestParam(value = "role") String[] roles){
        user.setRoles(userService.getRoles(roles));
        userService.userAdd(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/user-deleteconfirm/{id}")
    public String confirmDeleteUser(@PathVariable("id") Long id){
        userService.userDelete(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/user-delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model, Authentication authentication){
        User user1 = userService.userById(id);
        User user2 = userService.userById(userService.findByUserEmail(authentication.getName()).getId());
        model.addAttribute("user1", user1);
        model.addAttribute("user2", user2);
        model.addAttribute("users", userService.usersAll());
        return "user-delete";
    }

    @GetMapping("/admin/user-update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model, Authentication authentication){
        User user1 = userService.userById(id);
        User user2 = userService.userById(userService.findByUserEmail(authentication.getName()).getId());
        model.addAttribute("user1", user1);
        model.addAttribute("user2", user2);
        model.addAttribute("users", userService.usersAll());
        return "user-edit";
    }

    @PostMapping("/admin/user-update")
    public String updateUser(User user, @RequestParam(value = "role") String[] roles){
        user.setRoles(userService.getRoles(roles));
        userService.userAdd(user);
        return "redirect:/admin";
    }
}