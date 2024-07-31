package com.inventory.small_busness.Controller;

import com.inventory.small_busness.Models.User;
import com.inventory.small_busness.Repository.UserRepository;
import com.inventory.small_busness.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/api/v1/inventory")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(@ModelAttribute("user") User user) {
        return "register";
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@Valid User user) {
        ModelAndView mv = new ModelAndView();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        mv.setViewName("redirect:/api/v1/inventory/login");
        return mv;
    }
}
