package com.example.demo.dto;

import java.util.List;

public class SubmenuResponseDto {
	private String name;
	private List<String> roles;

	// Constructors, Getters, and Setters
	public SubmenuResponseDto(String name, List<String> roles) {
		this.name = name;
		this.roles = roles;
	}

	public String getName() {
		return name;
	}

	public List<String> getRoles() {
		return roles;
	}
}
