package taskman.tests;
//TODO test for task without reservation -> UNAVAILABLE and AVAILABLE
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

import taskman.exceptions.IllegalDateException;
import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.memento.NormalTaskMemento;
import taskman.model.project.Project;
import taskman.model.resource.ResourceType;
import taskman.model.task.NormalTask;
import taskman.model.task.Task;
import taskman.model.time.Clock;
import taskman.model.time.TimeSpan;
import taskman.model.user.Developer;

public class TaskTest {
	private String description;
	private int estimatedDuration;
	private int acceptableDeviation;
	private List<NormalTask> dependencies;
	private TimeSpan timespan;
	private Clock clock = new Clock();
	private Company company;
	List<ResourceType> list =  new ArrayList<ResourceType>();
	private BranchOffice branchOffice;

	@Before
	public void setup() {
		description = "description";
		estimatedDuration = 10;
		acceptableDeviation = 1;
		dependencies = new ArrayList<NormalTask>();
		clock.setSystemTime(new DateTime(2015, 10, 12, 8, 0));
		company = new Company();
		ResourceType rt = new ResourceType("name", null, null, false);
		rt.addResource("n", new LocalTime(8,0), new LocalTime(16,0));
		list.add(rt);
		branchOffice = new BranchOffice(company, "", list);

	}

