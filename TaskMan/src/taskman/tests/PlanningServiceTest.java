package taskman.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.company.ProjectHandler;
import taskman.model.company.ResourceHandler;
import taskman.model.resource.Resource;
import taskman.model.resource.ResourceType;
import taskman.model.task.NormalTask;
import taskman.model.task.TaskFactory;
import taskman.model.time.Clock;
import taskman.model.time.PlanningService;
import taskman.model.time.TimeSpan;
import taskman.model.user.Developer;

public class PlanningServiceTest {
	ProjectHandler phtest;
	ProjectHandler ph;
	ResourceHandler rh;
	PlanningService planning;
	DateTime startTime;
	TimeSpan firstTimeSpan;
	List<ResourceType> requires = new ArrayList<ResourceType>();
	List<ResourceType> conflictsWith = new ArrayList<ResourceType>();
	private TaskFactory tf;
	private Clock clock;

	@Before
	public void setUp() throws Exception {
		clock = new Clock();
		clock.setSystemTime(new DateTime(2015, 1, 1, 00, 00));
		Company company = new Company();
		tf = new TaskFactory(new BranchOffice(company, "New York",
				new ArrayList<ResourceType>()), clock);
		ph = new ProjectHandler(tf);

		planning = new PlanningService(clock);

		startTime = new DateTime(2015, 1, 1, 13, 0);

		ph.addProject("PlanningService", "Test", startTime, new DateTime(2015,
				2, 1, 17, 0));

		ph.getProjects()
				.get(0)
				.addTask("PlanningServiceTest no requirements", 60, 0, null,
						null, null, 1);

		ResourceType t1 = new ResourceType("Car", requires, conflictsWith,
				false);

		t1.addResource("Car 1", new LocalTime(12, 0), new LocalTime(15, 0));

		ResourceType t2 = new ResourceType("Server", requires, conflictsWith,
				false);
		t2.addResource("Server 1", new LocalTime(12, 0), new LocalTime(16, 0));

		t2.addResource("Server 2", new LocalTime(12, 0), new LocalTime(16, 0));
		ArrayList<ResourceType> list = new ArrayList<ResourceType>();
		list.add(t1);
		list.add(t2);
		rh = new ResourceHandler(list);

		LinkedHashMap<ResourceType, Integer> lhm1 = new LinkedHashMap<ResourceType, Integer>();
		LinkedHashMap<ResourceType, Integer> lhm2 = new LinkedHashMap<ResourceType, Integer>();
		LinkedHashMap<ResourceType, Integer> lhm3 = new LinkedHashMap<ResourceType, Integer>();
		lhm1.put(rh.getResourceTypes().get(0), 1);
		lhm2.put(rh.getResourceTypes().get(1), 1);
		lhm3.put(rh.getResourceTypes().get(1), 1);
		ph.getProjects()
				.get(0)
				.addTask("PlanningServiceTest requirements 1", 60, 0, null,
						null, lhm1, 1);

		ph.getProjects()
				.get(0)
				.addTask("PlanningServiceTest requirements 2", 60, 0, null,
						null, lhm2, 1);

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
						null, lhm3, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetPossibleStartTimesNullTask() {
		planning.getPossibleStartTimes(null, 3, new DateTime());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetPossibleStartTimesNegativeAmount() {
		NormalTask task = ph.getProjects().get(0).getTasks().get(0);
		planning.getPossibleStartTimes(task, -3, new DateTime());
	}

	@Test
	public void testGetPossibleStartTime_EarlestStartTimeBeforeSystem() {
		NormalTask task = ph.getProjects().get(0).getTasks().get(0);
		Set<DateTime> list = planning.getPossibleStartTimes(task, 1,
				new DateTime(200, 1, 1, 8, 0));
		assertTrue(list.contains(new DateTime(2015, 1, 1, 8, 0)));
	}

	@Test
	public void testGetPossibleStartTimes() {
		int amount = 3;
		NormalTask task = ph.getProjects().get(0).getTasks().get(0);
		Set<DateTime> expectedStartTimes = new TreeSet<DateTime>();
		Clock systemClock = new Clock();
		DateTime systemTime = new DateTime(2015, 1, 1, 8, 0);
		systemClock.setSystemTime(systemTime);
		for (int i = 0; i < amount; i++) {
			expectedStartTimes.add(new DateTime(2015, 1, 1, 8 + i, 0));
		}
		Set<DateTime> startTimes = planning.getPossibleStartTimes(task, amount,
				null);
		assertEquals(expectedStartTimes, startTimes);
	}

	// @Test
	// public void testGetPossibleStartTimesAfter20150115() {
	// int amount = 3;
	// Task task = ph.getProjects().get(0).getTasks().get(0);
	// Set<DateTime> expectedStartTimes = new TreeSet<DateTime>();
	// for (int i = 0; i < amount; i++) {
	// expectedStartTimes.add(new DateTime(2015, 1, 15, 10 + i, 0));
	// }
	// Set<DateTime> startTimes = planning.getPossibleStartTimes(task, amount,
	// new DateTime(2015, 1, 15, 10, 0));
	// //assertEquals(expectedStartTimes, startTimes);
	// }

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
		NormalTask task = ph.getProjects().get(0).getTasks().get(0);
		planning.isValidTimeSpan(task, null, null);
	}

	// @Test
	// public void testIsValidTimeSpan() {
	// Task task = ph.getProjects().get(0).getTasks().get(0);
	// TimeSpan timeSpan = new TimeSpan(startTime, startTime.plusMinutes(task
	// .getEstimatedDuration()));
	// assertTrue(planning.isValidTimeSpan(task, timeSpan, null));
	// }

	@Test
	public void testIsValidTimeSpanAfter20150115() {
		NormalTask task = ph.getProjects().get(0).getTasks().get(0);
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

	@Test
	public void testIsValidTimeSpanNotEnoughResources() {
		ResourceType r1 = new ResourceType("name", null, null, false);
		r1.addResource("resource 1", new LocalTime(10, 0), new LocalTime(11, 0));
		r1.addResource("resource 2", new LocalTime(14, 0), new LocalTime(15, 0));

		Map<ResourceType, Integer> resourceTypes = new HashMap<ResourceType, Integer>();
		resourceTypes.put(r1, 2);
		ph.getProjects().get(0)
				.addTask("description 1", 10, 1, null, null, resourceTypes, 1);
		NormalTask task = ph.getProjects().get(0).getTasks()
				.get(ph.getProjects().get(0).getTasks().size() - 1);
		assertFalse(planning.isValidTimeSpan(task, new TimeSpan(new DateTime(
				2015, 10, 12, 10, 0), new DateTime(2015, 10, 12, 10, 30)),
				new DateTime(2015, 10, 12, 10, 0)));
	}

	@Test
	public void testIsValidTimeSpan_DeveloperNotAvailable() {
		NormalTask task = ph.getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("d 1", new LocalTime(14, 0), new LocalTime(
				14, 10));
		task.addRequiredDeveloper(d);
		assertFalse(planning.isValidTimeSpan(task, new TimeSpan(new DateTime(
				2015, 10, 12, 10, 30), new DateTime(2015, 10, 12, 14, 30)),
				new DateTime(2015, 10, 12, 10, 0)));
	}

	@Test
	public void testIsValidTimeSpanResourceFree() {
		NormalTask task3 = ph.getProjects().get(0).getTasks().get(2);
		NormalTask task4 = ph.getProjects().get(0).getTasks().get(3);
		DateTime newStartTime = startTime.plusMinutes(task3
				.getEstimatedDuration() + 1);
		TimeSpan timeSpan = new TimeSpan(newStartTime,
				newStartTime.plusMinutes(task4.getEstimatedDuration()));
		assertTrue(planning.isValidTimeSpan(task4, timeSpan, null));
	}
}
