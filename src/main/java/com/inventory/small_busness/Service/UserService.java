package com.inventory.small_busness.Service;


import com.inventory.small_busness.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

//    @Autowired
//    private UserRepository userRepository;
//
//    private PasswordEncoder passwordEncoder;
//
//    public void save(User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userRepository.save(user);
//    }
//
//    public User findByUsername(String username) {
//        return userRepository.findByUsername(username)  .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//    }

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
}
