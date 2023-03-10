package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;

public interface RoleService {
    List<Role> getRoles();

    void saveRole(Role role);

    void removeRoleById(Long id);
}

