package com.jwtpractice.jwt.services;


import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        if(userName.equals(("Neha"))){
            System.out.println("Inside CustomUserDetailsService");
            return new User("Neha", "neha@123", new ArrayList<>());
        }else{
            throw new UsernameNotFoundException("User not registered.");
        }
    }
}
