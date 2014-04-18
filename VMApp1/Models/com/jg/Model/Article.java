package com.jg.Model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="articles")
public class Article {

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMain_author_id() {
		return main_author_id;
	}
	public void setMain_author_id(int main_author_id) {
		this.main_author_id = main_author_id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getVolume_id() {
		return volume_id;
	}
	public void setVolume_id(int volume_id) {
		this.volume_id = volume_id;
	}
	public int getEdition_id() {
		return edition_id;
	}
	public void setEdition_id(int edition_id) {
		this.edition_id = edition_id;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	@Id @GeneratedValue
	private int id;
	private int main_author_id;
	private int status;
	private int volume_id;
	private int edition_id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at;

}