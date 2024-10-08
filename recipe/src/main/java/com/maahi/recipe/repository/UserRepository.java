package com.maahi.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maahi.recipe.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User findByUsername(String username);
}
