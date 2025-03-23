package com.Harum.Harum.Controllers;

import com.Harum.Harum.Models.Users;
import com.Harum.Harum.Services.UserService;
import com.Harum.Harum.Constants.StatusCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Get all users
    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.status(StatusCodes.OK.getCode()).body(userService.getAllUsers());
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.status(StatusCodes.OK.getCode()).body(user))
                .orElseGet(() -> ResponseEntity.status(StatusCodes.NOT_FOUND.getCode()).build());
    }

    // Create new user
    @PostMapping
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        return ResponseEntity.status(StatusCodes.CREATED.getCode()).body(userService.createUser(user));
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable String id, @RequestBody Users userDetails) {
        return userService.updateUser(id, userDetails)
                .map(user -> ResponseEntity.status(StatusCodes.OK.getCode()).body(user))
                .orElseGet(() -> ResponseEntity.status(StatusCodes.NOT_FOUND.getCode()).build());
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        return userService.deleteUser(id)
                ? ResponseEntity.status(StatusCodes.NO_CONTENT.getCode()).build()
                : ResponseEntity.status(StatusCodes.NOT_FOUND.getCode()).build();
    }
}
