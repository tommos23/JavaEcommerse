package com.jg.Model;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.Table;

@Table(name="templates")
public class Template {
	private String name;
	private String description;
	private String format;
	private String url;
	private int id;
}
