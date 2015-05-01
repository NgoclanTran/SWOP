package taskman.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.memento.TaskMemento;
import taskman.model.project.task.Task;
import taskman.model.resource.ResourceType;
import taskman.model.time.Clock;
import taskman.model.time.TimeSpan;
import taskman.model.user.Developer;

public class TaskTest {
	private String description;
	private int estimatedDuration;
	private int acceptableDeviation;
	private List<Task> dependencies;
	private TimeSpan timespan;
	private Clock clock = Clock.getInstance();

	@Before
	public void setup() {
		description = "description";
		estimatedDuration = 10;
		acceptableDeviation = 1;
		dependencies = new ArrayList<Task>();
		clock.setSystemTime(new DateTime(2015, 10, 12, 8, 0));

	}

	@Test(expected = IllegalArgumentException.class)
	public void constructor1Test_FalseCase_DescriptionNull() {
		Task t = new Task(null, estimatedDuration, acceptableDeviation,
				dependencies, null, null);

	}

	@Test(expected = IllegalArgumentException.class)
	public void constructor1Test_FalseCase_EstimatedDuration() {
		Task t = new Task(description, -10, acceptableDeviation, dependencies,
				null, null);

	}

	@Test(expected = IllegalArgumentException.class)
	public void constructor1Test_FalseCase_acceptableDeviation() {
		Task t = new Task(description, estimatedDuration, -10, dependencies,
				null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructor2Test_FalseCase_DescriptionNull() {
		Task t = new Task(null, estimatedDuration, acceptableDeviation,
				dependencies, null, null);

	}

	@Test
	public void constructorTest_TrueCase() {
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();
		assertEquals(t.getDescription(), description);
		assertEquals(t.getEstimatedDuration(), estimatedDuration);
		assertEquals(t.getAcceptableDeviation(), acceptableDeviation);
		assertTrue(t.getDependencies().isEmpty());
		assertEquals(t.getRequiredDevelopers().get(0), d);
		assertTrue(t.getRequiredResourceTypes().isEmpty());
		assertNull(t.getAlternative());
		assertEquals(t.getStatusName(), "AVAILABLE");
	}

	@Test
	public void constructorTest_WithDependencies() {
		Task t1 = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		ArrayList<Task> l = new ArrayList<Task>();
		l.add(t1);
		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
				l, null, null);
		assertEquals(t2.getDescription(), description);
		assertEquals(t2.getEstimatedDuration(), estimatedDuration);
		assertEquals(t2.getAcceptableDeviation(), acceptableDeviation);
		assertEquals(t2.getDependencies(), l);
		assertTrue(t2.getRequiredDevelopers().isEmpty());
		assertTrue(t2.getRequiredResourceTypes().isEmpty());
		assertNull(t2.getAlternative());
		assertEquals(t2.getStatusName(), "UNAVAILABLE");
	}

	@Test
	public void constructorTest_WithRequireResourceTypes() {
		HashMap<ResourceType, Integer> map = new HashMap<ResourceType, Integer>();
		ResourceType rt = new ResourceType("name", null, null, false);
		rt.addResource("1", new LocalTime(8, 0), new LocalTime(16, 0));
		rt.addResource("2", new LocalTime(8, 0), new LocalTime(16, 0));
		rt.addResource("3", new LocalTime(8, 0), new LocalTime(16, 0));
		map.put(rt, 3);
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, map);
		assertEquals(t.getDescription(), description);
		assertEquals(t.getEstimatedDuration(), estimatedDuration);
		assertEquals(t.getAcceptableDeviation(), acceptableDeviation);
		assertTrue(t.getDependencies().isEmpty());
		assertTrue(t.getRequiredDevelopers().isEmpty());
		assertEquals(t.getRequiredResourceTypes().size(), 1);
		assertTrue(t.getRequiredResourceTypes().containsKey(rt));
		assertNull(t.getAlternative());
		assertEquals(t.getStatusName(), "UNAVAILABLE");
	}

