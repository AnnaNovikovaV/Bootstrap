package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAll();
//    Role findById(Long id);
    void saveAll(List<Role> roles);
//    void deleteById(Long id);
    Role getRoleByName(String name);
}
