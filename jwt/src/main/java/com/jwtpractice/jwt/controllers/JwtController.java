package com.jwtpractice.jwt.controllers;

import com.jwtpractice.jwt.models.JwtRequest;
import com.jwtpractice.jwt.models.JwtResponse;
import com.jwtpractice.jwt.services.CustomUserDetailsService;
import com.jwtpractice.jwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class JwtController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;
    @RequestMapping(value="/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody JwtRequest jwtRequest) throws Exception {
        System.out.println(jwtRequest);
        try{
            System.out.println("inside try");
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),
                    jwtRequest.getPassword()));
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

    @GetMapping("/home")
    public String home(){
        System.out.println("Inside home");
        return "Welcome to jwt-practice.";
    }
}
