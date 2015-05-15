package taskman.tests;

import static org.junit.Assert.*;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.UserHandler;
import taskman.model.project.task.Task;
import taskman.model.user.*;
import taskman.model.time.*;

public class UserHandlerTest {

	private UserHandler u;
	private Clock clock = new Clock();
	@Before
	public void setUp() throws Exception {
		u = new UserHandler();
	}

	@Test
	public void getDevelopersTest(){
		List<Developer> d = u.getDevelopers();
		Developer developer = new Developer("name", new LocalTime(10,10), new LocalTime(11,10));
		d.add(developer);
		assertFalse(u.getDevelopers().contains(developer));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void addDeveloperTest_Name_Null(){
		u.addDeveloper(null);
	}
	
	@Test
	public void addDeveloperTest_TrueCase(){
		assertTrue(u.getDevelopers().isEmpty());
		u.addDeveloper("name");
		assertEquals(u.getDevelopers().get(0).getDailyAvailability().getStartTime().getHourOfDay(),8);
		assertEquals(u.getDevelopers().get(0).getDailyAvailability().getStartTime().getMinuteOfHour(),0);

		assertEquals(u.getDevelopers().get(0).getDailyAvailability().getEndTime().getHourOfDay(),17);
		assertEquals(u.getDevelopers().get(0).getDailyAvailability().getEndTime().getMinuteOfHour(),0);

	}
	
	@Test (expected = IllegalArgumentException.class)
	public void getAvailableDevelopersTest_TimeSpan_Null(){
		u.getAvailableDevelopers(null);
	}
	
	@Test
	public void getAvailableDevelopersTest_TrueCase(){
		u.addDeveloper("name1");
		u.addDeveloper("name2");
		// --------------------- Developers don't have planning --------------------
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015,10,12,10, 0), new DateTime(2015,10,12,11,0));
		assertEquals(u.getAvailableDevelopers(timeSpan).size(),2);
		
		Task task = new Task(clock,"description", 10, 1, null, null, null);
		
		Developer d = u.getDevelopers().get(0);
		d.addReservation(task, timeSpan);
		
		// ----------------------- One developer has a planning ----------------
		assertEquals(u.getAvailableDevelopers(timeSpan).size(),1);
		
		
	}

}
