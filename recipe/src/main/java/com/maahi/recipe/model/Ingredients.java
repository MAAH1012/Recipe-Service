package com.maahi.recipe.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ingredients")
public class Ingredients {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

	private String name;
	
	private long number;
	
	 @ManyToOne
	 @JoinColumn(name = "recipe_id")
	 private Recipe recipe;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}
	
	
}
