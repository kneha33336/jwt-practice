package com.jwtpractice.jwt.repository;

import com.jwtpractice.jwt.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Id;

public interface UserRepository extends JpaRepository<UserData, Id> {
}
