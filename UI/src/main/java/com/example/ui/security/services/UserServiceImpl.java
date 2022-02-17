package com.example.ui.security.services;

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

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> opt = userRepo.findUserByEmail(email);

        org.springframework.security.core.userdetails.User springUser=null;

        if(!opt.isPresent()) {
            throw new UsernameNotFoundException("User with email: " +email +" not found");
        }else {
            User user = opt.get();

            List<String> roles = user.getRoles();
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
    public Integer saveUser(User user) {
        return null;
    }



}