package com.example.finalproj.service;


import com.example.finalproj.entity.User;
import com.example.finalproj.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private EmailService emailService;


    public User add(User user) {
       user.setPassword(passwordEncoder.encode(user.getPassword()));
        return  userRepository.save(user);
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }



    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void saveUserWithResetCode(User user) {
        String resetCode = String.valueOf((int) (Math.random() * 1000000));

        user.setResetCode(resetCode);

        userRepository.save(user);

        emailService.sendCode(user.getEmail(), resetCode);
    }


    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole()))
        );
    }

    public User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return findByUsername(username);
        } else {
            throw new RuntimeException("Invalid authentication");
        }
    }


}
