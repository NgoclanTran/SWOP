package taskman.tests;

import static org.junit.Assert.*;

import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import taskman.exceptions.IllegalTimeException;
import taskman.model.user.Developer;
import taskman.model.user.ProjectManager;

public class ProjectManagerTest {

	private String name;
	private LocalTime startTime;
	private LocalTime endTime;

	@Before
	public void setUp() throws Exception {
		name = "name";
		startTime = new LocalTime(10, 0);
		endTime = new LocalTime(16, 0);
	}

	@Test
	public void constructorTest_success() {
		ProjectManager pm = new ProjectManager(name);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorTest_nullName() {
		ProjectManager pm = new ProjectManager(null);
	}
	@Test
	public void isDeveloperTest(){
		ProjectManager pm = new ProjectManager(name);
		assertFalse(pm.isDeveloper());
	}
	@Test
	public void isProjectmanagerTest(){
		ProjectManager pm = new ProjectManager(name);
		assertTrue(pm.isProjectManager());
	}
	@Test
	public void getNameTest(){
		ProjectManager pm = new ProjectManager(name);
		assertEquals(pm.getName(), name);
	}
	
	@Test
	public void toStringTest(){
		ProjectManager pm = new ProjectManager(name);
		assertEquals(pm.toString(), name);
	}
}
