package com.jg.Tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.jg.Controller.Controller;
import com.jg.Controller.GlobalController;
import com.jg.Model.Global;

/**
 * Tests for {@link Foo}.
 *
 * @author user@example.com (John Doe)
 */
@RunWith(JUnit4.class)
public class GlobalTests {
	private final String name = "Test Global Name";
	private final String goals = "Test global goals";
	private final String guidelines = "Test global guidelines";
	
	private GlobalController globalController;
	
	@Before
	public void setUp() {
		globalController = new GlobalController();
		globalController.startSession();
	}
	
	@After
	public void cleanUp() {
		if (globalController.isSessionReady())
			globalController.endSession();
	}

    @Test
    public void getGlobal() {
    	Global global = globalController.getGlobal();
    	assertNotNull("Global object is null", global);
    }
    
    @Test
    public void updateGlobal() {
    	Global global = globalController.getGlobal();
    	assertEquals("Call update not successful", Controller.entryResponse.SUCCESS, globalController.update(global.getId(), name, goals, guidelines));
    }
    
    @Test
    public void globalUpdateComplete() {
    	Global global = globalController.getGlobal();
    	assertTrue("Persistant storage unsuccessful - name", global.getName().equals(name));
    	assertTrue("Persistant storage unsuccessful - goals", global.getGoals().equals(goals));
    	assertTrue("Persistant storage unsuccessful - guidelines", global.getSubmission_guidelines().equals(guidelines));
    }
}
