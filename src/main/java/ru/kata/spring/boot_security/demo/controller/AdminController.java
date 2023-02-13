package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }
    @GetMapping
    public String adminPage(Model model){
        model.addAttribute("users", userService.getAllUsers());
        return "adminPage";
    }

    @GetMapping("/new")
    public String getUserCreateForm(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roles", roleService.getRoles());
        return "newUser";
    }
    @PostMapping("/createNew")
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "my_roles") String stringRole) {
        Role role = new Role(stringRole);
        roleService.saveRole(role);
        user.setRoles(Set.of(role));
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping( "/{id}/edit")
    public String getUserUpdateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("roles", roleService.getRoles());
        return "editUser";
    }

    @PostMapping( "/{id}")
    public String updateUser(@ModelAttribute("user") User user, @PathVariable("id") Long id,) {
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}/delete")
    public String removeUserById(@PathVariable("id") Long id) {
        roleService.removeRoleById(id);
        userService.removeUserById(id);
        return "redirect:/admin";
    }



}
