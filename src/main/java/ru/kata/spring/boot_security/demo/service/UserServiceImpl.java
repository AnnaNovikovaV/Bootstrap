package ru.kata.spring.boot_security.demo.service;


import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.userMapper.UserMapper;

import javax.annotation.PostConstruct;
import java.util.List;


@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService, UserDetailsService {

    UserRepository repository;
    RoleService roleService;
    PasswordEncoder passwordEncoder;
    UserMapper mapper;

    public UserServiceImpl(UserRepository repository, RoleService roleService, UserMapper mapper, @Lazy PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.roleService = roleService;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User getUserByUsername(String username) {
        return repository.findByUsername(username);
    }

    public User findForEdit(int id) {
        User user = repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword("");
        return user;
    }

    public User findById(int id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
    }

    @Transactional
    public void update(User updatedUser) {

        User user = findById(updatedUser.getId());
        repository.save(mapper.toUser(user, updatedUser));
    }

    @Transactional
    public void delete(int id) {
        repository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @PostConstruct
    public void initDefaultUsers() {
        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        roleService.saveAll(List.of(userRole, adminRole));

        User admin = new User();
        admin.setUsername("admin");
        admin.setLastname("admin");
        admin.setAge(30);
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRoles(List.of(adminRole, userRole));

        User user = new User();
        user.setUsername("user");
        user.setLastname("user");
        user.setAge(20);
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setRoles(List.of(userRole));

            repository.saveAll(List.of(admin, user));
        }
    }
