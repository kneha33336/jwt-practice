package com.jwtpractice.jwt.controllers;

import com.jwtpractice.jwt.cache.GuavaCache;
import com.jwtpractice.jwt.entity.UserData;
import com.jwtpractice.jwt.models.JwtRequest;
import com.jwtpractice.jwt.models.JwtResponse;
import com.jwtpractice.jwt.repository.UserRepository;
import com.jwtpractice.jwt.services.CustomUserDetailsService;
import com.jwtpractice.jwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class JwtController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    GuavaCache cache;

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody JwtRequest jwtRequest) throws Exception {
        System.out.println(jwtRequest);
        try{
            System.out.println("inside try");
            //authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),
            //      jwtRequest.getPassword()));
        }catch(Exception exception){
            System.out.println("Inside catch");
            exception.printStackTrace();
            throw new Exception("Bad Credentials");
        }
        System.out.println("try successfull");
        UserDetails userDetails = this.customUserDetailsService
                .loadUserByUsername(jwtRequest.getUsername());

        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody UserData user){
        System.out.println(user);
        if(user != null && user.getUserName() != null && !user.getUserName().equals("")
                && user.getPassword() != null && !user.getPassword().equals("")
                && user.getEmailId() != null && !user.getEmailId().equals("")){
            return ResponseEntity.ok(userRepository.save(user));
        }
        System.out.println("Bad request");
        return (ResponseEntity<?>) ResponseEntity.badRequest();
    }

    @GetMapping("/home")
    public String home(){
        System.out.println("Inside home");
        return "Welcome to jwt-practice.";
    }

    @GetMapping("/user/userId/{userId}")
    public ResponseEntity<?> getUserByUserId(@RequestParam String userId) throws Exception {
        if(userId == null || userId.equals("")){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Long id = Long.parseLong(userId);
        Optional<UserData> userData = cache.findByUserId(id);
        if(userData != null)
            return ResponseEntity.ok(userData.get());
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/user/userName/{userName}")
    public ResponseEntity<?> getUserByUserName(@RequestParam String userName) throws Exception {
        if(userName == null || userName.equals("")){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<UserData> userData = cache.findByUserName(userName);
        if(userData != null)
            return new ResponseEntity<>(userData, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
