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
import com.jg.Controller.DocumentTemplateController;
import com.jg.Controller.EditionController;
import com.jg.Controller.GlobalController;
import com.jg.Controller.LettersToEditorsController;
import com.jg.Controller.Controller.entryResponse;
import com.jg.Controller.RoleController;
import com.jg.Controller.UserController;
import com.jg.Controller.VolumeController;
import com.jg.Model.Article;
import com.jg.Model.Edition;
import com.jg.Model.Global;
import com.jg.Model.LetterToEditor;
import com.jg.Model.Role;
import com.jg.Model.Template;
import com.jg.Model.User;
import com.jg.Model.Volume;

/**
 * @author aca10mg@shef.ac.uk (Mike Goddard)
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4.class)
public class DocumentTemplateTests {
	private static int id;
	private final String name = "Test Template Name";
	private final String description = "Test template description";
	private final String format = "pdf";
	private final String url = "path/to/file.pdf";
	private final String name2 = "Test Template Name 2";
	private final String description2 = "Test template description 2";
	private final String format2 = "docx";
	private final String url2 = "path/to/file2.docx";
	
	private DocumentTemplateController DTController;
	
	@Before
	public void setUp() {
		DTController = new DocumentTemplateController();
		DTController.startSession();
	}
	
	@After
	public void cleanUp() {
		if (DTController.isSessionReady())
			DTController.endSession();
	}
	
	// create template
	@Test
	public void a_createTemplate() {
		assertEquals("Create template - DB Error", Controller.entryResponse.SUCCESS, DTController.create(name, description, format, url));
	}
	
	// get all templates
	@Test
	public void b_getAllTemplates() {
		List<Template> templates = DTController.getAllTemplates();
		assertNotNull("Get all templates - null", templates);
		Template template = null;
		for (int i = 0; i < templates.size(); i++) {
			if (templates.get(i).getName().equals(name)) {
				if (templates.get(i).getDescription().equals(description)) {
					if (templates.get(i).getFormat().equals(format)) {
						if (templates.get(i).getUrl().equals(url)) {
							template = templates.get(i);
							id = template.getId();
						}
					}
				}
			}
		}
		assertNotNull("Get all templates - template not found", template);
	}
	
	// get by id
	@Test
	public void c_getById() {
		Template template = DTController.get(id);
		assertNotNull("Get template by id - template not found", template);
	}
	
	// update template
	@Test
	public void d_updateTemplate() {
		assertEquals("Update template - DB Error", Controller.entryResponse.SUCCESS, DTController.update(id, name2, description2, format2, url2));
		Template template = DTController.get(id);
		assertNotNull("Update template - template null", template);
		assertEquals("Update template - persistant storage failed - name", name2, template.getName());
		assertEquals("Update template - persistant storage failed - description", description2, template.getDescription());
		assertEquals("Update template - persistant storage failed - format2", format2, template.getFormat());
		assertEquals("Update template - persistant storage failed - url", url2, template.getUrl());
	}
}
