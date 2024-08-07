package com.inventory.small_busness.Service;


import com.inventory.small_busness.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var userName = repository.findByUsername(username);

        User.UserBuilder userBuilder = null;

        if (userName.isPresent()) {
            com.inventory.small_busness.Models.User cureentUser = userName.get();

            userBuilder = User.withUsername(username);
            userBuilder.password(cureentUser.getPassword());
            userBuilder.roles(cureentUser.getRole());
            return userBuilder.build();

        } else {
            throw new UsernameNotFoundException("USER NOT FOUND");
        }
    }

    public List<com.inventory.small_busness.Models.User> findAll() {
        return repository.findAll();
    }

    public void update(com.inventory.small_busness.Models.User user) {
        // Find the existing user by ID
        var existingUser = repository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + user.getId()));

        // Update the user's details
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setRole(user.getRole());

        // Save the updated user back to the database
        repository.save(existingUser);
    }

    public void deleteUsers(Long id) throws Exception {
        var existingUser = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: "));
        if (!Objects.equals(existingUser.getRole(), "MANAGER")) {
            repository.deleteById(existingUser.getId());
        } else {
            throw new Exception("You cannot delete the MANAGER");
        }
    }
}
