package com.javaEcommerce.myapp;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.sql.SQLException;

import com.javaEcommerce.myapp.dao.ArticleDao;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	
	@Autowired
	private ArticleDao articleDoa;
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		model.addAttribute("articles", articleDoa.getAllArticles());
		
		
		
//		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
//		dataSource.setDriverClass(org.h2.Driver.class);
//		dataSource.setUsername("team130");
//		dataSource.setUrl("jdbc:mysql://stusql.dcs.shef.ac.uk");
//		dataSource.setPassword("60dd53ae");
//		
//		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
//		List<Article> results = (List<Article>)jdbcTemplate.query("SELECT * FROM ARTICLE", 
//				new RowMapper(){
//					@Override
//					public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
//						return new Article(rs.getInt("id"), rs.getInt("main_author_id"), rs.getInt("status"), rs.getInt("volume"), rs.getInt("edition"));
//					}
//				});
		
//		model.addAttribute("articles", results);
		
		
//		Date date = new Date();
//		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
//		
//		String formattedDate = dateFormat.format(date);
//		
//		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
}