	@Test(expected = IllegalArgumentException.class)
	public void constructor1Test_FalseCase_DescriptionNull() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(null, 10, 1, dependencies, null, null, 1);
		//Task t = new Task(clock,null, estimatedDuration, acceptableDeviation,
		//		dependencies, null, null);

	}

	@Test(expected = IllegalArgumentException.class)
	public void constructor1Test_FalseCase_EstimatedDuration() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, -10, acceptableDeviation, dependencies, null, null, 1);
		//Task t = new Task(clock,description, -10, acceptableDeviation, dependencies,
		//		null, null);

	}

	@Test(expected = IllegalArgumentException.class)
	public void constructor1Test_FalseCase_acceptableDeviation() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, -10, dependencies, null, null, 1);
		//Task t = new Task(clock,description, estimatedDuration, -10, dependencies,
		//		null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructor2Test_FalseCase_DescriptionNull() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(null, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);

	}

	@Test
	public void constructorTest_TrueCase() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		Task t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		assertEquals(t.getDescription(), description);
		assertEquals(t.getEstimatedDuration(), estimatedDuration);
		assertEquals(t.getAcceptableDeviation(), acceptableDeviation);
		assertTrue(((NormalTask) t).getDependencies().isEmpty());
		assertEquals(t.getRequiredDevelopers().get(0), d);
		assertTrue(t.getRequiredResourceTypes().isEmpty());
		assertNull(((NormalTask) t).getAlternative());
		assertEquals(t.getStatusName(), "PLANNED");
	}

	@Test
	public void constructorTest_WithDependencies() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t1 = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		ArrayList<NormalTask> l = new ArrayList<NormalTask>();
		l.add(t1);
		p.addTask(description, estimatedDuration, acceptableDeviation,l,null,null, 1);
		Task t2 = branchOffice.getPh().getProjects().get(0).getTasks().get(1);
		assertEquals(t2.getDescription(), description);
		assertEquals(t2.getEstimatedDuration(), estimatedDuration);
		assertEquals(t2.getAcceptableDeviation(), acceptableDeviation);
		assertEquals(((NormalTask) t2).getDependencies(), l);
		assertTrue(t2.getRequiredDevelopers().isEmpty());
		assertTrue(t2.getRequiredResourceTypes().isEmpty());
		assertNull(((NormalTask) t2).getAlternative());
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
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, map, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
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
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		DateTime startTime = new DateTime(2015, 10, 12, 9, 0);
		DateTime endTime = new DateTime(2015, 10, 12, 10, 0);

		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(true, startTime, endTime);
		assertEquals(t.getStatusName(), "FAILED");
		p.addTask(description, estimatedDuration, acceptableDeviation, null, t, null, 1);
		NormalTask t2 = branchOffice.getPh().getProjects().get(0).getTasks().get(1);
		assertEquals(t.getAlternative(), t2);

	}

	@Test
	public void getDependenciesTest_TrueCase() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t2 = branchOffice.getPh().getProjects().get(0).getTasks().get(1);
		dependencies.add(t2);

		// test if the dependencies of our task didn't changed
		assertNotEquals(t.getDependencies(), dependencies);
	}

	@Test(expected = IllegalArgumentException.class)
	public void attachDependantTest_FalseCase_Null() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null,1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		t.attachDependant(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addTimeSpanTest_FalseCase_StartNull() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		DateTime endTime = new DateTime(2015, 1, 1, 1, 1);
		t.addTimeSpan(false, null, endTime);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addTimeSpanTest_FalseCase_EndNull() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		DateTime startTime = new DateTime(2015, 1, 1, 1, 1);
		t.addTimeSpan(true, startTime, null);
	}

	@Test(expected = IllegalStateException.class)
	public void addTimeSpanTest_StatusUNAVAILABLE() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 10, 1);

		t.addTimeSpan(true, startTime, endTime);
		assertEquals(t.getStatusName(), "FAILED");
		ArrayList<NormalTask> dep = new ArrayList<NormalTask>();
		dep.add(t);
		p.addTask(description, estimatedDuration, acceptableDeviation, dep, null, null, 1);
		NormalTask t2 = branchOffice.getPh().getProjects().get(0).getTasks().get(1);
		assertEquals(t2.getStatusName(), "UNAVAILABLE");
		assertFalse(t2.isAvailable());
		assertEquals(t2.getStatusName(), "UNAVAILABLE");
		t2.addTimeSpan(false, startTime, endTime);
	}

	@Test
	public void addTimeSpanTest_StatusPLANNED() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
	}

	@Test(expected = IllegalStateException.class)
	public void addTimeSpanTest_StatusFAILED() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

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
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

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
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(true, startTime, endTime);
		t.addAlternative(null);
	}

	@Test(expected = IllegalStateException.class)
	public void addAlternativeTest_StatusAVAILABLE() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t2 = branchOffice.getPh().getProjects().get(0).getTasks().get(1);
		t.addAlternative(t2);

	}

	@Test(expected = IllegalStateException.class)
	public void addAlternativeTest_StatusUNAVAILABLE() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t2 = branchOffice.getPh().getProjects().get(0).getTasks().get(1);
		assertEquals(t.getStatusName(), "UNAVAILABLE");
		t.addAlternative(t2);
	}

	@Test
	public void addAlternativeTest_StatusFAILED() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(true, startTime, endTime);
		assertEquals(t.getStatusName(), "FAILED");
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t2 = branchOffice.getPh().getProjects().get(0).getTasks().get(1);

		t.addAlternative(t2);
		assertEquals(t.getAlternative(), t2);
		assertEquals(t.getStatusName(), "FAILED");
	}

	@Test(expected = IllegalStateException.class)
	public void addAlternativeTest_StatusFINISHED() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null,1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "FINISHED");
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t2 = branchOffice.getPh().getProjects().get(0).getTasks().get(1);

		t.addAlternative(t2);
	}

	@Test
	public void isAvailableTest_StatusPLANNED() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		assertEquals(t.getStatusName(), "PLANNED");
		assertTrue(t.isPlanned());

	}

	@Test
	public void isAvailableTest_StatusUNAVAILABLE() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		assertEquals(t.getStatusName(), "UNAVAILABLE");
		assertFalse(t.isAvailable());
	}

	@Test
	public void isAvailableTest_StatusFINISHED() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

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
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

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
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		assertFalse(t.isAvailable());
	}

	@Test
	public void isFinishedTest_StatusAVAILABLE() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		assertFalse(t.isFinished());

	}

	@Test
	public void isFinishedTest_StatusUNAVAILABLE() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null,1);
		NormalTask t2 = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		assertFalse(t2.isFinished());
		assertEquals(t2.getStatusName(), "UNAVAILABLE");
	}

	@Test
	public void isFinishedTest_StatusFINISHED() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

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
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

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
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null,1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		assertFalse(t.isFinished());
	}

	@Test
	public void isFailedTest_StatusPLANNED() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertFalse(t.isFailed());
		assertEquals(t.getStatusName(), "PLANNED");

	}

	@Test
	public void isFailedTest_StatusUNAVAILABLE() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		assertFalse(t.isFailed());
		assertEquals(t.getStatusName(), "UNAVAILABLE");
	}

	@Test
	public void isFailedTest_StatusFINISHED() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

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
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

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
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		assertFalse(t.isFailed());
	}

	@Test
	public void isCompletedTest_StatusAVAILABLE() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertFalse(t.isCompleted());
	}

	@Test
	public void isCompletedTest_StatusUNAVAILABLE() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		assertFalse(t.isCompleted());
	}

	@Test
	public void isCompletedTest_StatusFAILED() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(true, startTime, endTime);
		assertTrue(t.isCompleted());
	}

	@Test
	public void isCompletedTest_StatusFINISHED() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(false, startTime, endTime);
		assertTrue(t.isCompleted());
	}

	@Test
	public void updateTaskAvailabilityTest_TrueCase_StatusPLANNED() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");

		t.update();
		assertEquals(t.getStatusName(), "AVAILABLE");
	}

	@Test(expected = IllegalStateException.class)
	public void updateTaskAvailabilityTest_TrueCase_StatusFINISHED() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(false, startTime, endTime);
		t.update();
	}

	@Test(expected = IllegalStateException.class)
	public void updateTaskAvailabilityTest_FalseCase_StatusFAILED() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null,1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(true, startTime, endTime);
		t.update();

	}

	@Test(expected = IllegalStateException.class)
	public void updateTaskAvailabilityTest_FalseCase_StatusEXECUTING() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "EXECUTING");
		t.update();

	}

	@Test
	public void updateTaskAvailabitlityTest_DependantUpdate() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t1 = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		ArrayList<NormalTask> l = new ArrayList<NormalTask>();
		l.add(t1);
		p.addTask(description, estimatedDuration, acceptableDeviation, l, null, null, 1);
		NormalTask t2 = branchOffice.getPh().getProjects().get(0).getTasks().get(1);
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

		t1.update();
		assertEquals(t1.getStatusName(), "PLANNED");
		assertEquals(t2.getStatusName(), "PLANNED");

	}

	@Test
	public void updateTaskAvailabilityTest_DeveloperAvailableTest() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t1 = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name developer 1", new LocalTime(8, 0),
				new LocalTime(23, 0));
		d.addReservation(t1, new TimeSpan(new DateTime(2015, 10, 12, 11, 11),
				new DateTime(2015, 10, 14, 10, 0)));
		t1.addRequiredDeveloper(d);

		Developer d2 = new Developer("name developer 1", new LocalTime(13, 0),
				new LocalTime(23, 0));
		t1.addRequiredDeveloper(d2);
		t1.update();
	}

	@Test
	public void updateTaskAvailabilityTest_ResourceAvailableTest() {

		ResourceType rt = new ResourceType("name resourceTyp 1", null, null,
				false);
		rt.addResource("1", new LocalTime(8, 0), new LocalTime(23, 0));
		rt.addResource("2", new LocalTime(16, 0), new LocalTime(23, 0));

		LinkedHashMap<ResourceType, Integer> l = new LinkedHashMap<ResourceType, Integer>();
		l.put(rt, 2);
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, l, 1);
		NormalTask t1 = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
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

		t1.update();
	}

	@Test
	public void updateTaskAvailabilityTest_FAILEDTask_Update() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t1 = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d1 = new Developer("name", new LocalTime(8, 0),
				new LocalTime(16, 0));
		d1.addReservation(t1, new TimeSpan(new DateTime(2015, 10, 12, 8, 11),
				new DateTime(2015, 10, 14, 10, 0)));
		t1.addRequiredDeveloper(d1);
		t1.update();
		assertEquals(t1.getStatusName(), "PLANNED");

		t1.executeTask();
		assertEquals(t1.getStatusName(), "EXECUTING");
		t1.addTimeSpan(true, new DateTime(2015, 10, 12, 8, 0), new DateTime(
				2015, 10, 12, 16, 0));
		assertEquals(t1.getStatusName(), "FAILED");
		p.addTask(description, estimatedDuration, acceptableDeviation, null, t1, null, 1);
		NormalTask t2 = branchOffice.getPh().getProjects().get(0).getTasks().get(1);

		Developer d2 = new Developer("name", new LocalTime(8, 0),
				new LocalTime(16, 0));
		d2.addReservation(t2, new TimeSpan(new DateTime(2015, 10, 12, 8, 11),
				new DateTime(2015, 10, 14, 10, 0)));
		t2.addRequiredDeveloper(d2);
		t2.update();
		assertEquals(t2.getStatusName(), "PLANNED");

		t2.executeTask();
		assertEquals(t2.getStatusName(), "EXECUTING");

		ArrayList<NormalTask> listTasks = new ArrayList<NormalTask>();
		listTasks.add(t1);
		p.addTask(description, estimatedDuration, acceptableDeviation, listTasks, null, null, 1);
		NormalTask t3 = branchOffice.getPh().getProjects().get(0).getTasks().get(2);
		t3.update();

	}

	@Test
	public void updateTaskAvailabilityTest_isAlternativeFinishedTest_NoAlternative() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t1 = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d1 = new Developer("name", new LocalTime(8, 0),
				new LocalTime(16, 0));
		d1.addReservation(t1, new TimeSpan(new DateTime(2015, 10, 12, 8, 11),
				new DateTime(2015, 10, 14, 10, 0)));
		t1.addRequiredDeveloper(d1);
		t1.update();
		assertEquals(t1.getStatusName(), "PLANNED");
		t1.executeTask();
		assertEquals(t1.getStatusName(), "EXECUTING");
		t1.addTimeSpan(true, new DateTime(2015, 10, 12, 8, 0), new DateTime(
				2015, 10, 12, 16, 0));
		assertEquals(t1.getStatusName(), "FAILED");

		ArrayList<NormalTask> dependencies = new ArrayList<NormalTask>();
		dependencies.add(t1);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t2 = branchOffice.getPh().getProjects().get(0).getTasks().get(1);
		t2.update();

	}

	@Test
	public void updateTaskAvailabilityTest_isAlternativeFinishedTest_AlternativeFINISHED() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t1 = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d1 = new Developer("name", new LocalTime(8, 0),
				new LocalTime(16, 0));
		d1.addReservation(t1, new TimeSpan(new DateTime(2015, 10, 12, 8, 11),
				new DateTime(2015, 10, 14, 10, 0)));
		t1.addRequiredDeveloper(d1);
		t1.update();
		assertEquals(t1.getStatusName(), "PLANNED");
		t1.executeTask();
		assertEquals(t1.getStatusName(), "EXECUTING");
		t1.addTimeSpan(true, new DateTime(2015, 10, 12, 8, 0), new DateTime(
				2015, 10, 12, 16, 0));
		assertEquals(t1.getStatusName(), "FAILED");
		p.addTask(description, estimatedDuration, acceptableDeviation, null, t1, null, 1);
		NormalTask t3 = branchOffice.getPh().getProjects().get(0).getTasks().get(1);
		Developer d2 = new Developer("name", new LocalTime(8, 0),
				new LocalTime(16, 0));
		d2.addReservation(t3, new TimeSpan(new DateTime(2015, 10, 12, 8, 11),
				new DateTime(2015, 10, 14, 10, 0)));
		t3.addRequiredDeveloper(d2);
		t3.update();
		assertEquals(t3.getStatusName(), "PLANNED");
		t3.executeTask();
		assertEquals(t3.getStatusName(), "EXECUTING");
		t3.addTimeSpan(false, new DateTime(2015, 10, 12, 8, 0), new DateTime(
				2015, 10, 12, 16, 0));
		assertEquals(t3.getStatusName(), "FINISHED");

		ArrayList<NormalTask> dependencies = new ArrayList<NormalTask>();
		dependencies.add(t1);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t2 = branchOffice.getPh().getProjects().get(0).getTasks().get(2);
		t2.update();

	}

	@Test
	public void updateTaskAvailabilityTest_isAlternativeFinishedTest_AlternativeFAILED() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t1 = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d1 = new Developer("name", new LocalTime(8, 0),
				new LocalTime(16, 0));
		d1.addReservation(t1, new TimeSpan(new DateTime(2015, 10, 12, 8, 11),
				new DateTime(2015, 10, 14, 10, 0)));
		t1.addRequiredDeveloper(d1);
		t1.update();
		assertEquals(t1.getStatusName(), "PLANNED");
		t1.executeTask();
		assertEquals(t1.getStatusName(), "EXECUTING");
		t1.addTimeSpan(true, new DateTime(2015, 10, 12, 8, 0), new DateTime(
				2015, 10, 12, 16, 0));
		assertEquals(t1.getStatusName(), "FAILED");
		p.addTask(description, estimatedDuration, acceptableDeviation, null, t1, null, 1);
		NormalTask t3 = branchOffice.getPh().getProjects().get(0).getTasks().get(1);

		Developer d2 = new Developer("name", new LocalTime(8, 0),
				new LocalTime(16, 0));
		d2.addReservation(t3, new TimeSpan(new DateTime(2015, 10, 12, 8, 11),
				new DateTime(2015, 10, 14, 10, 0)));
		t3.addRequiredDeveloper(d2);
		t3.update();
		assertEquals(t3.getStatusName(), "PLANNED");
		t3.executeTask();
		assertEquals(t3.getStatusName(), "EXECUTING");
		t3.addTimeSpan(true, new DateTime(2015, 10, 12, 8, 0), new DateTime(
				2015, 10, 12, 16, 0));
		assertEquals(t3.getStatusName(), "FAILED");

		ArrayList<NormalTask> dependencies = new ArrayList<NormalTask>();
		dependencies.add(t1);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t2 = branchOffice.getPh().getProjects().get(0).getTasks().get(1);
		t2.update();

	}

	@Test
	public void calculateTotalExecutionTimeTest_TrueCase() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2015, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getTotalExecutionTime(), 60);
	}

	@Test(expected = IllegalStateException.class)
	public void calculateTotalExecutionTimeTest_StatusUNAVAILABLE() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		int time = t.getTotalExecutionTime();
	}

	@Test
	public void calculateTotalExecutionTimeTest_StatusFAILED() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2015, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(true, startTime, endTime);
		assertEquals(t.getTotalExecutionTime(), 60);
	}

	@Test
	public void calculateTotalExecutionTimeTest_StatusFINISHEED() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2015, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "FINISHED");
		int time = t.getTotalExecutionTime();
		assertEquals(time, 60);
	}

	@Test
	public void calculateOverduePercentageTest_TrueCase() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

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
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertTrue(t.isPlanned());
	}

	@Test
	public void isPlannedTest_FalseCase() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		assertFalse(t.isPlanned());
	}

	@Test
	public void isSeveralyOverDueTest_StatusUNAVAILABLE() {

	}

	@Test(expected = IllegalStateException.class)
	public void isSeveralyOverDueTest_StatusAVAILABLE() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		t.isSeverelyOverdue();
	}

	@Test
	public void isSeveralyOverDueTest_StatusFINISHED() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, 0, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 9, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		DateTime startTime = new DateTime(2015, 1, 1, 10, 0);
		DateTime endTime = new DateTime(2015, 1, 1, 12, 0);
		t.executeTask();
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "FINISHED");
		assertTrue(t.isSeverelyOverdue());
	}

	@Test
	public void isSeveralyOverDueTest_StatusFAILED() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, 0, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 9, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		DateTime startTime = new DateTime(2015, 1, 1, 10, 0);
		DateTime endTime = new DateTime(2015, 1, 1, 12, 0);
		t.executeTask();
		t.addTimeSpan(true, startTime, endTime);
		assertEquals(t.getStatusName(), "FAILED");
		assertTrue(t.isSeverelyOverdue());

	}

	@Test(expected = IllegalStateException.class)
	public void isSeveralyOverDueTest_StatusEXECUTING() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, 0, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 9, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		assertTrue(t.isSeverelyOverdue());
	}
	//TODO
	@Test
	public void createMomentoTest() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, 0, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		assertTrue(t.createMemento() instanceof NormalTaskMemento);
	}

	@Test
	public void getDevelopersTest() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, 0, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		List<Developer> ld = t.getRequiredDevelopers();
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		ld.add(d);
		assertNotEquals(t.getRequiredDevelopers(), ld);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addDeveloperTest_Null() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, 0, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		t.addRequiredDeveloper(null);
	}

	@Test
	public void addDeveloperTest_TrueCase() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, 0, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer developer = new Developer("1", new LocalTime(8, 0),
				new LocalTime(16, 0));

		t.addRequiredDeveloper(developer);
		assertEquals(t.getRequiredDevelopers().get(0), developer);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addDeveloperTest_AlreadyInTask() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, 0, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer developer = new Developer("1", new LocalTime(8, 0),
				new LocalTime(16, 0));
		t.addRequiredDeveloper(developer);
		t.addRequiredDeveloper(developer);

	}

	@Test(expected = IllegalArgumentException.class)
	public void attachDependantTest_Null() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, 0, null, null, null, 1);
		NormalTask t1 = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		t1.attachDependant(null);
	}

	@Test
	public void attachDependantTest() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, 0, null, null, null, 1);
		NormalTask t1 = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		p.addTask(description, estimatedDuration, 0, null, null, null, 1);
		NormalTask t2 = branchOffice.getPh().getProjects().get(0).getTasks().get(1);
		t1.attachDependant(t2);
	}

	@Test
	public void gerRequiredResourceTest() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, 0, null, null, null,1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
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
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, map, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
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
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, 0, null, null, map, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
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
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, 0, null, null, map, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
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
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, 0, null, null, map, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
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
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, 0, null, null, map, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

	}

	@Test
	public void setMomenToTest_Status_UNAVAILABLE() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		NormalTaskMemento tm = t.createMemento();
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");

		t.setMemento(tm);
		assertEquals(t.getDependencies(), dependencies);
		assertNull(t.getAlternative());
		assertEquals(t.getStatusName(), "UNAVAILABLE");

	}

	@Test
	public void setMomenToTest_Status_AVAILABLE() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		NormalTaskMemento tm = t.createMemento();
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");

		t.setMemento(tm);
		assertEquals(t.getDependencies(), dependencies);
		assertNull(t.getAlternative());
		assertEquals(t.getStatusName(), "PLANNED");

	}

	@Test
	public void setMomenToTest_Status_FINISHED() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");

		t.addTimeSpan(false, new DateTime(2015, 10, 14, 10, 0), new DateTime(
				2015, 10, 14, 13, 0));
		assertEquals(t.getStatusName(), "FINISHED");
		NormalTaskMemento tm = t.createMemento();
		t.setMemento(tm);
		assertEquals(t.getDependencies(), dependencies);
		assertNull(t.getAlternative());
		assertEquals(t.getStatusName(), "FINISHED");

	}

	@Test
	public void setMomenToTest_Status_FAILED() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null,1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");

		t.addTimeSpan(true, new DateTime(2015, 10, 14, 10, 0), new DateTime(
				2015, 10, 14, 13, 0));
		assertEquals(t.getStatusName(), "FAILED");
		NormalTaskMemento tm = t.createMemento();
		t.setMemento(tm);
		assertEquals(t.getDependencies(), dependencies);
		assertNull(t.getAlternative());
		assertEquals(t.getStatusName(), "FAILED");

	}

	@Test
	public void setMomenToTest_Status_EXECUTING() {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");

		NormalTaskMemento tm = t.createMemento();
		t.addTimeSpan(true, new DateTime(2015, 10, 14, 10, 0), new DateTime(
				2015, 10, 14, 13, 0));
		assertEquals(t.getStatusName(), "FAILED");
		t.setMemento(tm);
		assertEquals(t.getDependencies(), dependencies);
		assertNull(t.getAlternative());
		assertEquals(t.getStatusName(), "EXECUTING");

	}

	@Test
	public void isAvailableTest_StatusPlanned() {
		clock.advanceSystemTime(new DateTime(2015,10,12,9,0));
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		assertEquals(t.getStatusName(), "PLANNED");
		assertFalse(t.isAvailable());
	}

	public void isAlternativeFinished_Status_UNAVAILABLE(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		assertEquals(t.getStatusName(), "UNAVAILABLE");
		assertFalse(t.isAlternativeFinished());
	}

	@Test (expected = IllegalStateException.class)
	public void isSeverelyOverdue_Status_UNAVAILABLE(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		assertEquals(t.getStatusName(), "UNAVAILABLE");
		assertFalse(t.isSeverelyOverdue());
	}

	@Test (expected = IllegalStateException.class)
	public void calculateOverDuePercentage_Status_UNAVAILABLE(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		assertEquals(t.getStatusName(), "UNAVAILABLE");
		t.getOverduePercentage();
	}

	@Test (expected = IllegalStateException.class)
	public void executeTask_Status_UNAVAILABLE(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		assertEquals(t.getStatusName(), "UNAVAILABLE");
		t.executeTask();
	}

	@Test (expected = IllegalArgumentException.class)
	public void constructor_Null_Clock(){
		NormalTask t = new NormalTask(null, "description", 10, 1, null, null, null, 1);
	}

	@Test
	public void isExecuting_Status_FINISHED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, 0, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 9, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		DateTime startTime = new DateTime(2015, 1, 1, 10, 0);
		DateTime endTime = new DateTime(2015, 1, 1, 12, 0);
		t.executeTask();
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "FINISHED");
		assertFalse(t.isExecuting());
	}

	@Test (expected = IllegalStateException.class)
	public void addTimeSpan_Status_FINISHED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, 0, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 9, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		DateTime startTime = new DateTime(2015, 1, 1, 10, 0);
		DateTime endTime = new DateTime(2015, 1, 1, 12, 0);
		t.executeTask();
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "FINISHED");
		t.addTimeSpan(true, startTime, endTime);
	} 

	@Test
	public void isAlternativeFinished_Status_FINISHED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, 0, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 9, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		DateTime startTime = new DateTime(2015, 1, 1, 10, 0);
		DateTime endTime = new DateTime(2015, 1, 1, 12, 0);
		t.executeTask();
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "FINISHED");
		assertFalse(t.isAlternativeFinished());
	}

	@Test (expected = IllegalStateException.class)
	public void addAlternative_Status_FINISHED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, 0, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 9, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		DateTime startTime = new DateTime(2015, 1, 1, 10, 0);
		DateTime endTime = new DateTime(2015, 1, 1, 12, 0);
		t.executeTask();
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "FINISHED");
		t.addAlternative(t);
	}

	@Test (expected = IllegalStateException.class)
	public void executeTask_Status_FINISHED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, 0, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 9, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		DateTime startTime = new DateTime(2015, 1, 1, 10, 0);
		DateTime endTime = new DateTime(2015, 1, 1, 12, 0);
		t.executeTask();
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "FINISHED");
		t.executeTask();
	}

	@Test (expected = IllegalStateException.class)
	public void delegateTask_Status_FINISHED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, 0, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 9, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		DateTime startTime = new DateTime(2015, 1, 1, 10, 0);
		DateTime endTime = new DateTime(2015, 1, 1, 12, 0);
		t.executeTask();
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "FINISHED");
		t.delegateTask();
	}

	@Test
	public void isExecuting_Status_FAILED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null,1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");

		t.addTimeSpan(true, new DateTime(2015, 10, 14, 10, 0), new DateTime(
				2015, 10, 14, 13, 0));
		assertEquals(t.getStatusName(), "FAILED");
		assertFalse(t.isExecuting());
	}

	@Test
	public void isPlanned_Status_FAILED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null,1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");

		t.addTimeSpan(true, new DateTime(2015, 10, 14, 10, 0), new DateTime(
				2015, 10, 14, 13, 0));
		assertEquals(t.getStatusName(), "FAILED");
		assertTrue(t.isPlanned());
	}


	@Test (expected = IllegalStateException.class)
	public void addTimeSpan_Status_FAILED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null,1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");

		t.addTimeSpan(true, new DateTime(2015, 10, 14, 10, 0), new DateTime(
				2015, 10, 14, 13, 0));
		assertEquals(t.getStatusName(), "FAILED");
		t.addTimeSpan(true, new DateTime(2015, 10, 14, 10, 0), new DateTime(
				2015, 10, 14, 13, 0));
	}

	@Test
	public void isAlternativeFinished_Status_FAILED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null,1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");

		t.addTimeSpan(true, new DateTime(2015, 10, 14, 10, 0), new DateTime(
				2015, 10, 14, 13, 0));
		assertEquals(t.getStatusName(), "FAILED");
		assertFalse(t.isAlternativeFinished());
	}

	@Test (expected = IllegalStateException.class)
	public void executeTask_Status_FAILED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null,1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");

		t.addTimeSpan(true, new DateTime(2015, 10, 14, 10, 0), new DateTime(
				2015, 10, 14, 13, 0));
		assertEquals(t.getStatusName(), "FAILED");
		t.executeTask();
	}
	@Test (expected = IllegalStateException.class)
	public void delegateTask_Status_FAILED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null,1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");

		t.addTimeSpan(true, new DateTime(2015, 10, 14, 10, 0), new DateTime(
				2015, 10, 14, 13, 0));
		assertEquals(t.getStatusName(), "FAILED");
		t.delegateTask();
	}

	@Test
	public void isExecuting_Status_EXECUTING(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		assertTrue(t.isExecuting());
	}

	@Test
	public void isPlanned_Status_EXECUTING(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		assertTrue(t.isPlanned());
	}

	@Test
	public void isDelegated_Status_EXECUTING(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		assertFalse(t.isDelegated());
	}
	@Test 	public void updateStatus_Status_EXECUTING(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.update();
		assertEquals(t.getStatusName(),"EXECUTING");
	}

	@Test (expected = IllegalArgumentException.class)
	public void addTimeSpan_Status_EXECUTING_StartTime_Null(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(true, null, new DateTime(2015, 10, 13, 16, 0));
	}

	@Test (expected = IllegalArgumentException.class)
	public void addTimeSpan_Status_EXECUTING_EndTime_Null(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(true,new DateTime(2015, 10, 13, 16, 0), null);
	}

	@Test (expected = IllegalDateException.class)
	public void addTimeSpan_Status_EXECUTING_StartTimeAfterEndTime(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(true,  new DateTime(2016, 10, 13, 16, 0), new DateTime(2015, 10, 13, 16, 0));
	}

	@Test
	public void isAlternativeFinished(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		assertFalse(t.isAlternativeFinished());
	}

	@Test (expected = IllegalStateException.class)
	public void addAlternative_Status_EXECUTING(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addAlternative(t);
	}
	@Test (expected = IllegalStateException.class)
	public void isSeveralOverdue_Status_EXECUTING(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.isSeverelyOverdue();
	}

	@Test (expected = IllegalStateException.class)
	public void calculateTotalExecutionTime_Status_EXECUTING(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.getTotalExecutionTime();
	}

	@Test (expected = IllegalStateException.class)
	public void calculateOverduePercentage_Status_EXECUTING(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.getOverduePercentage();
	}

	@Test (expected = IllegalStateException.class)
	public void executeTask_Status_EXECUTING(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.executeTask();
	}

	@Test (expected = IllegalStateException.class)
	public void delegateTask_Status_EXECUTING(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.delegateTask();
	}

	@Test (expected = IllegalArgumentException.class)
	public void completeTask_StartTime_Null(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.completeTask(true, null,new DateTime(2015, 10, 13, 16, 0) );
	}

	@Test (expected = IllegalArgumentException.class)
	public void completeTask_EndTime_Null(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);


		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");
		t.executeTask();
		assertEquals(t.getStatusName(), "EXECUTING");
		t.completeTask(true,new DateTime(2015, 10, 13, 16, 0), null);
	}

	@Test
	public void isAvailable_Status_DELEGATED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		t.delegateTask();
		assertEquals(t.getStatusName(), "DELEGATED");
		assertFalse(t.isAvailable());
	}
	@Test
	public void isFinished_Status_DELEGATED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		t.delegateTask();
		assertEquals(t.getStatusName(), "DELEGATED");
		assertFalse(t.isFinished());
	}

	@Test
	public void isFailed_Status_DELEGATED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		t.delegateTask();
		assertEquals(t.getStatusName(), "DELEGATED");
		assertFalse(t.isFailed());
	}

	@Test
	public void isFExecuting_Status_DELEGATED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		t.delegateTask();
		assertEquals(t.getStatusName(), "DELEGATED");
		assertFalse(t.isExecuting());
	}


	@Test
	public void isPlanned_Status_DELEGATED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		t.delegateTask();
		assertEquals(t.getStatusName(), "DELEGATED");
		assertFalse(t.isPlanned());
	}
	@Test
	public void isDelegated_Status_DELEGATED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		t.delegateTask();
		assertEquals(t.getStatusName(), "DELEGATED");
		assertTrue(t.isDelegated());
	}

	@Test //(expected = IllegalStateException.class)
	public void updateStatus_Status_DELEGATED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		t.delegateTask();
		assertEquals(t.getStatusName(), "DELEGATED");
		t.update();
	}

	@Test (expected = IllegalStateException.class)
	public void getTimeSpan_Status_DELEGATED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		t.delegateTask();
		assertEquals(t.getStatusName(), "DELEGATED");
		t.getTimeSpan();
	}

	@Test 
	public void addTimeSpan_Failed_Status_PLANNED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		t.delegateTask();
		assertEquals(t.getStatusName(), "DELEGATED");
		t.addTimeSpan(true, new DateTime(2015,02,20,10,0),  new DateTime(2015,02,20,12,0));
		assertEquals(t.getStatusName(), "FAILED");
		assertEquals(t.getTimeSpan().getStartTime(),new DateTime(2015,02,20,10,0));
		assertEquals(t.getTimeSpan().getEndTime(), new DateTime(2015,02,20,12,0));
	}

	@Test 
	public void addTimeSpan_Finished_Status_PLANNED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		t.delegateTask();
		assertEquals(t.getStatusName(), "DELEGATED");
		t.addTimeSpan(false, new DateTime(2015,02,20,10,0),  new DateTime(2015,02,20,12,0));
		assertEquals(t.getStatusName(), "FINISHED");
		assertEquals(t.getTimeSpan().getStartTime(),new DateTime(2015,02,20,10,0));
		assertEquals(t.getTimeSpan().getEndTime(), new DateTime(2015,02,20,12,0));
	}

	@Test (expected = IllegalStateException.class)
	public void addAlternative_Status_DELEGATED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		t.delegateTask();
		assertEquals(t.getStatusName(), "DELEGATED");
		t.addAlternative(t);
	}
	
	@Test (expected = IllegalStateException.class)
	public void isSeveralOverdue_Status_PLANNED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		t.delegateTask();
		assertEquals(t.getStatusName(), "DELEGATED");
		t.isSeverelyOverdue();
	}
	@Test (expected = IllegalStateException.class)
	public void icalculateTotaleExecutionTime_Status_PLANNED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		t.delegateTask();
		assertEquals(t.getStatusName(), "DELEGATED");
		t.getTotalExecutionTime();
	}
	
	@Test (expected = IllegalStateException.class)
	public void calculateOverduePercentages_Status_PLANNED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		t.delegateTask();
		assertEquals(t.getStatusName(), "DELEGATED");
		t.getOverduePercentage();
	}
	
	@Test (expected = IllegalStateException.class)
	public void executeTask_Status_PLANNED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		t.delegateTask();
		assertEquals(t.getStatusName(), "DELEGATED");
		t.executeTask();
	}
	
	@Test (expected = IllegalStateException.class)
	public void delegateTask_Statuss_PLANNED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		t.delegateTask();
		assertEquals(t.getStatusName(), "DELEGATED");
		t.delegateTask();
	}
	@Test
	public void isExecuting_Status_PLANNED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		Task t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		assertEquals(t.getDescription(), description);
		assertEquals(t.getEstimatedDuration(), estimatedDuration);
		assertEquals(t.getAcceptableDeviation(), acceptableDeviation);
		assertTrue(((NormalTask) t).getDependencies().isEmpty());
		assertEquals(t.getRequiredDevelopers().get(0), d);
		assertTrue(t.getRequiredResourceTypes().isEmpty());
		assertNull(((NormalTask) t).getAlternative());
		assertEquals(t.getStatusName(), "PLANNED");
		assertFalse(t.isExecuting());
	}

	@Test (expected = IllegalStateException.class)
	public void calculateTotalExecutionTime_Status_PLANNED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		Task t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		assertEquals(t.getDescription(), description);
		assertEquals(t.getEstimatedDuration(), estimatedDuration);
		assertEquals(t.getAcceptableDeviation(), acceptableDeviation);
		assertTrue(((NormalTask) t).getDependencies().isEmpty());
		assertEquals(t.getRequiredDevelopers().get(0), d);
		assertTrue(t.getRequiredResourceTypes().isEmpty());
		assertNull(((NormalTask) t).getAlternative());
		assertEquals(t.getStatusName(), "PLANNED");
		t.getTotalExecutionTime();
	}

	@Test (expected = IllegalStateException.class)
	public void calculateOverduePercentage_Status_PLANNED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		Task t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		assertEquals(t.getDescription(), description);
		assertEquals(t.getEstimatedDuration(), estimatedDuration);
		assertEquals(t.getAcceptableDeviation(), acceptableDeviation);
		assertTrue(((NormalTask) t).getDependencies().isEmpty());
		assertEquals(t.getRequiredDevelopers().get(0), d);
		assertTrue(t.getRequiredResourceTypes().isEmpty());
		assertNull(((NormalTask) t).getAlternative());
		assertEquals(t.getStatusName(), "PLANNED");
		t.getOverduePercentage();
	}

	@Test (expected = IllegalStateException.class)
	public void delegateTask_Status_PLANNED(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		Task t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		assertEquals(t.getDescription(), description);
		assertEquals(t.getEstimatedDuration(), estimatedDuration);
		assertEquals(t.getAcceptableDeviation(), acceptableDeviation);
		assertTrue(((NormalTask) t).getDependencies().isEmpty());
		assertEquals(t.getRequiredDevelopers().get(0), d);
		assertTrue(t.getRequiredResourceTypes().isEmpty());
		assertNull(((NormalTask) t).getAlternative());
		assertEquals(t.getStatusName(), "PLANNED");
		t.delegateTask();
	}
	
	@Test
	public void isAvailable_Status_AVAILABLE(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");

		t.update();
		assertEquals(t.getStatusName(), "AVAILABLE");
		assertTrue(t.isAvailable());
	}
	@Test
	public void isFailed_Status_AVAILABLE(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");

		t.update();
		assertEquals(t.getStatusName(), "AVAILABLE");
		assertFalse(t.isFailed());
	}
	
	@Test
	public void isExecuting_Status_AVAILABLE(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");

		t.update();
		assertEquals(t.getStatusName(), "AVAILABLE");
		assertFalse(t.isExecuting());
	}
	
	@Test
	public void isPlanned_Status_AVAILABLE(){
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);

		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		assertEquals(t.getStatusName(), "PLANNED");

		t.update();
		assertEquals(t.getStatusName(), "AVAILABLE");
		assertFalse(t.isPlanned());
	}
	//TODO
	@Test
	public void updateStatus_Status_AVAILABLE(){
		//FINISHED Task
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, null, null, null,1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();

		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "EXECUTING");
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(), "FINISHED");
		
		// Create task available
		ArrayList<NormalTask> list = new ArrayList<NormalTask>();
		list.add(t);
		//ArrayList<ResourceType> t = new ArrayList<ResourceType>();
		//rlist.add(branchOffice.getRh().getResourceTypes().get(0));
		HashMap<ResourceType, Integer> rlist = new HashMap<ResourceType, Integer>();
		rlist.put(branchOffice.getRh().getResourceTypes().get(0), 1);
		p.addTask(description, estimatedDuration, acceptableDeviation, list,null, rlist, 1);
		NormalTask t1 = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		branchOffice.getRh().getResourceTypes().get(0).getResources().get(0).addReservation(t1, new TimeSpan(new DateTime(2000,10,15,10,0), new DateTime(2000,10,15,12,0)));

		Developer d1 = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan1 = new TimeSpan(new DateTime(2015, 10, 13, 8, 0),
				new DateTime(2015, 10, 13, 16, 0));
		d1.addReservation(t, timeSpan);
		t1.addRequiredDeveloper(d);
		t1.update();

		assertEquals(t1.getStatusName(), "PLANNED");

		t1.update();
		assertEquals(t1.getStatusName(), "AVAILABLE");
	}
}