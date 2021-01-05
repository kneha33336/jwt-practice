package com.jwtpractice.jwt.services;

import com.jwtpractice.jwt.entity.UserData;
import com.jwtpractice.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//import org.springframework.security.core.userdetails.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<com.jwtpractice.jwt.entity.UserData> userList = userRepository.findAll();
        UserDetails userDetails = null;
        for(UserData user : userList){
            System.out.println(user);
        }
        for(UserData user : userList){
            //System.out.println(user);
            if(user.getUserName().equals(username)){
                userDetails = new User(user.getUserName(), user.getPassword(), new ArrayList<>());;
                break;
            }
        }
        return userDetails;
        /*if(userName.equals(("Neha"))){
            System.out.println("Inside CustomUserDetailsService");
            return new User("Neha", "neha@123", new ArrayList<>());
        }else{
            throw new UsernameNotFoundException("User not registered.");
        }*/
    }
}
