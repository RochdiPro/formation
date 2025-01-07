package com.example.demo.controller;

import com.example.demo.config.JwtUtil;
import com.example.demo.model.AuthRequest;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "Gestion des utilisateurs")
public class UserController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserService userService;


    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest user) {
        try {
            // Authentification de l'utilisateur
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()
                    )
            );
            // Chargement des détails de l'utilisateur
            UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
            // Génération du token JWT
            String token = jwtUtil.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(token);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

    }

    // ajoter un utilisateur
    @PostMapping()
    public ResponseEntity<User> add(@RequestBody User user) {
        System.out.println(user);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.addUser(user);
        return ResponseEntity.ok(user);

    }

    // Obtenir tous les Users
    @GetMapping
    @Operation(summary = "Obtenir tous les utilisateurs", description = "Récupère une liste de tous les utilisateurs.")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUers();
        return ResponseEntity.ok(users);
    }

    // Obtenir un User par ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return ResponseEntity.ok(user.get());
    }

    // Mettre à jour un rôle
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {

        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    // Supprimer un User
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}