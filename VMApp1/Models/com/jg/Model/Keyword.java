package com.jg.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="keywords")
public class Keyword {
	
	public Keyword(){
		super();
	}
	public Keyword(String word){
		setKeyword(word);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	@Id @GeneratedValue
	private int id;
	@Column(nullable = false, unique=true)
	private String keyword;	
}
