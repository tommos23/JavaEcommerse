package com.javaEcommerce.myapp.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javaEcommerce.myapp.*;
 
@Repository
public class ArticleDaoImpl implements ArticleDao {
 
    @Autowired
    private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Article> getAllArticles() {
		return this.sessionFactory.getCurrentSession().createQuery("from ARTICLE").list();
	}
}
