package taskman.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.PlanningService;
import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.model.project.task.Task;
import taskman.model.resource.Resource;
import taskman.model.resource.ResourceType;
import taskman.model.time.Clock;
import taskman.model.time.TimeSpan;

public class PlanningServiceTest {
	ProjectHandler phtest;
	ProjectHandler ph;
	ResourceHandler rh;
	PlanningService planning;
	DateTime startTime;
	TimeSpan firstTimeSpan;
	List<ResourceType> requires = new ArrayList<ResourceType>();
	List<ResourceType> conflictsWith = new ArrayList<ResourceType>();

	@Before
	public void setUp() throws Exception {
		ph = new ProjectHandler();
		rh = new ResourceHandler();
		planning = new PlanningService();

		startTime = new DateTime(2015, 1, 1, 13, 0);

		ph.addProject("PlanningService", "Test", startTime, new DateTime(2015,
				2, 1, 17, 0));

		ph.getProjects()
				.get(0)
				.addTask("PlanningServiceTest no requirements", 60, 0, null,
						null, null);

		rh.addResourceType("Car", requires, conflictsWith, false);
		rh.getResourceTypes()
				.get(0)
				.addResource("Car 1", new LocalTime(12, 0),
						new LocalTime(15, 0));

		rh.addResourceType("Server", requires, conflictsWith, false);
		rh.getResourceTypes()
				.get(1)
				.addResource("Server 1", new LocalTime(12, 0),
						new LocalTime(16, 0));
		rh.getResourceTypes()
				.get(1)
				.addResource("Server 2", new LocalTime(12, 0),
						new LocalTime(16, 0));

		LinkedHashMap<ResourceType, Integer> lhm1 = new LinkedHashMap<ResourceType, Integer>();
		LinkedHashMap<ResourceType, Integer> lhm2 = new LinkedHashMap<ResourceType, Integer>();
		LinkedHashMap<ResourceType, Integer> lhm3 = new LinkedHashMap<ResourceType, Integer>();
		lhm1.put(rh.getResourceTypes().get(0), 1);
		lhm2.put(rh.getResourceTypes().get(1), 1);
		lhm3.put(rh.getResourceTypes().get(1), 1);
		ph.getProjects()
				.get(0)
				.addTask("PlanningServiceTest requirements 1", 60, 0, null,
						null, lhm1);

		ph.getProjects()
				.get(0)
				.addTask("PlanningServiceTest requirements 2", 60, 0, null,
						null, lhm2);

		firstTimeSpan = new TimeSpan(startTime, startTime.plusMinutes(ph
				.getProjects().get(0).getTasks().get(2).getEstimatedDuration()));
		System.out.println(startTime);
		System.out.println(startTime.plusMinutes(ph.getProjects().get(0)
				.getTasks().get(2).getEstimatedDuration()));
		for (Resource resource : rh.getResourceTypes().get(1)
				.getSuggestedResources(firstTimeSpan, 2)) {
			resource.addReservation(ph.getProjects().get(0).getTasks().get(2),
					firstTimeSpan);
		}

		ph.getProjects()
				.get(0)
				.addTask("PlanningServiceTest requirements 3", 60, 0, null,
						null, lhm3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetPossibleStartTimesNullTask() {
		planning.getPossibleStartTimes(null, 3, new DateTime());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetPossibleStartTimesNegativeAmount() {
		Task task = ph.getProjects().get(0).getTasks().get(0);
		planning.getPossibleStartTimes(task, -3, new DateTime());
	}

	@Test
	public void testGetPossibleStartTimes() {
		int amount = 3;
		Task task = ph.getProjects().get(0).getTasks().get(0);
		Set<DateTime> expectedStartTimes = new TreeSet<DateTime>();
		Clock systemClock = Clock.getInstance();
		DateTime systemTime = new DateTime(2015, 1, 1, 8, 0);
		systemClock.setSystemTime(systemTime);
		for (int i = 0; i < amount; i++) {
			expectedStartTimes.add(new DateTime(2015, 1, 1, 8 + i, 0));
		}
		Set<DateTime> startTimes = planning.getPossibleStartTimes(task, amount,
				null);
		assertEquals(expectedStartTimes, startTimes);
	}

//	@Test
//	public void testGetPossibleStartTimesAfter20150115() {
//		int amount = 3;
//		Task task = ph.getProjects().get(0).getTasks().get(0);
//		Set<DateTime> expectedStartTimes = new TreeSet<DateTime>();
//		for (int i = 0; i < amount; i++) {
//			expectedStartTimes.add(new DateTime(2015, 1, 15, 10 + i, 0));
//		}
//		Set<DateTime> startTimes = planning.getPossibleStartTimes(task, amount,
//				new DateTime(2015, 1, 15, 10, 0));
//		//assertEquals(expectedStartTimes, startTimes);
//	}

	// @Test(expected = IllegalStateException.class)
	// public void testGetPossibleStartTimesNotEnoughResources() {
	// int amount = 3;
	// Task task = ph.getProjects().get(0).getTasks().get(1);
	// planning.getPossibleStartTimes(task, amount, null);
	// }

	@Test(expected = IllegalArgumentException.class)
	public void testIsValidTimeSpanNullTask() {
		TimeSpan timeSpan = new TimeSpan(startTime, startTime.plusMinutes(10));
		planning.isValidTimeSpan(null, timeSpan, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIsValidTimeSpanNullTimeSpan() {
		Task task = ph.getProjects().get(0).getTasks().get(0);
		planning.isValidTimeSpan(task, null, null);
	}

//	@Test
//	public void testIsValidTimeSpan() {
//		Task task = ph.getProjects().get(0).getTasks().get(0);
//		TimeSpan timeSpan = new TimeSpan(startTime, startTime.plusMinutes(task
//				.getEstimatedDuration()));
//		assertTrue(planning.isValidTimeSpan(task, timeSpan, null));
//	}

	@Test
	public void testIsValidTimeSpanAfter20150115() {
		Task task = ph.getProjects().get(0).getTasks().get(0);
		TimeSpan timeSpan = new TimeSpan(startTime, startTime.plusMinutes(task
				.getEstimatedDuration()));
		assertFalse(planning.isValidTimeSpan(task, timeSpan, new DateTime(2015,
				1, 15, 10, 0)));
		startTime = new DateTime(2015, 1, 15, 16, 0);
		timeSpan = new TimeSpan(startTime, startTime.plusMinutes(task
				.getEstimatedDuration()));
		assertTrue(planning.isValidTimeSpan(task, timeSpan, new DateTime(2015,
				1, 15, 10, 0)));
	}

	// @Test
	// public void testIsValidTimeSpanNotEnoughResources() {
	// Task task = ph.getProjects().get(0).getTasks().get(1);
	// TimeSpan timeSpan = new TimeSpan(startTime,
	// startTime.plusMinutes(task.getEstimatedDuration()));
	// assertFalse(planning.isValidTimeSpan(task, timeSpan, null));
	// }
	//
	// @Test
	// public void testIsValidTimeSpanResourceReserverd() {
	// Task task4 = ph.getProjects().get(0).getTasks().get(3);
	// TimeSpan timeSpan = new TimeSpan(startTime,
	// startTime.plusMinutes(task4.getEstimatedDuration()));
	// assertFalse(planning.isValidTimeSpan(task4, timeSpan, null));
	// }
	//
	@Test
	public void testIsValidTimeSpanResourceFree() {
		Task task3 = ph.getProjects().get(0).getTasks().get(2);
		Task task4 = ph.getProjects().get(0).getTasks().get(3);
		DateTime newStartTime = startTime.plusMinutes(task3
				.getEstimatedDuration() + 1);
		TimeSpan timeSpan = new TimeSpan(newStartTime,
				newStartTime.plusMinutes(task4.getEstimatedDuration()));
		assertTrue(planning.isValidTimeSpan(task4, timeSpan, null));
	}
}