	@Test
	public void constructorTest_WithAlternativeFor() {
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();
		DateTime startTime = new DateTime(2015, 10, 12, 9, 0);
		DateTime endTime = new DateTime(2015, 10, 12, 10, 0);

		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(true, startTime, endTime);
		assertEquals(t.getStatusName(), "FAILED");

		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
				null, t, null);
		assertEquals(t.getAlternative(), t2);

	}

	@Test
	public void getDependenciesTest_TrueCase() {
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
		dependencies.add(t2);

		// test if the dependencies of our task didn't changed
		assertNotEquals(t.getDependencies(), dependencies);
	}

	@Test(expected = IllegalArgumentException.class)
	public void attachDependantTest_FalseCase_Null() {
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
		t.attachDependant(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addTimeSpanTest_FalseCase_StartNull() {
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();
		DateTime endTime = new DateTime(2015, 1, 1, 1, 1);
		t.addTimeSpan(false, null, endTime);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addTimeSpanTest_FalseCase_EndNull() {
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();
		DateTime startTime = new DateTime(2015, 1, 1, 1, 1);
		t.addTimeSpan(true, startTime, null);
	}

	@Test(expected = IllegalStateException.class)
	public void addTimeSpanTest_StatusUNAVAILABLE() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 10, 1);

		t.addTimeSpan(true, startTime, endTime);
		assertEquals(t.getStatusName(), "FAILED");
		ArrayList<Task> dep = new ArrayList<Task>();
		dep.add(t);
		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
				dep, null, null);
		assertEquals(t2.getStatusName(), "UNAVAILABLE");
		assertFalse(t2.isAvailable());
		assertEquals(t2.getStatusName(), "UNAVAILABLE");
		t2.addTimeSpan(false, startTime, endTime);
	}

	@Test
	public void addTimeSpanTest_StatusAVAILABLE() {
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();
		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		assertEquals(t.getStatusName(), "AVAILABLE");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
	}

	@Test(expected = IllegalStateException.class)
	public void addTimeSpanTest_StatusFAILED() {
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(true, startTime, endTime);
		assertEquals(t.getStatusName(), "FAILED");
		t.addTimeSpan(true, startTime, endTime);
	}

	@Test(expected = IllegalStateException.class)
	public void addTimeSpanTest_StatusFINISHED() {
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "FINISHED");
		t.addTimeSpan(true, startTime, endTime);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addAlternativeTest_FalseCase_AlternativeNull() {
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(true, startTime, endTime);
		t.addAlternative(null);
	}

	@Test(expected = IllegalStateException.class)
	public void addAlternativeTest_StatusAVAILABLE() {
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();
		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
		t.addAlternative(t2);

	}

	@Test(expected = IllegalStateException.class)
	public void addAlternativeTest_StatusUNAVAILABLE() {
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		assertEquals(t.getStatusName(), "UNAVAILABLE");
		t.addAlternative(t2);
	}

	@Test
	public void addAlternativeTest_StatusFAILED() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(true, startTime, endTime);
		assertEquals(t.getStatusName(), "FAILED");
		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
				dependencies, null, null);

		t.addAlternative(t2);
		assertEquals(t.getAlternative(), t2);
		assertEquals(t.getStatusName(), "FAILED");
	}

	@Test(expected = IllegalStateException.class)
	public void addAlternativeTest_StatusFINISHED() {
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "FINISHED");
		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
				dependencies, null, null);

		t.addAlternative(t2);
	}

	@Test
	public void isAvailableTest_StatusAVAILABLE() {
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();
		assertEquals(t.getStatusName(), "AVAILABLE");
		assertTrue(t.isAvailable());

	}

	@Test
	public void isAvailableTest_StatusUNAVAILABLE() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
		assertEquals(t.getStatusName(), "UNAVAILABLE");
		assertFalse(t.isAvailable());
	}

	@Test
	public void isAvailableTest_StatusFINISHED() {
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "FINISHED");
		assertFalse(t.isAvailable());
	}

	@Test
	public void isAvailableTest_StatusFAILED() {
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(true, startTime, endTime);
		assertEquals(t.getStatusName(), "FAILED");
		assertFalse(t.isAvailable());
	}

	@Test
	public void isAvailableTest_StatusEXECUTING() {
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		assertFalse(t.isAvailable());
	}

	@Test
	public void isFinishedTest_StatusAVAILABLE() {
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();
		assertFalse(t.isFinished());

	}

	@Test
	public void isFinishedTest_StatusUNAVAILABLE() {
		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);

		assertFalse(t2.isFinished());
		assertEquals(t2.getStatusName(), "UNAVAILABLE");
	}

	@Test
	public void isFinishedTest_StatusFINISHED() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(false, startTime, endTime);

		assertTrue(t.isFinished());
		assertEquals(t.getStatusName(), "FINISHED");
	}

	@Test
	public void isFinishedTest_StatusFAILED() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(true, startTime, endTime);
		assertFalse(t.isFinished());
		assertEquals(t.getStatusName(), "FAILED");
	}

	@Test
	public void isFinishedTest_StatusEXECUTING() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		assertFalse(t.isFinished());
	}

	@Test
	public void isFailedTest_StatusAVAILABLE() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		assertFalse(t.isFailed());
		assertEquals(t.getStatusName(), "AVAILABLE");

	}

	@Test
	public void isFailedTest_StatusUNAVAILABLE() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
		assertFalse(t.isFailed());
		assertEquals(t.getStatusName(), "UNAVAILABLE");
	}

	@Test
	public void isFailedTest_StatusFINISHED() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "FINISHED");
		assertFalse(t.isFailed());

	}

	@Test
	public void isFailedTest_StatusFAILED() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(true, startTime, endTime);

		assertTrue(t.isFailed());
		assertEquals(t.getStatusName(), "FAILED");
	}

	@Test
	public void isFailedTest_StatusEXECUTING() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		assertFalse(t.isFailed());
	}

	@Test
	public void isCompletedTest_StatusAVAILABLE() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		assertFalse(t.isCompleted());
	}

	@Test
	public void isCompletedTest_StatusUNAVAILABLE() {
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
		assertFalse(t.isCompleted());
	}

	@Test
	public void isCompletedTest_StatusFAILED() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(true, startTime, endTime);
		assertTrue(t.isCompleted());
	}

	@Test
	public void isCompletedTest_StatusFINISHED() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(false, startTime, endTime);
		assertTrue(t.isCompleted());
	}

	@Test
	public void updateTaskAvailabilityTest_TrueCase_StatusAVAILABLE() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		assertEquals(t.getStatusName(), "AVAILABLE");

		t.updateTaskAvailability();
		assertEquals(t.getStatusName(), "AVAILABLE");
	}

	@Test(expected = IllegalStateException.class)
	public void updateTaskAvailabilityTest_TrueCase_StatusFINISHED() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(false, startTime, endTime);
		t.updateTaskAvailability();
	}

	@Test(expected = IllegalStateException.class)
	public void updateTaskAvailabilityTest_FalseCase_StatusFAILED() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(true, startTime, endTime);
		t.updateTaskAvailability();

	}

	@Test(expected = IllegalStateException.class)
	public void updateTaskAvailabilityTest_FalseCase_StatusEXECUTING() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "EXECUTING");
		t.updateTaskAvailability();

	}

	@Test
	public void updateTaskAvailabitlityTest_DependantUpdate() {
		Task t1 = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		ArrayList<Task> l = new ArrayList<Task>();
		l.add(t1);
		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
				l, null, null);
		Developer d1 = new Developer("name developer 1", new LocalTime(8, 0),
				new LocalTime(23, 0));
		d1.addReservation(t1, new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 10, 0)));
		t1.addRequiredDeveloper(d1);

		Developer d2 = new Developer("name developer 2", new LocalTime(8, 0),
				new LocalTime(23, 0));
		d2.addReservation(t2, new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 10, 0)));
		t2.addRequiredDeveloper(d2);

		assertEquals(t1.getStatusName(), "UNAVAILABLE");
		assertEquals(t2.getStatusName(), "UNAVAILABLE");

		t1.updateTaskAvailability();
		assertEquals(t1.getStatusName(), "AVAILABLE");
		assertEquals(t2.getStatusName(), "UNAVAILABLE");

	}

	@Test
	public void updateTaskAvailabilityTest_DeveloperAvailableTest() {
		Task t1 = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name developer 1", new LocalTime(8, 0),
				new LocalTime(23, 0));
		d.addReservation(t1, new TimeSpan(new DateTime(2015, 10, 12, 11, 11),
				new DateTime(2015, 10, 14, 10, 0)));
		t1.addRequiredDeveloper(d);

		Developer d2 = new Developer("name developer 1", new LocalTime(13, 0),
				new LocalTime(23, 0));
		t1.addRequiredDeveloper(d2);
		t1.updateTaskAvailability();
	}

	@Test
	public void updateTaskAvailabilityTest_ResourceAvailableTest() {

		ResourceType rt = new ResourceType("name resourceTyp 1", null, null,
				false);
		rt.addResource("1", new LocalTime(8, 0), new LocalTime(23, 0));
		rt.addResource("2", new LocalTime(16, 0), new LocalTime(23, 0));

		LinkedHashMap<ResourceType, Integer> l = new LinkedHashMap<ResourceType, Integer>();
		l.put(rt, 2);
		Task t1 = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, l);
		rt.getResources()
				.get(0)
				.addReservation(
						t1,
						new TimeSpan(new DateTime(2015, 10, 12, 11, 11),
								new DateTime(2015, 10, 14, 10, 0)));
		Developer d = new Developer("name developer 1", new LocalTime(8, 0),
				new LocalTime(23, 0));
		d.addReservation(t1, new TimeSpan(new DateTime(2015, 10, 12, 11, 11),
				new DateTime(2015, 10, 14, 10, 0)));
		t1.addRequiredDeveloper(d);

		t1.updateTaskAvailability();
	}

	@Test
	public void updateTaskAvailabilityTest_FAILEDTask_Update() {
		Task t1 = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d1 = new Developer("name", new LocalTime(8, 0),
				new LocalTime(16, 0));
		d1.addReservation(t1, new TimeSpan(new DateTime(2015, 10, 12, 8, 11),
				new DateTime(2015, 10, 14, 10, 0)));
		t1.addRequiredDeveloper(d1);
		t1.updateTaskAvailability();
		assertEquals(t1.getStatusName(), "AVAILABLE");

		t1.executeTask();
		assertEquals(t1.getStatusName(), "EXECUTING");
		t1.addTimeSpan(true, new DateTime(2015, 10, 12, 8, 0), new DateTime(
				2015, 10, 12, 16, 0));
		assertEquals(t1.getStatusName(), "FAILED");

		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
				null, t1, null);
		Developer d2 = new Developer("name", new LocalTime(8, 0),
				new LocalTime(16, 0));
		d2.addReservation(t2, new TimeSpan(new DateTime(2015, 10, 12, 8, 11),
				new DateTime(2015, 10, 14, 10, 0)));
		t2.addRequiredDeveloper(d2);
		t2.updateTaskAvailability();
		assertEquals(t2.getStatusName(), "AVAILABLE");

		t2.executeTask();
		assertEquals(t2.getStatusName(), "EXECUTING");

		ArrayList<Task> listTasks = new ArrayList<Task>();
		listTasks.add(t1);
		Task t3 = new Task(description, estimatedDuration, acceptableDeviation,
				listTasks, null, null);
		t3.updateTaskAvailability();

	}

	@Test
	public void updateTaskAvailabilityTest_isAlternativeFinishedTest_NoAlternative() {
		Task t1 = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d1 = new Developer("name", new LocalTime(8, 0),
				new LocalTime(16, 0));
		d1.addReservation(t1, new TimeSpan(new DateTime(2015, 10, 12, 8, 11),
				new DateTime(2015, 10, 14, 10, 0)));
		t1.addRequiredDeveloper(d1);
		t1.updateTaskAvailability();
		assertEquals(t1.getStatusName(), "AVAILABLE");
		t1.executeTask();
		assertEquals(t1.getStatusName(), "EXECUTING");
		t1.addTimeSpan(true, new DateTime(2015, 10, 12, 8, 0), new DateTime(
				2015, 10, 12, 16, 0));
		assertEquals(t1.getStatusName(), "FAILED");

		ArrayList<Task> dependencies = new ArrayList<Task>();
		dependencies.add(t1);
		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
		t2.updateTaskAvailability();

	}

	@Test
	public void updateTaskAvailabilityTest_isAlternativeFinishedTest_AlternativeFINISHED() {
		Task t1 = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d1 = new Developer("name", new LocalTime(8, 0),
				new LocalTime(16, 0));
		d1.addReservation(t1, new TimeSpan(new DateTime(2015, 10, 12, 8, 11),
				new DateTime(2015, 10, 14, 10, 0)));
		t1.addRequiredDeveloper(d1);
		t1.updateTaskAvailability();
		assertEquals(t1.getStatusName(), "AVAILABLE");
		t1.executeTask();
		assertEquals(t1.getStatusName(), "EXECUTING");
		t1.addTimeSpan(true, new DateTime(2015, 10, 12, 8, 0), new DateTime(
				2015, 10, 12, 16, 0));
		assertEquals(t1.getStatusName(), "FAILED");

		Task t3 = new Task(description, estimatedDuration, acceptableDeviation,
				null, t1, null);
		Developer d2 = new Developer("name", new LocalTime(8, 0),
				new LocalTime(16, 0));
		d2.addReservation(t3, new TimeSpan(new DateTime(2015, 10, 12, 8, 11),
				new DateTime(2015, 10, 14, 10, 0)));
		t3.addRequiredDeveloper(d2);
		t3.updateTaskAvailability();
		assertEquals(t3.getStatusName(), "AVAILABLE");
		t3.executeTask();
		assertEquals(t3.getStatusName(), "EXECUTING");
		t3.addTimeSpan(false, new DateTime(2015, 10, 12, 8, 0), new DateTime(
				2015, 10, 12, 16, 0));
		assertEquals(t3.getStatusName(), "FINISHED");

		ArrayList<Task> dependencies = new ArrayList<Task>();
		dependencies.add(t1);
		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
		t2.updateTaskAvailability();

	}

	@Test
	public void updateTaskAvailabilityTest_isAlternativeFinishedTest_AlternativeFAILED() {
		Task t1 = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d1 = new Developer("name", new LocalTime(8, 0),
				new LocalTime(16, 0));
		d1.addReservation(t1, new TimeSpan(new DateTime(2015, 10, 12, 8, 11),
				new DateTime(2015, 10, 14, 10, 0)));
		t1.addRequiredDeveloper(d1);
		t1.updateTaskAvailability();
		assertEquals(t1.getStatusName(), "AVAILABLE");
		t1.executeTask();
		assertEquals(t1.getStatusName(), "EXECUTING");
		t1.addTimeSpan(true, new DateTime(2015, 10, 12, 8, 0), new DateTime(
				2015, 10, 12, 16, 0));
		assertEquals(t1.getStatusName(), "FAILED");

		Task t3 = new Task(description, estimatedDuration, acceptableDeviation,
				null, t1, null);
		Developer d2 = new Developer("name", new LocalTime(8, 0),
				new LocalTime(16, 0));
		d2.addReservation(t3, new TimeSpan(new DateTime(2015, 10, 12, 8, 11),
				new DateTime(2015, 10, 14, 10, 0)));
		t3.addRequiredDeveloper(d2);
		t3.updateTaskAvailability();
		assertEquals(t3.getStatusName(), "AVAILABLE");
		t3.executeTask();
		assertEquals(t3.getStatusName(), "EXECUTING");
		t3.addTimeSpan(true, new DateTime(2015, 10, 12, 8, 0), new DateTime(
				2015, 10, 12, 16, 0));
		assertEquals(t3.getStatusName(), "FAILED");

		ArrayList<Task> dependencies = new ArrayList<Task>();
		dependencies.add(t1);
		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
		t2.updateTaskAvailability();

	}

	@Test
	public void calculateTotalExecutionTimeTest_TrueCase() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2015, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getTotalExecutionTime(), 60);
	}

	@Test(expected = IllegalStateException.class)
	public void calculateTotalExecutionTimeTest_StatusUNAVAILABLE() {
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
		int time = t.getTotalExecutionTime();
	}

	@Test
	public void calculateTotalExecutionTimeTest_StatusFAILED() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2015, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(true, startTime, endTime);
		assertEquals(t.getTotalExecutionTime(), 60);
	}

	@Test
	public void calculateTotalExecutionTimeTest_StatusFINISHEED() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2015, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "FINISHED");
		int time = t.getTotalExecutionTime();
		assertEquals(time, 60);
	}

	// @Test (expected = IllegalStateException.class)
	// public void calculateTotalExecutionTimeTest_StatusEXECUTING(){
	// //TODO
	// Task t = new Task(description, estimatedDuration, acceptableDeviation,
	// null, null, null);
	// Developer d = new Developer("name", new LocalTime(8,0), new
	// LocalTime(16,0));
	// TimeSpan timeSpan = new TimeSpan(new DateTime(2015,10,12,8,0), new
	// DateTime(2015,10,12,16,0));
	// d.addReservation(t, timeSpan);
	// t.addRequiredDeveloper(d);
	// t.updateTaskAvailability();
	//
	// DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
	// DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
	// t.addTimeSpan(false, startTime, endTime);
	// assertEquals(t.getStatusName(),"EXECUTING");
	// t.getTotalExecutionTime();
	//
	// }

	@Test
	public void calculateTotalExecutionTimeTest_TrueCase_Alternative() {
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2015, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(true, startTime, endTime);

		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
				null, t, null);
		Developer d2 = new Developer("name", new LocalTime(8, 0),
				new LocalTime(16, 0));
		TimeSpan timeSpan2 = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d2.addReservation(t2, timeSpan2);
		t2.addRequiredDeveloper(d2);
		t2.updateTaskAvailability();

		DateTime startTime2 = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime2 = new DateTime(2015, 1, 1, 12, 1);
		t2.executeTask();
		assertEquals(t2.getStatusName(), "EXECUTING");
		t2.addTimeSpan(false, startTime2, endTime2);

		assertEquals(t.getTotalExecutionTime(), 120);

	}

	@Test
	public void calculateOverduePercentageTest_TrueCase() {
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 0);
		DateTime endTime = new DateTime(2015, 1, 1, 11, 0);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(false, startTime, endTime);

		assertEquals(t.getOverduePercentage(), (60 - this.estimatedDuration)
				* 100 / this.estimatedDuration);

	}

	@Test
	public void isPlannedTest_TrueCase() {
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		assertTrue(t.isPlanned());
	}

	@Test
	public void isPlannedTest_FalseCase() {
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		assertFalse(t.isPlanned());
	}

	@Test
	public void isSeveralyOverDueTest_StatusUNAVAILABLE() {

	}

	@Test(expected = IllegalStateException.class)
	public void isSeveralyOverDueTest_StatusAVAILABLE() {
		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();
		t.isSeverelyOverdue();
	}

	@Test
	public void isSeveralyOverDueTest_StatusFINISHED() {
		Task t = new Task(description, estimatedDuration, 0, null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 9, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();
		DateTime startTime = new DateTime(2015, 1, 1, 10, 0);
		DateTime endTime = new DateTime(2015, 1, 1, 12, 0);
		t.executeTask();
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "FINISHED");
		assertTrue(t.isSeverelyOverdue());
	}

	@Test
	public void isSeveralyOverDueTest_StatusFAILED() {
		Task t = new Task(description, estimatedDuration, 0, null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 9, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();
		DateTime startTime = new DateTime(2015, 1, 1, 10, 0);
		DateTime endTime = new DateTime(2015, 1, 1, 12, 0);
		t.executeTask();
		t.addTimeSpan(true, startTime, endTime);
		assertEquals(t.getStatusName(), "FAILED");
		assertTrue(t.isSeverelyOverdue());

	}

	@Test(expected = IllegalStateException.class)
	public void isSeveralyOverDueTest_StatusEXECUTING() {
		Task t = new Task(description, estimatedDuration, 0, null, null, null);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 9, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();
		assertTrue(t.isSeverelyOverdue());
	}

	@Test
	public void createMomentoTest() {
		Task t = new Task(description, estimatedDuration, 0, null, null, null);
		assertTrue(t.createMemento() instanceof TaskMemento);
	}

	@Test
	public void getDevelopersTest() {
		Task t = new Task(description, estimatedDuration, 0, null, null, null);
		List<Developer> ld = t.getRequiredDevelopers();
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		ld.add(d);
		assertNotEquals(t.getRequiredDevelopers(), ld);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addDeveloperTest_Null() {
		Task t = new Task(description, estimatedDuration, 0, null, null, null);
		t.addRequiredDeveloper(null);
	}

	@Test
	public void addDeveloperTest_TrueCase() {
		Task t = new Task(description, estimatedDuration, 0, null, null, null);
		Developer developer = new Developer("1", new LocalTime(8, 0),
				new LocalTime(16, 0));

		t.addRequiredDeveloper(developer);
		assertEquals(t.getRequiredDevelopers().get(0), developer);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addDeveloperTest_AlreadyInTask() {
		Task t = new Task(description, estimatedDuration, 0, null, null, null);
		Developer developer = new Developer("1", new LocalTime(8, 0),
				new LocalTime(16, 0));
		t.addRequiredDeveloper(developer);
		t.addRequiredDeveloper(developer);

	}

	@Test(expected = IllegalArgumentException.class)
	public void attachDependantTest_Null() {
		Task t1 = new Task(description, estimatedDuration, 0, null, null, null);
		t1.attachDependant(null);
	}

	@Test
	public void attachDependantTest() {
		Task t1 = new Task(description, estimatedDuration, 0, null, null, null);
		Task t2 = new Task(description, estimatedDuration, 0, null, null, null);
		t1.attachDependant(t2);
	}

	@Test
	public void gerRequiredResourceTest() {
		Task t = new Task(description, estimatedDuration, 0, null, null, null);
		HashMap<ResourceType, Integer> l = (HashMap<ResourceType, Integer>) t
				.getRequiredResourceTypes();
		ResourceType r = new ResourceType("name", null, null, false);
		l.put(r, 1);
		assertNotEquals(t.getRequiredResourceTypes(), l);
	}

	// @Test (expected = IllegalArgumentException.class)
	// public void addRequiredResourceTest_AlreadyAdded(){
	// Task t = new Task(description, estimatedDuration, 0, null, null, null);
	// ResourceType r = new ResourceType("name", null, null, false);
	// t.addRequiredResourceType(r, 2);
	// t.addRequiredResourceType(r, 1);
	// }

	@Test(expected = IllegalArgumentException.class)
	public void addRequiredResourceTest_SelfConflicting() {

		ResourceType r = new ResourceType("name", null, null, true);
		r.addResource("effective resource 1", new LocalTime(0, 0),
				new LocalTime(23, 59));
		r.addResource("effective resource 2", new LocalTime(0, 0),
				new LocalTime(23, 59));

		LinkedHashMap<ResourceType, Integer> map = new LinkedHashMap<ResourceType, Integer>();
		map.put(r, 2);
		new Task(description, estimatedDuration, 0, null, null, map);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addRequiredResourceTest_Conflicting1() {

		ResourceType r1 = new ResourceType("resource 1", null, null, false);
		r1.addResource("effective resource 1", new LocalTime(0, 0),
				new LocalTime(23, 59));
		ArrayList<ResourceType> c = new ArrayList<ResourceType>();
		c.add(r1);
		ResourceType r2 = new ResourceType("resource 2", null, c, false);
		r2.addResource("effective resource 2", new LocalTime(0, 0),
				new LocalTime(23, 59));

		LinkedHashMap<ResourceType, Integer> map = new LinkedHashMap<ResourceType, Integer>();
		map.put(r2, 1);
		map.put(r1, 1);
		new Task(description, estimatedDuration, 0, null, null, map);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addRequiredResourceTest_Conflicting2() {

		ResourceType r1 = new ResourceType("resource 1", null, null, false);
		r1.addResource("effective resource 1", new LocalTime(0, 0),
				new LocalTime(23, 59));
		ArrayList<ResourceType> c = new ArrayList<ResourceType>();
		c.add(r1);
		ResourceType r2 = new ResourceType("resource 2", null, c, false);
		r2.addResource("effective resource 2", new LocalTime(0, 0),
				new LocalTime(23, 59));

		LinkedHashMap<ResourceType, Integer> map = new LinkedHashMap<ResourceType, Integer>();
		map.put(r1, 1);
		map.put(r2, 1);
		new Task(description, estimatedDuration, 0, null, null, map);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addRequiredResourceTest_ToMuchAdded() {
		ResourceType r = new ResourceType("name", null, null, false);
		r.addResource("effective resource 1", new LocalTime(0, 0),
				new LocalTime(23, 59));
		r.addResource("effective resource 2", new LocalTime(0, 0),
				new LocalTime(23, 59));

		LinkedHashMap<ResourceType, Integer> map = new LinkedHashMap<ResourceType, Integer>();
		map.put(r, 3);
		new Task(description, estimatedDuration, 0, null, null, map);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addRequiredResourceTest_AddRequiredFirst() {
		ResourceType t1 = new ResourceType("type 1", null, null, false);
		t1.addResource("effective resource 1", new LocalTime(0, 0),
				new LocalTime(23, 59));
		ArrayList<ResourceType> l = new ArrayList<ResourceType>();
		l.add(t1);

		ResourceType t2 = new ResourceType("type 1", l, null, false);

		LinkedHashMap<ResourceType, Integer> map = new LinkedHashMap<ResourceType, Integer>();
		map.put(t2, 1);
		new Task(description, estimatedDuration, 0, null, null, map);

	}

	@Test
	public void setMomenToTest_Status_UNAVAILABLE() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);
		TaskMemento tm = t.createMemento();
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		assertEquals(t.getStatusName(), "AVAILABLE");

		t.setMemento(tm);
		assertEquals(t.getDependencies(), dependencies);
		assertNull(t.getAlternative());
		assertEquals(t.getStatusName(), "UNAVAILABLE");

	}

	@Test
	public void setMomenToTest_Status_AVAILABLE() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		assertEquals(t.getStatusName(), "AVAILABLE");
		TaskMemento tm = t.createMemento();
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.setMemento(tm);
		assertEquals(t.getDependencies(), dependencies);
		assertNull(t.getAlternative());
		assertEquals(t.getStatusName(), "AVAILABLE");

	}

	@Test
	public void setMomenToTest_Status_FINISHED() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		assertEquals(t.getStatusName(), "AVAILABLE");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");

		t.addTimeSpan(false, new DateTime(2015, 10, 14, 10, 0), new DateTime(
				2015, 10, 14, 13, 0));
		assertEquals(t.getStatusName(), "FINISHED");
		TaskMemento tm = t.createMemento();
		t.setMemento(tm);
		assertEquals(t.getDependencies(), dependencies);
		assertNull(t.getAlternative());
		assertEquals(t.getStatusName(), "FINISHED");

	}

	@Test
	public void setMomenToTest_Status_FAILED() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		assertEquals(t.getStatusName(), "AVAILABLE");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");

		t.addTimeSpan(true, new DateTime(2015, 10, 14, 10, 0), new DateTime(
				2015, 10, 14, 13, 0));
		assertEquals(t.getStatusName(), "FAILED");
		TaskMemento tm = t.createMemento();
		t.setMemento(tm);
		assertEquals(t.getDependencies(), dependencies);
		assertNull(t.getAlternative());
		assertEquals(t.getStatusName(), "FAILED");

	}

	@Test
	public void setMomenToTest_Status_EXECUTING() {

		Task t = new Task(description, estimatedDuration, acceptableDeviation,
				null, null, null);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.updateTaskAvailability();

		assertEquals(t.getStatusName(), "AVAILABLE");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");

		TaskMemento tm = t.createMemento();
		t.addTimeSpan(true, new DateTime(2015, 10, 14, 10, 0), new DateTime(
				2015, 10, 14, 13, 0));
		assertEquals(t.getStatusName(), "FAILED");
		t.setMemento(tm);
		assertEquals(t.getDependencies(), dependencies);
		assertNull(t.getAlternative());
		assertEquals(t.getStatusName(), "EXECUTING");

	}

	@Test
	public void executeTaskTest() {

	}
}
