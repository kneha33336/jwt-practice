package com.jwtpractice.jwt.repository;

import com.jwtpractice.jwt.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserData, Long> {
}
