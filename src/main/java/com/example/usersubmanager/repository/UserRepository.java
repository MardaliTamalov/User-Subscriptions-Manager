package com.example.usersubmanager.repository;

import com.example.usersubmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

}
