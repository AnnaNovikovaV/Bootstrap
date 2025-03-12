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
import java.util.Set;


@Controller
@RequestMapping()
public class AdminController {

    UserService userService;

    RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String index(Model model, Principal principal) {
        model.addAttribute("user", userService.getUserByUsername(principal.getName()));
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", roleService.findAll());
        return "admin";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("user") User user) {
        return "admin";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("user") User user, @RequestParam("roles") List<String> roleNames) {
        Set<Role> roles = Set.of();
        for (Role role : user.getRoles()) {
            roles.add(roleService.getRoleByName(role.getName()));
        }
        user.setRoles(roles);
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public String edit(Model model, @RequestParam("id") int id) {
        model.addAttribute("user", userService.findForEdit(id));
        model.addAttribute("roles", roleService.findAll());
        return "admin";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("user") User user, Model model ) {
        Set<Role> roles = Set.of();
        for (Role role : user.getRoles()) {
            roles.add(roleService.getRoleByName(role.getName()));
        }
        user.setRoles(roles);
        userService.update(user);

        return "redirect:/admin";
    }

    @DeleteMapping("{id}/delete")
    public String delete(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
