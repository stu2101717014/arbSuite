package com.example.ui.security.services;

import com.example.ui.security.data.Role;
import com.example.ui.security.data.RoleRepository;
import com.example.ui.security.data.User;
import com.example.ui.security.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepo;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final RoleRepository roleRepository;

    private static final String ROLE_USER = "ROLE_User";

    private static final String ROLE_ADMIN = "ROLE_Admin";

    @Autowired
    public UserServiceImpl(UserRepository userRepo,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           RoleRepository roleRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepo = userRepo;
        this.roleRepository = roleRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> opt = userRepo.findUserByEmail(email);

        org.springframework.security.core.userdetails.User springUser = null;

        if(!opt.isPresent()) {
            throw new UsernameNotFoundException("User with email: " +email +" not found");
        }else {
            User user = opt.get();

            List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
            Set<GrantedAuthority> ga = new HashSet<>();
            for(String role:roles) {
                ga.add(new SimpleGrantedAuthority(role));
            }

            springUser = new org.springframework.security.core.userdetails.User(
                    email,
                    user.getPassword(),
                    ga );

        }

        return springUser;
    }

    @Override
    public User setDefaultRole(User user){
        List<Role> all = this.roleRepository.findAll();
        Stream<Role> roleStream = all.stream().filter(role -> role.getName().equals(ROLE_USER));
        Role role = roleStream.findFirst().get();
        user.setRoles(role);
        return user;
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return this.userRepo.saveAndFlush(user);
    }

}