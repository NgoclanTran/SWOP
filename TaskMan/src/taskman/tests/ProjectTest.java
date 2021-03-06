package taskman.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import taskman.exceptions.IllegalDateException;
import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.memento.ProjectMemento;
import taskman.model.project.Project;
import taskman.model.resource.ResourceType;
import taskman.model.task.NormalTask;
import taskman.model.task.Task;
import taskman.model.task.TaskFactory;
import taskman.model.time.Clock;
import taskman.model.time.TimeSpan;
import taskman.model.user.Developer;

public class ProjectTest {

	Project project;
	String name = "Name";
	String description = "Description";
	DateTime creation = new DateTime(2014, 1, 1, 0, 0);
	DateTime due = new DateTime(2014, 1, 1, 9, 0);
	private Clock clock = new Clock();
	private TaskFactory tf;
	private Company company = new Company();
	List<ResourceType> list = new ArrayList<ResourceType>();
	private BranchOffice branchOffice = new BranchOffice(company, "", list);

	@Before
	public void setUp() throws Exception {
		tf = new TaskFactory(branchOffice , clock);
		project = new Project(name, description, creation, due,tf);
		clock.setSystemTime(new DateTime(2015,10,12,8,0));
	}

	@Test
	public void testProject() {
		assertEquals(name, project.getName());
		assertEquals(description, project.getDescription());
		assertEquals(creation, project.getCreationTime());
		assertEquals(due, project.getDueTime());
		assertEquals(0, project.getTasks().size());
		assertFalse(project.isFinished());
	}

