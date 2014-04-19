package com.jg.Model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name="subjects")
public class Subject {
	
	public Subject(){
		super();
	}
	public Subject(String title){
		setTitle(title);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Set<Version> getVersions() {
		return versions;
	}
	public void setVersions(Set<Version> versions) {
		this.versions = versions;
	}
	@Id @GeneratedValue
	private int id;

	@Column(nullable=false,unique=true)
	private String title;

	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "subjects")
	private Set<Version> versions = new HashSet<Version>(0);

}