package com.jg.Model;

import javax.persistence.*;

import org.hibernate.annotations.Type;

@Entity
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
	@Column(length = 50)
	String name;
	@Type(type="true_false")
	private boolean can_read_published;
	@Type(type="true_false")
	private boolean can_read_unpublished;
	@Type(type="true_false")
	private boolean can_write;
	@Type(type="true_false")
	private boolean can_review;
	@Type(type="true_false")
	private boolean can_edit;
	@Type(type="true_false")
	private boolean can_publish;
}
