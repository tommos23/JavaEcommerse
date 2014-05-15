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

import com.jg.Controller.RoleController;
import com.jg.Model.Role;

/**
 * @author aca10mg@shef.ac.uk (Mike Goddard)
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4.class)
public class RoleTests {
	private static int id;
	private static String name;
	
	private RoleController roleController;
	
	@Before
	public void setUp() {
		roleController = new RoleController();
		roleController.startSession();
	}
	
	@After
	public void cleanUp() {
		if (roleController.isSessionReady())
			roleController.endSession();
	}
	
	// get roles
	@Test
	public void a_getRoles() {
		List<Role> roles = roleController.getRoles();
		assertNotNull("Get roles - null", roles);
		if (roles.size() > 0) {
			Role role = roles.get(0);
			id = role.getId();
			name = role.getName();
		}
	}
	
	// get role (id)
	@Test
	public void b_getRoleById() {
		Role role = roleController.get(id);
		assertNotNull("Get role by id - null", role);
		assertEquals("Get role by id - name does not match", name, role.getName());
	}
	
	// get role (name)
	@Test
	public void c_getRoleByName() {
		Role role = roleController.get(name);
		assertNotNull("Get role by name - null", role);
		assertEquals("Get role by name - id does not match", id, role.getId());
	}
	
}
