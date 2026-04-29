package org.example.userservice.controller;

import lombok.AllArgsConstructor;
import org.example.userservice.dto.UserRegistrationDto;
import org.example.userservice.model.User;
import org.example.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> findAll() {

        return userService.findAll();
    }

    @PostMapping("/registration")
    public ResponseEntity<User> create(@RequestBody UserRegistrationDto dto) {

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return ResponseEntity.ok(userService.create(user));
    }


}
