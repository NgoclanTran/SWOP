package taskman.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.PlanningService;
import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.model.project.task.Task;
import taskman.model.time.TimeSpan;

public class PlanningServiceTest {
	
	ProjectHandler ph;
	ResourceHandler rh;
	PlanningService planning;
	
	@Before
	public void setUp() throws Exception {
		ph = new ProjectHandler();
		rh = new ResourceHandler();
		planning = new PlanningService(rh);
		
		ph.addProject("PlanningService", "Test", new DateTime(2015,1,1,8,0), new DateTime(2015,2,1,17,0));
		ph.getProjects().get(0).addTask("PlanningServiceTest", 60, 0, null, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPlanningServiceNullResourceHandler() {
		new PlanningService(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetPossibleStartTimesNullTask() {
		planning.getPossibleStartTimes(null, 3, new DateTime());
	}

	@Test
	public void testGetPossibleStartTimes() {
		int amount = 3;
		Task task = ph.getProjects().get(0).getTasks().get(0);
		Set<DateTime> expectedStartTimes = new HashSet<DateTime>();
		for(int i = 0; i < amount; i++) {
			expectedStartTimes.add(new DateTime(2015,1,1,8 + i,0));
		}
		Set<DateTime> startTimes = planning.getPossibleStartTimes(task, amount, null);
		assertEquals(expectedStartTimes, startTimes);
	}
	
	@Test
	public void testGetPossibleStartTimesAfter20150115() {
		int amount = 3;
		Task task = ph.getProjects().get(0).getTasks().get(0);
		Set<DateTime> expectedStartTimes = new HashSet<DateTime>();
		for(int i = 0; i < amount; i++) {
			expectedStartTimes.add(new DateTime(2015,1,15,10 + i,0));
		}
		Set<DateTime> startTimes = planning.getPossibleStartTimes(task, amount, new DateTime(2015,1,15,10,0));
		assertEquals(expectedStartTimes, startTimes);
	}

	@Test
	public void testIsValidTimeSpan() {
		Task task = ph.getProjects().get(0).getTasks().get(0);
		DateTime startTime = new DateTime(2015,1,1,8,0);
		TimeSpan timeSpan = new TimeSpan(startTime, startTime.plusMinutes(task.getEstimatedDuration()));
		assertTrue(planning.isValidTimeSpan(task, timeSpan, null));
	}
	
	@Test
	public void testIsValidTimeSpanAfter20150115() {
		Task task = ph.getProjects().get(0).getTasks().get(0);
		DateTime startTime = new DateTime(2015,1,1,8,0);
		TimeSpan timeSpan = new TimeSpan(startTime, startTime.plusMinutes(task.getEstimatedDuration()));
		assertFalse(planning.isValidTimeSpan(task, timeSpan, new DateTime(2015,1,15,10,0)));
		startTime = new DateTime(2015,1,15,16,0);
		timeSpan = new TimeSpan(startTime, startTime.plusMinutes(task.getEstimatedDuration()));
		assertTrue(planning.isValidTimeSpan(task, timeSpan, new DateTime(2015,1,15,10,0)));
	}

}
