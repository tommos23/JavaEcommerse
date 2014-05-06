package com.jg.Tests;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import com.jg.Controller.ArticleController;
import com.jg.Controller.Controller;
import com.jg.Controller.EditionController;
import com.jg.Controller.GlobalController;
import com.jg.Controller.LettersToEditorsController;
import com.jg.Controller.Controller.entryResponse;
import com.jg.Controller.UserController;
import com.jg.Controller.VolumeController;
import com.jg.Model.Article;
import com.jg.Model.Edition;
import com.jg.Model.Global;
import com.jg.Model.LetterToEditor;
import com.jg.Model.User;
import com.jg.Model.Volume;

/**
 * @author aca10mg@shef.ac.uk (Mike Goddard)
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4.class)
public class LettersToEditorsTests {
	private final String text = "This is some letter text";
	private final String edited_text = "This is some edited letter text";
	private final String reply_text = "This is some reply text";
	private final Date created_at = new Date();
	private final Date edited_at= new Date();
	private final Date replyed_at = new Date();
	private static int id;
	private int status = 0;
	private int publish_edition;
	
	private LettersToEditorsController lteController;
	
	@Before
	public void setUp() {
		lteController = new LettersToEditorsController();
		lteController.startSession();
	}
	
	@After
	public void cleanUp() {
		if (lteController.isSessionReady())
			lteController.endSession();
	}

	@Test
	public void a_createLetter() {
		ArticleController ac = new ArticleController();
		ac.startSession();
		List<Article> articles = ac.getAllArticles(5);
		if (ac.isSessionReady())
			ac.endSession();
		if (articles.size() > 0) {
			Article article = articles.get(0);
			UserController uc = new UserController();
			uc.startSession();
			List<User> users = uc.getUsers();
			if (uc.isSessionReady())
				uc.endSession();
			if (users.size() > 0) {
				User user = users.get(0);
				assertEquals("Create letter - DB Error", Controller.entryResponse.SUCCESS, lteController.create(article, user, text));
			}
		}
	}
	
    @Test
    public void b_getAllLetters() {
    	List<LetterToEditor> letters = lteController.getAllLettersToEditors(0);
    	assertNotNull("Letters list is null", letters);
    	if (letters.size() > 0) {
    		System.out.println("Letter id: "+letters.get(0).getId());
    		id = letters.get(0).getId();
    	}
    }
    
    @Test
    public void c_getLetter() {
    	System.out.println("Letter id: "+id);
    	LetterToEditor letter = lteController.get(id);
    	System.out.println(letter.toString());
    	assertNotNull("Get letter for confirmed ID - null", letter);
    }

    @Test
    public void d_postLetter() {
    	assertEquals("Post letter unsuccessful - DB Error", Controller.entryResponse.SUCCESS, lteController.postLetter(id, edited_text));
    	LetterToEditor letter = lteController.get(id);
    	assertEquals("Post letter unsuccessful - persistant storage failed (status)", 1, letter.getStatus());
    	assertEquals("Post letter unsuccessful - persistant storage failed (edited text)", edited_text, letter.getEdited_text());
    }
    
    @Test
    public void e_replyToLetter() {
    	assertEquals("Reply to letter unsuccessful - DB Error", Controller.entryResponse.SUCCESS, lteController.replyToLetter(id, reply_text));
    	LetterToEditor letter = lteController.get(id);
    	assertEquals("Reply to letter unsuccessful - persistant storage failed (status)", 2, letter.getStatus());
    	assertEquals("Reply to letter unsuccessful - persistant storage failed (reply text)", reply_text, letter.getReply_text());
    }

    @Test
    public void f_approveLetter() {
    	assertEquals("Approve letter unsuccessful - DB Error", Controller.entryResponse.SUCCESS, lteController.approveLetter(id));
    	LetterToEditor letter = lteController.get(id);
    	assertEquals("Approve letter unsuccessful - persistant storage failed", 3, letter.getStatus());
    }

    @Test
    public void g_publishLetter() {
    	VolumeController vc = new VolumeController();
    	vc.startSession();
    	List<Volume> volumes = vc.getAllVolumes();
    	if (volumes.size() > 0) {
    		Volume volume = volumes.get(0);
        	EditionController ec = new EditionController();
        	ec.startSession();
        	List<Edition> editions = ec.getEditionsForVolume(volume.getId());
        	if (editions.size() > 0) {
        		Edition edition = editions.get(0);
	        	assertEquals("Approve letter unsuccessful - DB Error", Controller.entryResponse.SUCCESS, lteController.publishLetter(id, edition));
	        	LetterToEditor letter = lteController.get(id);
	        	assertEquals("Approve letter unsuccessful - persistant storage failed", 4, letter.getStatus());
        	}
        	if (ec.isSessionReady())
        		ec.endSession();
    	}
    	if (vc.isSessionReady())
    		vc.endSession();
    }
}
