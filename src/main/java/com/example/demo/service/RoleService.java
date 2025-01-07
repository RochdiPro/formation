package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    // Ajouter un rôle
    public Role addRole(Role role) {
        return roleRepository.save(role);
    }

    // Obtenir tous les rôles
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Obtenir un rôle par ID
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    // Mettre à jour un rôle
    public Role updateRole(Long id, Role roleDetails) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        role.setName(roleDetails.getName());
        return roleRepository.save(role);
    }

    // Supprimer un rôle
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        roleRepository.delete(role);
    }
}
