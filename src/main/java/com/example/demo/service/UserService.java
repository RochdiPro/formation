package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;




    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Ajouter un User
    public User addUser(User user) {
        return userRepository.save(user);
    }

    // Obtenir tous les Users
    public List<User> getAllUers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                UserDetails connectedUser = (UserDetails) principal;
                System.out.println("Connected User: " + connectedUser.getUsername());
                for (GrantedAuthority authority : connectedUser.getAuthorities()) {
                    System.out.println("Authority: " + authority.getAuthority());
                }
            } else {
                System.out.println("Connected User: " + principal.toString());
            }
        }

        return userRepository.findAll();
    }

    // Obtenir un User par ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Mettre à jour un User
    public User updateUser(Long id, User userUpdate) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        return userRepository.save(userUpdate);
    }

    // Supprimer un User
    public void deleteUser(Long id) {
        User role = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        userRepository.delete(role);
    }
}
