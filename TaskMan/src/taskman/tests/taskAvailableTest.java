package taskman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.project.Project;
import taskman.model.resource.ResourceType;
import taskman.model.task.NormalTask;
import taskman.model.task.Task;
import taskman.model.time.Clock;
import taskman.model.time.TimeService;
import taskman.model.time.TimeSpan;
import taskman.model.user.Developer;

public class taskAvailableTest {
	private String description;
	private int estimatedDuration;
	private int acceptableDeviation;
	private List<NormalTask> dependencies;
	private TimeSpan timespan;
	private Clock clock = new Clock();
	private Company company;
	List<ResourceType> list =  new ArrayList<ResourceType>();
	private BranchOffice branchOffice;
	TimeService timeService = new TimeService();

	
	@Before
	public void setup(){
		description = "description";
		estimatedDuration = 10;
		acceptableDeviation = 1;
		dependencies = new ArrayList<NormalTask>();
		clock.setSystemTime(new DateTime(2015, 10, 12, 10, 0));
		company = new Company();
		ResourceType rt = new ResourceType("name", null, null, false);
		rt.addResource("n", new LocalTime(9,0), new LocalTime(16,0));
		list.add(rt);
		branchOffice = new BranchOffice(company, "", list);
		branchOffice.getPh().addProject("", "", new DateTime(2015, 10, 11, 9, 0), new DateTime(2015, 10, 22, 16, 0));
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null, null, 1);
		Task t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8, 0), new LocalTime(
				16, 0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 9, 0),
				new DateTime(2015, 10, 12, 16, 0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
	}
	@Test(expected = IllegalStateException.class)
	public void getTimeSpanTest(){
		Task t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		t.update();
		t.update();
		assertEquals("AVAILABLE", t.getStatusName());
		t.getTimeSpan();
	}
	@Test
	public void getIsAvailable(){
		Task t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		t.update();
		t.update();
		assertEquals(true, t.isAvailable());
	}
	@Test
	public void getIsFailed(){
		Task t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		t.update();
		t.update();
		assertEquals(false, t.isFailed());
	}
	@Test
	public void getIsExecuting(){
		Task t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		t.update();
		t.update();
		assertEquals(false, t.isExecuting());
	}
	@Test
	public void getIsPlanned(){
		Task t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		t.update();
		t.update();
		assertEquals(true, t.isPlanned());
	}
	@Test(expected=IllegalStateException.class)
	public void addTimeSpamTest(){
		Task t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		t.update();
		t.update();
		t.addTimeSpan(false, new DateTime(), new DateTime());
	}
	@Test(expected = IllegalStateException.class)
	public void severlyOverdueTest(){
		Task t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		t.update();
		t.update();
		t.isSeverelyOverdue();
	}
	@Test(expected = IllegalStateException.class)
	public void calculateTotalExecuteTimeTest(){
		Task t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		t.update();
		t.update();
		t.getTotalExecutionTime();
	}
	@Test(expected = IllegalStateException.class)
	public void calculateOverduePercentage(){
		Task t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		t.update();
		t.update();
		t.getOverduePercentage();
	}	
	@Test(expected = IllegalStateException.class)
	public void delegateTaskTest(){
		Task t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		t.update();
		t.update();
		t.delegateTask();	
	}
	
	@Test(expected = IllegalStateException.class)
	public void addAlternativeTest(){
		Task t = branchOffice.getPh().getProjects().get(0).getTasks().get(0);
		t.update();
		t.update();
		NormalTask task = new NormalTask(clock, description, estimatedDuration, acceptableDeviation, dependencies, null, null, 0);
		((NormalTask) t).addAlternative(task);
	}
	
}
