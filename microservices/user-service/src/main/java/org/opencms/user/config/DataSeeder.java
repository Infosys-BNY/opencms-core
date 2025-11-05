package org.opencms.user.config;

import org.opencms.user.entity.Group;
import org.opencms.user.entity.User;
import org.opencms.user.repository.GroupRepository;
import org.opencms.user.repository.UserRepository;
import org.opencms.user.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private PasswordService passwordService;
    
    @Override
    public void run(String... args) {
        Group adminGroup = new Group();
        adminGroup.setGroupName("Administrators");
        adminGroup.setGroupDescription("System administrators");
        adminGroup.setGroupOu("/");
        adminGroup.setGroupFlags(0);
        adminGroup = groupRepository.save(adminGroup);
        
        Group usersGroup = new Group();
        usersGroup.setGroupName("Users");
        usersGroup.setGroupDescription("Regular users");
        usersGroup.setGroupOu("/");
        usersGroup.setGroupFlags(0);
        usersGroup = groupRepository.save(usersGroup);
        
        Group guestsGroup = new Group();
        guestsGroup.setGroupName("Guests");
        guestsGroup.setGroupDescription("Guest users");
        guestsGroup.setGroupOu("/");
        guestsGroup.setGroupFlags(0);
        guestsGroup = groupRepository.save(guestsGroup);
        
        User admin = new User();
        admin.setUserName("Admin");
        admin.setUserPassword(passwordService.encryptPassword("admin"));
        admin.setUserFirstname("System");
        admin.setUserLastname("Administrator");
        admin.setUserEmail("admin@opencms.org");
        admin.setUserOu("/");
        admin.setUserFlags(1);
        admin.getGroups().add(adminGroup);
        userRepository.save(admin);
        
        User guest = new User();
        guest.setUserName("Guest");
        guest.setUserPassword(passwordService.encryptPassword("guest"));
        guest.setUserFirstname("Guest");
        guest.setUserLastname("User");
        guest.setUserEmail("guest@opencms.org");
        guest.setUserOu("/");
        guest.setUserFlags(1);
        guest.getGroups().add(guestsGroup);
        userRepository.save(guest);
    }
}
