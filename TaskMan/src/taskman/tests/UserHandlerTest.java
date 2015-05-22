package taskman.tests;

import static org.junit.Assert.*;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.user.*;
import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.company.UserHandler;
import taskman.model.project.Project;
import taskman.model.task.Task;
import taskman.model.time.*;

public class UserHandlerTest {

	private UserHandler u;
	private Clock clock = new Clock();
	private Company company;
	private BranchOffice branchOffice = new BranchOffice(company, "", null);
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
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask("", 10, 1, null, null, null);
		//Task task = new Task(clock,"description", 10, 1, null, null, null);
		
		Developer d = u.getDevelopers().get(0);
		d.addReservation(branchOffice.getPh().getProjects().get(0).getTasks().get(0), timeSpan);
		
		// ----------------------- One developer has a planning ----------------
		assertEquals(u.getAvailableDevelopers(timeSpan).size(),1);
		
		
	}
	
	@Test
	public void getProjectManagersTest(){
		List<ProjectManager> list = u.getProjectManagers();
		ProjectManager manager = new ProjectManager("name");
		list.add(manager);
		assertNotEquals(u.getProjectManagers(), list);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void addProjectManagerTest_NullName(){
		u.addProjectManager(null);
	}
	
	@Test
	public void addProjectManagerTest_TrueCase(){
		u.addProjectManager("name");
		ProjectManager m = u.getProjectManagers().get(0);
		assertEquals(m.getName(), "name");
	}
	
	@Test
	public void getUsersTest(){
		u.addDeveloper("developer");
		u.addProjectManager("manager");
		
		List<User> list = u.getUsers();
		assertEquals(list.size(),2);
		ProjectManager d = new ProjectManager("name");
		list.add(d);
		assertNotEquals(u.getUsers(), list);
	}

}