	@Test(expected = IllegalArgumentException.class)
	public void createProjectNullName() {
		new Project(null, description, creation, due,tf);
	}
	@Test(expected = IllegalArgumentException.class)
	public void createProjectNullTf(){
		new Project(name, description, creation, due, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createProjectNullDescription() {
		new Project(name, null, creation, due, tf);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createProjectNullCreationTime() {
		new Project(name, description, null, due, tf);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createProjectNullDueTime() {
		new Project(name, description, creation, null, tf);
	}

	@Test(expected = IllegalDateException.class)
	public void createProjectDueBeforeCreation() {
		new Project(name, description, new DateTime(2014, 1, 2, 0, 0),
				new DateTime(2014, 1, 1, 0, 0), tf);
	}
	
	@Test
	public void createTprojectTest_TrueCase(){
		Project p = new Project(name, description, creation, due, tf);
		assertEquals(p.getName(), name);
		assertEquals(p.getDescription(), description);
		assertEquals(p.getCreationTime(), creation);
		assertEquals(p.getDueTime(), due);
	}

	@Test
	public void testGetName() {
		assertEquals("Name", project.getName());
	}

	@Test
	public void testGetDescription() {
		assertEquals("Description", project.getDescription());
	}

	@Test
	public void testGetCreationTime() {
		assertEquals(creation, project.getCreationTime());
	}

	@Test
	public void testChangeGetCreationTime() {
		DateTime time = project.getCreationTime();
		assertEquals(creation, time);
		time = time.plusMillis(10000);
		assertEquals(creation, project.getCreationTime());
	}

	@Test
	public void testGetDueTime() {
		assertEquals(due, project.getDueTime());
	}

	@Test
	public void testChangeGetDueTime() {
		DateTime time = project.getDueTime();
		assertEquals(due, time);
		time = time.plusMillis(10000);
		assertEquals(due, project.getDueTime());
	}

	@Test
	public void testGetTasks() {
		String desc = "desc";
		int estimatedDuration = 500, acceptableDeviation = 50;
		List<NormalTask> dependencies = new ArrayList<NormalTask>();
		assertEquals(0, project.getTasks().size());
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null, 1);
		assertEquals(1, project.getTasks().size());
		assertEquals(desc, project.getTasks().get(0).getDescription());
		assertEquals(estimatedDuration, project.getTasks().get(0)
				.getEstimatedDuration());
		assertEquals(acceptableDeviation, project.getTasks().get(0)
				.getAcceptableDeviation());
	}
//TODO
//	@Test
//	public void testChangeGetTasks() {
//		String desc = "desc";
//		int estimatedDuration = 500, acceptableDeviation = 50;
//		List<NormalTask> dependencies = new ArrayList<NormalTask>();
//		assertEquals(0, project.getTasks().size());
//		project.addTask(desc, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		assertEquals(1, project.getTasks().size());
//		List<Task> tasks = project.getTasks();
//		tasks.add(new Task(clock,desc, estimatedDuration, acceptableDeviation,
//				dependencies, null, null));
//		assertEquals(1, project.getTasks().size());
//	}

	@Test
	public void testAddTask() {
		String desc = "desc";
		int estimatedDuration = 500, acceptableDeviation = 50;
		List<NormalTask> dependencies = new ArrayList<NormalTask>();
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null, 1);
		assertEquals(1, project.getTasks().size());
		assertEquals(desc, project.getTasks().get(0).getDescription());
		assertEquals(estimatedDuration, project.getTasks().get(0)
				.getEstimatedDuration());
		assertEquals(acceptableDeviation, project.getTasks().get(0)
				.getAcceptableDeviation());
		assertFalse(project.isFinished());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testAddTask_Name_Null(){
		int estimatedDuration = 500, acceptableDeviation = 50;
		List<NormalTask> dependencies = new ArrayList<NormalTask>();
		project.addTask(null, estimatedDuration, acceptableDeviation,
				dependencies, null, null, 1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testAddTask_EstimatedDuration_Neg(){
		String desc = "desc";
		int acceptableDeviation = 50;
		List<NormalTask> dependencies = new ArrayList<NormalTask>();
		project.addTask(desc, -1, acceptableDeviation,
				dependencies, null, null, 1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testAddTask_AcceptableDeviation_Null(){
		String desc = "desc";
		int estimatedDuration = 500;
		List<NormalTask> dependencies = new ArrayList<NormalTask>();
		project.addTask(desc, estimatedDuration, -1,
				dependencies, null, null, 1);
	}

	@Test(expected = IllegalStateException.class)
	public void testAddTaskFinishedProject() {
		String desc = "desc";
		int estimatedDuration = 500, acceptableDeviation = 50;
		List<NormalTask> dependencies = new ArrayList<NormalTask>();
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null,null, 1);
		project.getTasks()
				.get(0)
				.addTimeSpan(false, new DateTime(2014, 1, 1, 8, 0),
						new DateTime(2014, 1, 1, 9, 0));
		assertTrue(project.isFinished());
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null, 1);
	}

	@Test
	public void testAddTaskDependencies() {
		String desc = "desc", desc2 = "desc2";
		int estimatedDuration = 500, acceptableDeviation = 50;
		int estimatedDuration2 = 600, acceptableDeviation2 = 60;
		List<NormalTask> dependencies = new ArrayList<NormalTask>();
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null, 1);
		dependencies.add(project.getTasks().get(0));
		project.addTask(desc2, estimatedDuration2, acceptableDeviation2,
				dependencies, null, null, 1);
		assertEquals(2, project.getTasks().size());
		assertTrue(project.getTasks().get(1).getDependencies()
				.contains(project.getTasks().get(0)));
		assertEquals(desc, project.getTasks().get(0).getDescription());
		assertEquals(estimatedDuration, project.getTasks().get(0)
				.getEstimatedDuration());
		assertEquals(acceptableDeviation, project.getTasks().get(0)
				.getAcceptableDeviation());
		assertEquals(desc2, project.getTasks().get(1).getDescription());
		assertEquals(estimatedDuration2, project.getTasks().get(1)
				.getEstimatedDuration());
		assertEquals(acceptableDeviation2, project.getTasks().get(1)
				.getAcceptableDeviation());
		assertFalse(project.isFinished());
	}

	@Test(expected = IllegalStateException.class)
	public void testAddTaskDependenciesFinishedProject() {
		String desc = "desc", desc2 = "desc2";
		int estimatedDuration = 500, acceptableDeviation = 50;
		int estimatedDuration2 = 600, acceptableDeviation2 = 60;
		List<NormalTask> dependencies = new ArrayList<NormalTask>();
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null, 1);
		project.getTasks()
				.get(0)
				.addTimeSpan(false, new DateTime(2013, 1, 1, 8, 0),
						new DateTime(2013, 1, 1, 9, 0));
		assertTrue(project.isFinished());
		dependencies.add(project.getTasks().get(0));
		project.addTask(desc2, estimatedDuration2, acceptableDeviation2,
				dependencies, null, null, 1);
	}

	@Test
	public void testUpdateProjectStateEmptyTasks() {
		assertFalse(project.isFinished());
		assertEquals(0, project.getTasks().size());
		assertFalse(project.isFinished());
	}

	@Test
	public void testUpdateProjectStateFinishedTask() {
		assertFalse(project.isFinished());
		String desc = "desc";
		int estimatedDuration = 500, acceptableDeviation = 50;
		List<NormalTask> dependencies = new ArrayList<NormalTask>();
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null, 1);
		Task t = project.getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8,0), new LocalTime(16,0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015,10,12,8,0), new DateTime(2015,10,12,16,0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		
		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(),"EXECUTING");
		clock.setSystemTime(new DateTime(2020, 10, 12, 10, 30));
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(),"FINISHED");
		assertTrue(project.isFinished());
	}

