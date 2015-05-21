package taskman.tests;

import static org.junit.Assert.*;

import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import taskman.exceptions.IllegalTimeException;
import taskman.model.user.Developer;

public class DeveloperTest {

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
		Developer d = new Developer(name, startTime, endTime);
		assertEquals(d.getDailyAvailability().getStartTime(), startTime);
		assertEquals(d.getDailyAvailability().getEndTime(), endTime);
		assertEquals(d.toString(), "name");
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorTest_nullName() {
		Developer d = new Developer(null, startTime, endTime);
	}

	@Test(expected = IllegalTimeException.class)
	public void constructorTest_WrongDate() {
		Developer d = new Developer(name, endTime, startTime);

	}
	@Test
	public void isDeveloperTest(){
		Developer d = new Developer(name, startTime, endTime);
		assertTrue(d.isDeveloper());
	}
	@Test
	public void isProjectmanagerTest(){
		Developer d = new Developer(name, startTime, endTime);
		assertFalse(d.isProjectManager());
	}
	@Test
	public void getNameTest(){
		Developer d = new Developer(name, startTime, endTime);
		assertEquals(d.getName(), name);

	}
}
