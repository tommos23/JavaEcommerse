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
public class EditionTests {
	private static int id;
	private static int volume_id;
	private final String description = "Test Edition";
	private final String description2 = "Test Edition 2";
	
	private EditionController editionController;
	
	@Before
	public void setUp() {
		editionController = new EditionController();
		editionController.startSession();
	}
	
	@After
	public void cleanUp() {
		if (editionController.isSessionReady())
			editionController.endSession();
	}
	
	// Create
	@Test
	public void a_createEdition() {
		VolumeController vc = new VolumeController();
		vc.startSession();
		List<Volume> volumes = vc.getAllVolumes();
		if (vc.isSessionReady())
			vc.endSession();
		if (volumes.size() > 0) {
			Volume volume = volumes.get(0);
			volume_id = volume.getId();
			assertEquals("Create edition - DB Error", Controller.entryResponse.SUCCESS, editionController.create(description, volume));
		}
	}
	
	// Get (description, volume)
	@Test
	public void b_getEditionByDescriptionVolume() {
		VolumeController vc = new VolumeController();
		vc.startSession();
		Volume volume = vc.get(volume_id);
		if (vc.isSessionReady())
			vc.endSession();
		Edition edition = editionController.get(description, volume);
		assertNotNull("Get (description, volume) - retrieval error - null", edition);
		assertEquals("Get (description, volume) - retrieval error - description", description, edition.getDescription());
		id = edition.getId();
	}
	
	// Get (id)
	@Test
	public void c_getById() {
		Edition edition = editionController.get(id);
		assertNotNull("Get (id) - retrieval error - null", edition);
		assertEquals("Get (id) - retrieval error - description", description, edition.getDescription());
	}
	
	// Get editions for volume
	@Test
	public void d_getEditionsForVolume() {
		List<Edition> editions = editionController.getEditionsForVolume(volume_id);
		assertNotNull("Get (id) - retrieval error - null editions for volume", editions);
		Edition edition = null;
		for (int i = 0; i < editions.size(); i++) {
			if (editions.get(i).getId() == id) {
				edition = editions.get(i);
			}
		}
		assertNotNull("Get (id) - retrieval error - created edition not present", edition);
	}
	
	// Publish
	@Test
	public void e_publishEdition() {
		assertEquals("Publish - DB Error", Controller.entryResponse.SUCCESS, editionController.publish(id, 1));
		Edition edition = editionController.get(id);
		assertEquals("Publish - persistant storage failure", 1, edition.getStatus());
	}
	
	// Unpublish
	@Test
	public void f_unpublishEdition() {
		
		assertEquals("Unpublish - DB Error", Controller.entryResponse.SUCCESS, editionController.publish(id, 0));
		Edition edition = editionController.get(id);
		assertEquals("Unpublish - persistant storage failure", 0, edition.getStatus());
	}
	
	// Update
	@Test
	public void g_updateEdition() {
		VolumeController vc = new VolumeController();
		vc.startSession();
		Volume volume = vc.get(volume_id);
		if (vc.isSessionReady())
			vc.endSession();
		assertEquals("Update edition - DB Error", Controller.entryResponse.SUCCESS, editionController.update(id, 1, description2, volume));
		Edition edition = editionController.get(id);
		assertEquals("Update edition - persistant storage failure - status", 1, edition.getStatus());
		assertEquals("Update edition - persistant storage failure - description", description2, edition.getDescription());
	}
}
