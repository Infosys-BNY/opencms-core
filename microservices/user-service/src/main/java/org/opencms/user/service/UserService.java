package org.opencms.user.service;

import org.opencms.user.dto.UserDto;
import org.opencms.user.entity.User;
import org.opencms.user.exception.ResourceNotFoundException;
import org.opencms.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordService passwordService;
    
    public User getUserById(String userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }
    
    public User getUserByUsername(String username) {
        return userRepository.findByUserName(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }
    
    @Transactional
    public User createUser(UserDto userDto, String password) {
        if (userRepository.existsByUserName(userDto.getUserName())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        User user = new User();
        user.setUserName(userDto.getUserName());
        user.setUserPassword(passwordService.encryptPassword(password));
        user.setUserFirstname(userDto.getUserFirstname());
        user.setUserLastname(userDto.getUserLastname());
        user.setUserEmail(userDto.getUserEmail());
        user.setUserFlags(userDto.getUserFlags() != null ? userDto.getUserFlags() : 1);
        user.setUserOu(userDto.getUserOu() != null ? userDto.getUserOu() : "/");
        
        return userRepository.save(user);
    }
    
    @Transactional
    public User updateUser(String userId, UserDto userDto) {
        User user = getUserById(userId);
        
        if (userDto.getUserFirstname() != null) {
            user.setUserFirstname(userDto.getUserFirstname());
        }
        if (userDto.getUserLastname() != null) {
            user.setUserLastname(userDto.getUserLastname());
        }
        if (userDto.getUserEmail() != null) {
            user.setUserEmail(userDto.getUserEmail());
        }
        if (userDto.getUserFlags() != null) {
            user.setUserFlags(userDto.getUserFlags());
        }
        
        return userRepository.save(user);
    }
    
    @Transactional
    public void deleteUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }
    
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    
    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setUserFirstname(user.getUserFirstname());
        dto.setUserLastname(user.getUserLastname());
        dto.setUserEmail(user.getUserEmail());
        dto.setUserLastlogin(user.getUserLastlogin());
        dto.setUserFlags(user.getUserFlags());
        dto.setUserOu(user.getUserOu());
        dto.setUserDatecreated(user.getUserDatecreated());
        return dto;
    }
}
