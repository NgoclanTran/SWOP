package taskman.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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
import taskman.model.time.TimeSpan;
import taskman.model.user.Developer;

public class BranchOfficeTest {

	private Company company;
	BranchOffice b;

	@Before
	public void setup() {
		company = new Company();
		b = new BranchOffice(company, "", new ArrayList<ResourceType>());
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorTest_NullCompany() {
		new BranchOffice(null, "new york", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorTest_NullLocation() {
		new BranchOffice(company, null, null);
	}

	@Test
	public void constructorTest() {
		BranchOffice b = new BranchOffice(company, "new york",
				new ArrayList<ResourceType>());
		assertEquals(b.getCompany(), company);
		assertEquals(b.getLocation(), "new york");
		assertEquals(b.getDth().getDelegatedTasks().size(), 0);
		assertEquals(b.getPh().getProjects().size(), 0);
		assertEquals(b.getRh().getResourceTypes().size(), 0);
		assertEquals(b.getUh().getUsers().size(), 0);
		assertNotNull(b.getMh());
		assertNotNull(b.getClock());

	}

	@Test
	public void toStringTest() {
		assertEquals(b.toString(), "");
	}

	@Test
	public void getCompanyTest() {
		assertEquals(b.getCompany(), company);
	}

	@Test
	public void getLocationTest() {
		assertEquals(b.getLocation(), "");
	}

	@Test
	public void updateTest() {
		b.getPh().addProject("name", "desc",
				new DateTime(2015, 10, 12, 10, 10),
				new DateTime(2015, 10, 12, 14, 10));
		b.getPh().getProjects().get(0)
				.addTask("description", 10, 0, null, null, null, 1);
		NormalTask t = b.getPh().getProjects().get(0).getTasks().get(0);
		t.delegateTask();
		assertTrue(t.isDelegated());
		b.getClock().advanceSystemTime(new DateTime(2015, 10, 12, 10, 0));
		b.getDth().addDelegatedTask(new UUID(100, 120), "description", 10, 0,
				null, true, 1);
		DelegatedTask dt = b.getDth().getDelegatedTasks().get(0);
		Developer d = new Developer("name", new LocalTime(10, 0),
				new LocalTime(14, 0));
		d.addReservation(dt, new TimeSpan(new DateTime(2015, 10, 12, 10, 0),
				new DateTime(2015, 10, 12, 14, 0)));
		dt.addRequiredDeveloper(d);
		dt.update();
		dt.executeTask();
		dt.addTimeSpan(false, new DateTime(2015, 10, 12, 10, 0), new DateTime(
				2015, 10, 12, 10, 30));

		b.update();

	}
}
