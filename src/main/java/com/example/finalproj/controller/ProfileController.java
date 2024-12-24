package com.example.finalproj.controller;


import com.example.finalproj.entity.User;
import com.example.finalproj.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.UUID;

@AllArgsConstructor
@Controller
@RequestMapping("/bakery")
public class ProfileController {
    private final UserService userService;

    private static final String UPLOAD_DIR = "C:\\Users\\whyco\\IdeaProjects\\finalproj\\src\\main\\resources\\static\\images\\";

    @GetMapping("/profile")
    public String getProfile(Model model, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        model.addAttribute("user", currentUser);
        return "profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(Model model, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        model.addAttribute("user", currentUser);
        return "update_profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@Valid @ModelAttribute("user") User user,
                                BindingResult result,
                                @RequestParam("profilePicture") MultipartFile profilePicture,
                                Principal principal,
                                Model model) {
        if (result.hasErrors()) {
            return "update_profile";
        }

        User existingUser = userService.findByUsername(principal.getName());
        if (existingUser == null) {
            model.addAttribute("error", "User not found.");
            return "redirect:/bakery/profile";
        }

        existingUser.setUsername(user.getUsername());

        if (!profilePicture.isEmpty()) {
            try {
                String fileName = UUID.randomUUID() + "_" + profilePicture.getOriginalFilename();
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, profilePicture.getBytes());
                existingUser.setProfileImage("/images/" + fileName);
            } catch (IOException e) {
                model.addAttribute("error", "Failed to upload the image.");
                return "update_profile";
            }
        }

        if (existingUser.getProfileImage() == null || existingUser.getProfileImage().isEmpty()) {
            existingUser.setProfileImage("/images/olaf-default-avatar.jpg");
        }

        userService.saveUser(existingUser);
        model.addAttribute("success", "Profile updated successfully!");
        return "redirect:/bakery/profile";
    }
}
