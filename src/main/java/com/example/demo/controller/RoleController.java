package com.example.demo.controller;

import com.example.demo.model.Role;
import com.example.demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // Ajouter un rôle
    @PostMapping
    public ResponseEntity<Role> addRole(@RequestBody Role role) {
        Role createdRole = roleService.addRole(role);
        return ResponseEntity.ok(createdRole);
    }

    // Obtenir tous les rôles
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    // Obtenir un rôle par ID
    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        Optional<Role> role = roleService.getRoleById(id);

        return ResponseEntity.ok(role.get());

    }

    // Mettre à jour un rôle
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role roleDetails) {

        Role updatedRole = roleService.updateRole(id, roleDetails);
        return ResponseEntity.ok(updatedRole);

    }

    // Supprimer un rôle
    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable Long id) {

        roleService.deleteRole(id);

    }
}

