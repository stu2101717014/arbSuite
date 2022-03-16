package com.example.ui.security.services;

import com.example.ui.security.data.Role;
import com.example.ui.security.data.RoleRepository;
import com.example.ui.security.data.User;
import com.example.ui.security.data.UserRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@SpringComponent
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Role> all = this.roleRepository.findAll();
        Stream<Role> roleAdmin = all.stream().filter(r -> r.getName().equals("ROLE_Admin"));
        if (!roleAdmin.findAny().isPresent()){
            Role role = new Role();
            role.setName("ROLE_Admin");

            User user = new User();
            user.setName("admin");
            user.setEmail("admin@test.ts");
            user.setPassword("$2a$10$iLOpafHqsjZm..tTOCPT0uiSg8x/pQslhKRhYGE7Vscu8LG7ex2zW");
            user.setRoles(role);

            List<User> users = new ArrayList<User>();
            users.add(user);
            role.setUsers(users);

            this.roleRepository.saveAndFlush(role);
            this.userRepository.saveAndFlush(user);
        }

        Stream<Role> roleUser = all.stream().filter(r -> r.getName().equals("ROLE_User"));
        if (!roleUser.findAny().isPresent()){
            Role role = new Role();
            role.setName("ROLE_User");

            this.roleRepository.saveAndFlush(role);
        }

    }
}
