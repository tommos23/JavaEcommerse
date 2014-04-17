package com.jg.Model;

import javax.persistence.*;

@Table(name="roles")
public class Role {
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public boolean isCan_read_published() {
		return can_read_published;
	}
	public boolean isCan_read_unpublished() {
		return can_read_unpublished;
	}
	public boolean isCan_write() {
		return can_write;
	}
	public boolean isCan_review() {
		return can_review;
	}
	public boolean isCan_edit() {
		return can_edit;
	}
	public boolean isCan_publish() {
		return can_publish;
	}
	
	@Id @GeneratedValue
	int id;
	String name;
	boolean can_read_published;
	boolean can_read_unpublished;
	boolean can_write;
	boolean can_review;
	boolean can_edit;
	boolean can_publish;
}
