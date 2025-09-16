package com.example.document.service;

import com.example.document.dto.UserRequest;
import com.example.document.model.Department;
import com.example.document.model.User;
import com.example.document.repository.UserRepository;
import com.example.document.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public UserService(UserRepository userRepository, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    public User createUser(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .role("USER")
                .build();
        return userRepository.save(user);
    }

    public User updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User assignDepartments(Long userId, List<Long> departmentIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Department> newDepartments = new HashSet<>(departmentRepository.findAllById(departmentIds));

        Set<Department> updatedDepartments = new HashSet<>(user.getDepartments());
        updatedDepartments.addAll(newDepartments);
        user.setDepartments(updatedDepartments);

        // Sync with Supabase column
        user.setDepartmentIdsString(updatedDepartments.stream()
                .map(d -> String.valueOf(d.getId()))
                .collect(Collectors.joining(",")));

        return userRepository.save(user);
    }

    @Transactional
    public User unassignDepartments(Long userId, List<Long> departmentIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Department> toRemove = new HashSet<>(departmentRepository.findAllById(departmentIds));

        Set<Department> updatedDepartments = new HashSet<>(user.getDepartments());
        updatedDepartments.removeAll(toRemove);
        user.setDepartments(updatedDepartments);

        // Sync with Supabase column
        user.setDepartmentIdsString(updatedDepartments.stream()
                .map(d -> String.valueOf(d.getId()))
                .collect(Collectors.joining(",")));

        return userRepository.save(user);
    }
}
