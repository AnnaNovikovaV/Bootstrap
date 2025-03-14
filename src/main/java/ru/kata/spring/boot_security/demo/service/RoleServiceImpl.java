package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

//    @Override
//    public Role findById(Long id) {
//        return roleRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Role not found"));
//    }
//
//    @Override
//    @Transactional
//    public void save(Role role) {
//        roleRepository.save(role);
//    }
//
//    @Override
//    @Transactional
//    public void deleteById(Long id) {
//        roleRepository.deleteById(id);
//    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.getRoleByName(name);
    }


}