	@Test
	public void testUpdateProjectStateAvailableTask() {
		assertFalse(project.isFinished());
		String desc = "desc";
		int estimatedDuration = 500, acceptableDeviation = 50;
		List<NormalTask> dependencies = new ArrayList<NormalTask>();
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null, 1);
		assertFalse(project.isFinished());
	}

	@Test
	public void testUpdateProjectStateAvailableTasks() {
		assertFalse(project.isFinished());
		String desc = "desc";
		int estimatedDuration = 500, acceptableDeviation = 50;
		List<NormalTask> dependencies = new ArrayList<NormalTask>();
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null, 1);
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null, 1);
		Task t = project.getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8,0), new LocalTime(16,0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015,10,12,8,0), new DateTime(2015,10,12,16,0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		assertFalse(project.isFinished());
	}

	@Test
	public void testGetStateNameOngoing() {
		assertFalse(project.isFinished());
		assertEquals("ONGOING", project.getStateName());
	}

	@Test
	public void testGetStateNameFinished() {
		assertFalse(project.isFinished());
		String desc = "desc";
		int estimatedDuration = 500, acceptableDeviation = 50;
		List<NormalTask> dependencies = new ArrayList<NormalTask>();
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null, 1);
		Task t = project.getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8,0), new LocalTime(16,0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015,10,12,8,0), new DateTime(2015,10,12,16,0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		
		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(),"EXECUTING");
		clock.setSystemTime(new DateTime(2020, 10, 12, 10, 30));
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(),"FINISHED");
		assertTrue(project.isFinished());
		assertEquals("FINISHED", project.getStateName());
	}

	@Test
	public void testGetEstimatedFinishTime() {
		assertFalse(project.isFinished());
		String desc = "desc";
		List<NormalTask> dependencies = new ArrayList<NormalTask>();
		project.addTask(desc, 120, 0, dependencies, null, null, 1);
		assertEquals(new DateTime(2014, 1, 1, 10, 0),
				project.getEstimatedFinishTime());
		project.addTask(desc, 480, 0, dependencies, null, null, 1);
		project.addTask(desc, 60, 0, dependencies, null, null, 1);
	}

	@Test
	public void testGetTotalDelay() {
		assertFalse(project.isFinished());
		String desc = "desc";
		List<NormalTask> dependencies = new ArrayList<NormalTask>();
		project.addTask(desc, 600, 0, dependencies, null, null, 1);
		Task t = project.getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8,0), new LocalTime(16,0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2014,10,14,8,0), new DateTime(2014,10,14,16,0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		
		DateTime startTime = new DateTime(2014, 1, 1, 8, 1);
		DateTime endTime = new DateTime(2014, 1, 1, 8, 59);
		t.executeTask();
		assertEquals(t.getStatusName(),"EXECUTING");
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(),"FINISHED");
		assertTrue(project.isFinished());
		assertEquals(-1, project.getTotalDelay());
	}
	
	@Test
	public void testGetEstimatedFinishTime3() {
		assertFalse(project.isFinished());
		String desc = "desc";
		List<NormalTask> dependencies = new ArrayList<NormalTask>();
		project.addTask(desc, 600, 0, dependencies, null, null, 1);
		Task t = project.getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8,0), new LocalTime(16,0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2014,10,14,8,0), new DateTime(2014,10,14,16,0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		
		DateTime startTime = new DateTime(2014, 1, 1, 8, 1);
		DateTime endTime = new DateTime(2014, 1, 1, 11, 00);
		t.executeTask();
		assertEquals(t.getStatusName(),"EXECUTING");
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(),"FINISHED");
		assertTrue(project.isFinished());
		assertEquals(121, project.getTotalDelay());
	}
	
	@Test
	public void testGetTotalDelay2() {
		assertFalse(project.isFinished());
		String desc = "desc";
		List<NormalTask> dependencies = new ArrayList<NormalTask>();
		project.addTask(desc, 600, 0, dependencies, null, null, 1);
		project.addTask(desc, 10, 0, null, null, null, 1);
		Task t = project.getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8,0), new LocalTime(16,0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2014,10,14,8,0), new DateTime(2014,10,14,16,0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();		
		DateTime startTime = new DateTime(2014, 1, 1, 8, 1);
		DateTime endTime = new DateTime(2014, 1, 1, 9, 30);
		t.executeTask();
		assertEquals(t.getStatusName(),"EXECUTING");
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(),"FINISHED");
		
		
		Task t2 = project.getTasks().get(1);
		Developer d2 = new Developer("name", new LocalTime(8,0), new LocalTime(16,0));
		TimeSpan timeSpan2 = new TimeSpan(new DateTime(2014,10,14,8,0), new DateTime(2014,10,14,16,0));
		d2.addReservation(t2, timeSpan2);
		t2.addRequiredDeveloper(d2);
		t2.update();
		assertEquals(t2.getStatusName(),"AVAILABLE");
		DateTime startTime2 = new DateTime(2014, 1, 1, 8, 1);
		DateTime endTime2 = new DateTime(2014, 1, 1, 10, 30);
		t2.executeTask();
		assertEquals(t2.getStatusName(),"EXECUTING");
		t2.addTimeSpan(false, startTime2, endTime2);
		assertEquals(t2.getStatusName(),"FINISHED");
		assertTrue(project.isFinished());
		assertEquals(90, project.getTotalDelay());
	}
	
	@Test
	public void createMomenttoTest(){
		Project p = new Project(name, description, creation, due, tf);
		assertTrue(p.createMemento()instanceof ProjectMemento);
	}
	
	@Test
	public void toStringTest1(){
		Project p = new Project(name, description, creation, due, tf);
		project.addTask(description, 600, 0, null, null, null, 1);
		assertEquals(p.toString(),name +": ONGOING");
	} 
	
	@Test
	public void toStringTest2(){
		assertFalse(project.isFinished());
		String desc = "desc";
		int estimatedDuration = 500, acceptableDeviation = 50;
		List<NormalTask> dependencies = new ArrayList<NormalTask>();
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null, 1);
		Task t = project.getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8,0), new LocalTime(16,0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015,10,12,8,0), new DateTime(2015,10,12,16,0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		
		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(),"EXECUTING");
		clock.setSystemTime(new DateTime(2020, 10, 12, 10, 30));
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(),"FINISHED");
		assertTrue(project.isFinished());
		assertEquals("FINISHED", project.getStateName());
		assertEquals(project.toString(), name +": FINISHED");
	}
	@Test (expected = IllegalStateException.class)
	public void testFinishedGetEstimatedFinishTime(){
		assertFalse(project.isFinished());
		String desc = "desc";
		int estimatedDuration = 500, acceptableDeviation = 50;
		List<NormalTask> dependencies = new ArrayList<NormalTask>();
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null, 1);
		Task t = project.getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8,0), new LocalTime(16,0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015,10,12,8,0), new DateTime(2015,10,12,16,0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		
		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(),"EXECUTING");
		clock.setSystemTime(new DateTime(2020, 10, 12, 10, 30));
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(),"FINISHED");
		assertTrue(project.isFinished());
		project.getEstimatedFinishTime();
	}
	@Test (expected = IllegalStateException.class)
	public void testFinishedUpdate(){
		assertFalse(project.isFinished());
		String desc = "desc";
		int estimatedDuration = 500, acceptableDeviation = 50;
		List<NormalTask> dependencies = new ArrayList<NormalTask>();
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null, 1);
		Task t = project.getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8,0), new LocalTime(16,0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015,10,12,8,0), new DateTime(2015,10,12,16,0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		
		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(),"EXECUTING");
		clock.setSystemTime(new DateTime(2020, 10, 12, 10, 30));
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(),"FINISHED");
		assertTrue(project.isFinished());
		project.update();
	}
	@Test (expected = IllegalStateException.class)
	public void testFinishedAddTask(){
		assertFalse(project.isFinished());
		String desc = "desc";
		int estimatedDuration = 500, acceptableDeviation = 50;
		List<NormalTask> dependencies = new ArrayList<NormalTask>();
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null, 1);
		Task t = project.getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(8,0), new LocalTime(16,0));
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015,10,12,8,0), new DateTime(2015,10,12,16,0));
		d.addReservation(t, timeSpan);
		t.addRequiredDeveloper(d);
		t.update();
		
		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
		t.executeTask();
		assertEquals(t.getStatusName(),"EXECUTING");
		clock.setSystemTime(new DateTime(2020, 10, 12, 10, 30));
		t.addTimeSpan(false, startTime, endTime);
		assertEquals(t.getStatusName(),"FINISHED");
		assertTrue(project.isFinished());
		project.addTask("", 10, 1, null, null, null, 1);
	}
	
	@Test (expected = IllegalStateException.class)
	public void testOngoingGetTotalDelay(){
		project.addTask(description, 600, 0, null, null, null, 1);
		project.getTotalDelay();
	}
	
	@Test
	public void testgetEstimatedFinishTime_2(){
		Project p = new Project(name, description, creation, due,tf);
		p.addTask("task 1", 10, 1, null, null, null, 1);
		p.addTask("description 2", 10, 1, null, null, null, 1);
		assertEquals(p.getTasks().get(1).getStatusName(), "UNAVAILABLE");
		assertEquals(p.getTasks().get(0).getStatusName(), "UNAVAILABLE");
		NormalTask task = p.getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(10,0), new LocalTime(11,0));
		d.addReservation(task, new TimeSpan(new DateTime(2015,10,12,10,0), new DateTime(2015,10,12,14,0)));
		task.addRequiredDeveloper(d);
		clock.setSystemTime(new DateTime(2020, 10, 12, 10, 30));
		task.update();
		assertEquals(task.getStatusName(), "AVAILABLE");
		task.executeTask();
		assertEquals(task.getStatusName(), "EXECUTING");
		task.addTimeSpan(false, new DateTime(2015,10,12,11,0), new DateTime(2015,10,12,12,0));
		assertEquals(task.getStatusName(), "FINISHED");
		assertEquals(p.getEstimatedFinishTime(), new DateTime(2015,10,12,12,10));
		
	}
	
	@Test
	public void setMementoTest_StatusONGOING(){
		ProjectMemento pm = project.createMemento();
		assertEquals(project.getTasks().size(),0);
		project.addTask(description, 10, 1, null, null, null, 1);
		assertEquals(project.getTasks().size(),1);
		
		project.setMemento(pm);
		assertEquals(project.getTasks().size(),0);
	}
	
	@Test
	public void setMementoTest_StatusFINISHED(){
		Project p = new Project(name, description, creation, due,tf);
		p.addTask("task 1", 10, 1, null, null, null, 1);
		assertEquals(p.getTasks().get(0).getStatusName(), "UNAVAILABLE");
		NormalTask task = p.getTasks().get(0);
		Developer d = new Developer("name", new LocalTime(10,0), new LocalTime(11,0));
		d.addReservation(task, new TimeSpan(new DateTime(2015,10,12,10,0), new DateTime(2015,10,12,14,0)));
		task.addRequiredDeveloper(d);
		clock.advanceSystemTime(new DateTime(2015,10,12,10,1));
		task.update();
		assertEquals(task.getStatusName(), "AVAILABLE");
		task.executeTask();
		assertEquals(task.getStatusName(), "EXECUTING");
		clock.setSystemTime(new DateTime(2020, 10, 12, 10, 30));
		task.addTimeSpan(false, new DateTime(2015,10,12,11,0), new DateTime(2015,10,12,12,0));
		assertEquals(task.getStatusName(), "FINISHED");
		
		ProjectMemento pm = p.createMemento();
		assertEquals(p.getTasks().size(),1);
		p.setMemento(pm);
		assertEquals(p.getTasks().size(),1);
	}

}
