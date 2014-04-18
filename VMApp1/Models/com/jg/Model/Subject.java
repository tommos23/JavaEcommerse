package com.jg.Model;

import javax.persistence.*;

@Entity
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
	@Column(columnDefinition = "TEXT")
	private String title;
}