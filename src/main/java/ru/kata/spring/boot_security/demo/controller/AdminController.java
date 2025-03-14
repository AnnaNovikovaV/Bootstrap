package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@RequestMapping("admin")
public class AdminController {

    UserService userService;

    RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String index(Model model, Principal principal) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("newUser", new User());
        model.addAttribute("generalUser", userService.getUserByUsername(principal.getName()));
        model.addAttribute("user", userService.getUserByUsername(principal.getName()));
        return "admin";
    }

//    @PostMapping("/new")
//    public String newPerson(@ModelAttribute("newUser") User user) {
//        return "admin";
//    }

    @PostMapping("/create")
    public String create(@ModelAttribute("newUser") User user,@RequestParam("roles") List<String> roleNames) {
        List<Role> roles = roleNames.stream()
                .map(roleService::getRoleByName)
                .collect(Collectors.toList());
        user.setRoles(roles);
        userService.save(user);
        return "redirect:/admin";
    }

    @PatchMapping("/edit")
    public String edit(Model model, @RequestParam("id") int id) {
        model.addAttribute("user", userService.findForEdit(id));
        return "admin";
    }

    @PatchMapping("/edit/update")
    public String update(@ModelAttribute("user") User user, @RequestParam("roles") List<String> roleNames) {
        List<Role> roles = roleNames.stream()
                .map(roleService::getRoleByName)
                .collect(Collectors.toList());
        user.setRoles(roles);

        userService.update(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
