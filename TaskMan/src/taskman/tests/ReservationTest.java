package taskman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.project.Project;
import taskman.model.resource.ResourceType;
import taskman.model.task.Reservation;
import taskman.model.task.Task;
import taskman.model.time.Clock;
import taskman.model.time.TimeSpan;

public class ReservationTest {
	private Task task;
	private TimeSpan ts;
	private Clock clock = new Clock();
	private Company company = new Company();
	List<ResourceType> list = new ArrayList<ResourceType>();
	private BranchOffice branchOffice = new BranchOffice(company, "", list);
	@Before
	public void setUp() throws Exception {
		branchOffice .getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask("", 10, 1, null, null, null, 1);
		ts = new TimeSpan(new DateTime(2015,10,12,10,0), new DateTime(2015,10,12,16,0));
	}

	@Test (expected = NullPointerException.class)
	public void constructorTest_Task_Null(){
		Reservation r = new Reservation(null, ts);
	}
	
	@Test (expected = NullPointerException.class)
	public void constructorTest_TimeSpan_Null(){
		Reservation r = new Reservation(branchOffice.getPh().getProjects().get(0).getTasks().get(0), null);	
	}
	
	@Test 
	public void constructorTest_TrueCase(){
		Reservation r = new Reservation(branchOffice.getPh().getProjects().get(0).getTasks().get(0), ts);
		assertEquals(r.getTask(), branchOffice.getPh().getProjects().get(0).getTasks().get(0));
		assertEquals(r.getTimeSpan(), ts);
	}

}
