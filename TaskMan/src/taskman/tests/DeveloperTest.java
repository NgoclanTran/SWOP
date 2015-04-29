package taskman.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import taskman.exceptions.IllegalTimeException;
import taskman.model.PlanningService;
import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.model.project.task.Task;
import taskman.model.resource.Resource;
import taskman.model.resource.ResourceType;
import taskman.model.time.Clock;
import taskman.model.time.TimeSpan;
import taskman.model.user.Developer;

public class DeveloperTest {
	
	private String name;
	private LocalTime startTime;
	private LocalTime endTime;

	@Before
	public void setUp() throws Exception {
		name = "name";
		startTime = new LocalTime(10,0);
		endTime = new LocalTime(16,0);
	}
	
	@Test
	public void constructorTest_success(){
		Developer d = new Developer(name, startTime, endTime);
		assertEquals(d.getDailyAvailability().getStartTime(), startTime);
		assertEquals(d.getDailyAvailability().getEndTime(), endTime);
		assertEquals(d.toString(), "name");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void constructorTest_nullName(){
		Developer d = new Developer(null, startTime, endTime);
	}
	@Test (expected = IllegalTimeException.class)
	public void constructorTest_WrongDate(){
		Developer d = new Developer(name, endTime, startTime);
		
	}
}
