package org.opencms.user.controller;

import org.opencms.user.dto.UserDto;
import org.opencms.user.entity.User;
import org.opencms.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userService.toDto(user));
    }
    
    @GetMapping("/name/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(userService.toDto(user));
    }
    
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody Map<String, Object> request) {
        UserDto userDto = new UserDto();
        userDto.setUserName((String) request.get("username"));
        userDto.setUserFirstname((String) request.get("firstname"));
        userDto.setUserLastname((String) request.get("lastname"));
        userDto.setUserEmail((String) request.get("email"));
        userDto.setUserOu((String) request.get("orgUnit"));
        userDto.setUserFlags((Integer) request.get("flags"));
        
        String password = (String) request.get("password");
        User user = userService.createUser(userDto, password);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.toDto(user));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String id, @RequestBody UserDto userDto) {
        User user = userService.updateUser(id, userDto);
        return ResponseEntity.ok(userService.toDto(user));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllUsers(Pageable pageable) {
        Page<User> users = userService.getAllUsers(pageable);
        Page<UserDto> userDtos = users.map(user -> userService.toDto(user));
        return ResponseEntity.ok(userDtos);
    }
}
