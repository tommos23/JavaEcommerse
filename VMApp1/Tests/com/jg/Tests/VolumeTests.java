package com.jg.Tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import com.jg.Controller.Controller;
import com.jg.Controller.VolumeController;
import com.jg.Model.Volume;

/**
 * @author aca10mg@shef.ac.uk (Mike Goddard)
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4.class)
public class VolumeTests {
	private static int id;
	private final String description = "Test Volume";
	private final String description2 = "Test Volume 2";
	
	private VolumeController volumeController;
	
	@Before
	public void setUp() {
		volumeController = new VolumeController();
		volumeController.startSession();
	}
	
	@After
	public void cleanUp() {
		if (volumeController.isSessionReady())
			volumeController.endSession();
	}

	// Test create 
	@Test
	public void a_createVolume() {
		assertEquals("Create volume failed - DB Error", Controller.entryResponse.SUCCESS, volumeController.create(description));
	}
	
	// Test get all
	@Test
	public void b_getAllVolumes() {
		List<Volume> volumes = volumeController.getAllVolumes();
		assertNotNull("Volume retreival failed - all volumes - null", volumes);
		id = 0;
		for (int i = 0; i < volumes.size(); i++) {
			if (volumes.get(i).getDescription().equals(description)) {
				id = volumes.get(i).getId();
			}
		}
		assertFalse("Volume retreival failed - all volumes - created not present", Integer.valueOf(0).equals(Integer.valueOf(id)));
	}
	
	// Test get (id)
	@Test
	public void c_getVolume() {
		Volume volume = volumeController.get(id);
		assertNotNull("Volume retreival failed - get(id) - null", volume);
		assertEquals("Volume retreival failed - get(id) - description not equal", description, volume.getDescription());
	}
	
	// Test get with status (0)
	@Test
	public void d_getWithStatus() {
		List<Volume> volumes = volumeController.getWithStatus(0);
		assertNotNull("Volume retreival failed - getWithStatus(0) - null", volumes);
		Volume volume = null;
		for (int i = 0; i < volumes.size(); i++) {
			if (volumes.get(i).getId() == id) {
				volume = volumes.get(i);
			}
		}
		assertNotNull("Volume retreival failed - getWithStatus(0) - created not present", volume);
	}
	
	// Test publish (1)
	@Test
	public void e_publishVolume() {
		assertEquals("Publish volume failed - publish(1) - DB Error", Controller.entryResponse.SUCCESS, volumeController.publish(id, 1));
		Volume volume = volumeController.get(id);
		assertEquals("Publish volume failed - publish(1) - persistant storage failed", 1, volume.getStatus());
	}
	
	// Test get with status (1)
	@Test
	public void f_getWithStatus() {
		List<Volume> volumes = volumeController.getWithStatus(1);
		assertNotNull("Volume retreival failed - getWithStatus(1) - null", volumes);
		@SuppressWarnings("unused")
		Volume volume = null;
		for (int i = 0; i < volumes.size(); i++) {
			if (volumes.get(i).getId() == id) {
				volume = volumes.get(i);
			}
		}
		assertNotNull("Volume retreival failed - getWithStatus(1) - created not present");
	}
	
	// Test publish (0)
	@Test
	public void g_publishVolume() {
		assertEquals("Publish volume failed - publish(0) - DB Error", Controller.entryResponse.SUCCESS, volumeController.publish(id, 0));
		Volume volume = volumeController.get(id);
		assertEquals("Publish volume failed - publish(0) - persistant storage failed", 0, volume.getStatus());
	}
	
	// Test update
	@Test
	public void h_updateVolume() {
		assertEquals("Update volume - DB Error", Controller.entryResponse.SUCCESS, volumeController.update(id, 1, description2));
		Volume volume = volumeController.get(id);
		assertEquals("Update volume - persistant storage failure - status", 1, volume.getStatus());
		assertEquals("Update volume - persistant storage failure - description", description2, volume.getDescription());
	}
}
