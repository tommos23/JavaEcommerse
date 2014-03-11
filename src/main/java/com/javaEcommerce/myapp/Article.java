package com.javaEcommerce.myapp;

import javax.persistence.*;

@Entity
@Table(name="ARTICLE")
public class Article {
	@Id
	@Column(name="id")
	@GeneratedValue
	private int id;
	
	@Column(name="main_author_id")
	private int main_author_id;
	
	@Column(name="status")
	private int status;
	
	@Column(name="volume")
	private int volume;
	
	@Column(name="edition")
	private int edition;
	
	public Article() {}
	
	public Article(int id, int main_author_id, int status, int volume, int edition) {
		this.id = id;
		this.main_author_id = main_author_id;
		this.status = status;
		this.volume = volume;
		this.edition = edition;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getMainAuthor() {
		return this.main_author_id;
	}
	
	public void setMainAuthor(int main_author_id) {
		this.main_author_id = main_author_id;
	}
	
	public int getStatus() {
		return this.status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getVolume() {
		return this.volume;
	}
	
	public void setVolume(int volume) {
		this.volume = volume;
	}
	
	public int getEdition() {
		return this.edition;
	}
	
	public void setEdition(int edition) {
		this.edition = edition;
	}
	
}
