package com.example.document.controller;

import com.example.document.dto.UserRequest;
import com.example.document.dto.UserResponse;
import com.example.document.model.User;
import com.example.document.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponse createUser(@RequestBody UserRequest request) {
        User user = userService.createUser(request);
        return new UserResponse(user);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        User user = userService.updateUser(id, request);
        return new UserResponse(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}/assign-departments")
    public UserResponse assignDepartments(@PathVariable Long id, @RequestBody List<Long> departmentIds) {
        User user = userService.assignDepartments(id, departmentIds);
        return new UserResponse(user);
    }

    @PutMapping("/{id}/unassign-departments")
    public UserResponse unassignDepartments(@PathVariable Long id, @RequestBody List<Long> departmentIds) {
        User user = userService.unassignDepartments(id, departmentIds);
        return new UserResponse(user);
    }
}
