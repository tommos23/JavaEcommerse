package com.jg.Model;

import javax.persistence.*;

@Table(name="subjects")
public class Subject {
	public int getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	
	@Id @GeneratedValue
	private int id;
	private String title;
}