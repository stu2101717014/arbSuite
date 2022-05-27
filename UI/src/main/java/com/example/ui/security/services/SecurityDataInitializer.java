package com.example.ui.security.services;

import com.example.ui.security.data.Role;
import com.example.ui.security.data.RoleRepository;
import com.example.ui.security.data.User;
import com.example.ui.security.data.UserRepository;
import com.example.ui.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
public class SecurityDataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public SecurityDataInitializer(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Role> all = this.roleRepository.findAll();
        createAdminIfNotExists(all);
        //createRoleUserIfNotExists(all);
    }

    private void createRoleUserIfNotExists(List<Role> all) {
        Stream<Role> roleUser = all.stream().filter(r -> r.getName().equals(SecurityUtils.ROLE_USER));
        if (!roleUser.findAny().isPresent()){
            Role role = new Role();
            role.setName(SecurityUtils.ROLE_USER);

            this.roleRepository.saveAndFlush(role);
        }
    }

    private void createAdminIfNotExists(List<Role> all) {
        Stream<Role> roleAdmin = all.stream().filter(r -> r.getName().equals(SecurityUtils.ROLE_ADMIN));
        if (!roleAdmin.findAny().isPresent()){
            Role role = new Role();
            role.setName(SecurityUtils.ROLE_ADMIN);

            Role roleUser = new Role();
            roleUser.setName(SecurityUtils.ROLE_USER);

            User user = new User();
            user.setName("admin");
            user.setEmail("admin@test.ts");
            user.setPassword(this.bCryptPasswordEncoder.encode("admin"));
            user.setRoles(role, roleUser);

            List<User> users = new ArrayList<User>();
            users.add(user);
            role.setUsers(users);
            roleUser.setUsers(users);

            this.roleRepository.saveAndFlush(role);
            this.roleRepository.saveAndFlush(roleUser);
            this.userRepository.saveAndFlush(user);
        }
    }
}
