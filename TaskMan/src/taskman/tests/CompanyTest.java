package taskman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.resource.ResourceType;
import taskman.model.task.DelegatedTask;
import taskman.model.task.NormalTask;
import taskman.model.time.Clock;
import taskman.model.time.TimeSpan;
import taskman.model.user.Developer;

public class CompanyTest {
	private Company c;

	@Before
	public void setUp() throws Exception {
		c = new Company();
	}

	@Test
	public void constructorTest() {
		Company c = new Company();
		assertEquals(c.getBranchOffices().size(), 0);
		assertEquals(c.getName(), "SWOP15");
	}

	@Test(expected = IllegalArgumentException.class)
	public void addBranchOfficeTest_NullBranchOffice() {
		c.addBranchOffice(null);
	}

	@Test
	public void addBranchOfficeTest_TrueCase() {
		BranchOffice b = new BranchOffice(c, "new york",
				new ArrayList<ResourceType>());
		c.addBranchOffice(b);
		assertTrue(c.getBranchOffices().contains(b));
	}

	@Test
	public void setDependenciesFinishedTest() {
		BranchOffice b = new BranchOffice(c, "new york",
				new ArrayList<ResourceType>());
		c.addBranchOffice(b);
		UUID id = new UUID(10, 12);

		c.getBranchOffices()
				.get(0)
				.getDth()
				.addDelegatedTask(new UUID(10, 12), "description", 10, 0, null,
						false, 1);
		c.getBranchOffices().get(0).getDth().getDelegatedTasks().get(0)
				.setParentID(id);
		c.setDependenciesFinished(id);
		assertTrue(c.getBranchOffices().get(0).getDth().getDelegatedTasks()
				.get(0).dependenciesAreFinished());
	}

	@Test(expected = IllegalArgumentException.class)
	public void announceCompletion_NullTask() {
		c.announceCompletion(null);
	}

	@Test
	public void announceCompletionTest() {
		List<ResourceType> list = new ArrayList<ResourceType>();
		BranchOffice branchOffice = new BranchOffice(c, "new york", list);
		Clock clock = branchOffice.getClock();
		clock.setSystemTime(new DateTime(2015, 10, 12, 8, 0));
		DelegatedTask task = new DelegatedTask(clock, "description", 10, 0,
				null, false, 1);
		c.addBranchOffice(branchOffice);
		branchOffice.getPh().addProject("name", "description",
				new DateTime(2015, 10, 12, 10, 0),
				new DateTime(2015, 10, 12, 10, 30));
		branchOffice.getPh().getProjects().get(0)
				.addTask("task", 10, 0, null, null, null, 1);
		NormalTask t = branchOffice.getPh().getProjects().get(0).getTasks()
				.get(0);
		Developer d1 = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan1 = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d1.addReservation(t, timeSpan1);
		t.addRequiredDeveloper(d1);
		t.update();
		t.executeTask();
		
		
		
		
		task.setParentID(t.getID());
		
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(task, timeSpan);
		task.addRequiredDeveloper(d);
		task.update();
		task.executeTask();
		assertEquals(task.getStatusName(),"EXECUTING");
		task.addTimeSpan(false, new DateTime(2015,10,12,10,0), new DateTime(2015,10,12,14,0));
		c.announceCompletion(task);
		assertTrue(t.isFinished());

	}

	@Test(expected = IllegalArgumentException.class)
	public void delegateTaskTest_NullTask() {
		c.delegateTask(null, new BranchOffice(c, "new york",
				new ArrayList<ResourceType>()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void delegateTaskTest_NullBranchOffice() {
		BranchOffice b = new BranchOffice(c, "new york",
				new ArrayList<ResourceType>());
		c.addBranchOffice(b);
		c.getBranchOffices()
				.get(0)
				.getPh()
				.addProject("name", "", new DateTime(2015, 10, 12, 10, 0),
						new DateTime(2015, 10, 12, 10, 1));
		c.getBranchOffices().get(0).getPh().getProjects().get(0)
				.addTask("des", 10, 0, null, null, null, 1);
		c.delegateTask(c.getBranchOffices().get(0).getPh().getProjects().get(0)
				.getTasks().get(0), null);
	}

	@Test
	public void delegateTaskTest() {
		BranchOffice b = new BranchOffice(c, "new york",
				new ArrayList<ResourceType>());
		c.addBranchOffice(b);
		c.getBranchOffices()
				.get(0)
				.getPh()
				.addProject("name", "", new DateTime(2015, 10, 12, 10, 0),
						new DateTime(2015, 10, 12, 10, 1));
		c.getBranchOffices().get(0).getPh().getProjects().get(0)
				.addTask("des", 10, 0, null, null, null, 1);

		BranchOffice b2 = new BranchOffice(c, "brussels",
				new ArrayList<ResourceType>());
		c.addBranchOffice(b2);

		assertEquals(b2.getDth().getDelegatedTasks().size(), 0);
		c.delegateTask(c.getBranchOffices().get(0).getPh().getProjects().get(0)
				.getTasks().get(0), b2);
		assertEquals(b2.getDth().getDelegatedTasks().size(), 1);
	}

}
