package com.maahi.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maahi.recipe.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long>{

}
