package taskman.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.exceptions.IllegalDateException;
import taskman.model.memento.ProjectMemento;
import taskman.model.project.Project;
import taskman.model.project.task.Task;

public class ProjectTest {

	Project project;
	String name = "Name";
	String description = "Description";
	DateTime creation = new DateTime(2014, 1, 1, 0, 0);
	DateTime due = new DateTime(2014, 1, 2, 9, 0);

	@Before
	public void setUp() throws Exception {
		project = new Project(name, description, creation, due);
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
		new Project(null, description, creation, due);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createProjectNullDescription() {
		new Project(name, null, creation, due);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createProjectNullCreationTime() {
		new Project(name, description, null, due);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createProjectNullDueTime() {
		new Project(name, description, creation, null);
	}

	@Test(expected = IllegalDateException.class)
	public void createProjectDueBeforeCreation() {
		new Project(name, description, new DateTime(2014, 1, 2, 0, 0),
				new DateTime(2014, 1, 1, 0, 0));
	}
	
	@Test
	public void createTprojectTest_TrueCase(){
		Project p = new Project(name, description, creation, due);
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
		List<Task> dependencies = new ArrayList<Task>();
		assertEquals(0, project.getTasks().size());
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
		assertEquals(1, project.getTasks().size());
		assertEquals(desc, project.getTasks().get(0).getDescription());
		assertEquals(estimatedDuration, project.getTasks().get(0)
				.getEstimatedDuration());
		assertEquals(acceptableDeviation, project.getTasks().get(0)
				.getAcceptableDeviation());
	}

	@Test
	public void testChangeGetTasks() {
		String desc = "desc";
		int estimatedDuration = 500, acceptableDeviation = 50;
		List<Task> dependencies = new ArrayList<Task>();
		assertEquals(0, project.getTasks().size());
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
		assertEquals(1, project.getTasks().size());
		List<Task> tasks = project.getTasks();
		tasks.add(new Task(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null));
		assertEquals(1, project.getTasks().size());
	}

	@Test
	public void testAddTask() {
		String desc = "desc";
		int estimatedDuration = 500, acceptableDeviation = 50;
		List<Task> dependencies = new ArrayList<Task>();
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
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
		List<Task> dependencies = new ArrayList<Task>();
		project.addTask(null, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testAddTask_EstimatedDuration_Neg(){
		String desc = "desc";
		int acceptableDeviation = 50;
		List<Task> dependencies = new ArrayList<Task>();
		project.addTask(desc, -1, acceptableDeviation,
				dependencies, null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testAddTask_AcceptableDeviation_Null(){
		String desc = "desc";
		int estimatedDuration = 500;
		List<Task> dependencies = new ArrayList<Task>();
		project.addTask(desc, estimatedDuration, -1,
				dependencies, null, null);
	}

	@Test(expected = IllegalStateException.class)
	public void testAddTaskFinishedProject() {
		String desc = "desc";
		int estimatedDuration = 500, acceptableDeviation = 50;
		List<Task> dependencies = new ArrayList<Task>();
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null,null);
		project.getTasks()
				.get(0)
				.addTimeSpan(false, new DateTime(2014, 1, 1, 8, 0),
						new DateTime(2014, 1, 1, 9, 0));
		assertTrue(project.isFinished());
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
	}

	@Test
	public void testAddTaskDependencies() {
		String desc = "desc", desc2 = "desc2";
		int estimatedDuration = 500, acceptableDeviation = 50;
		int estimatedDuration2 = 600, acceptableDeviation2 = 60;
		List<Task> dependencies = new ArrayList<Task>();
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
		dependencies.add(project.getTasks().get(0));
		project.addTask(desc2, estimatedDuration2, acceptableDeviation2,
				dependencies, null, null);
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
		List<Task> dependencies = new ArrayList<Task>();
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
		project.getTasks()
				.get(0)
				.addTimeSpan(false, new DateTime(2013, 1, 1, 8, 0),
						new DateTime(2013, 1, 1, 9, 0));
		assertTrue(project.isFinished());
		dependencies.add(project.getTasks().get(0));
		project.addTask(desc2, estimatedDuration2, acceptableDeviation2,
				dependencies, null, null);
	}

	@Test
	public void testUpdateProjectStateEmptyTasks() {
		assertFalse(project.isFinished());
		assertEquals(0, project.getTasks().size());
		assertFalse(project.isFinished());
	}

	@Test
	public void testUpdateProjectStateFinishedTask() {
		//TODO task is unavailable
		assertFalse(project.isFinished());
		String desc = "desc";
		int estimatedDuration = 500, acceptableDeviation = 50;
		List<Task> dependencies = new ArrayList<Task>();
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
		project.getTasks()
				.get(0)
				.addTimeSpan(false, new DateTime(2014, 1, 1, 8, 0),
						new DateTime(2014, 1, 1, 9, 0));
		assertTrue(project.isFinished());
	}

	@Test
	public void testUpdateProjectStateAvailableTask() {
		assertFalse(project.isFinished());
		String desc = "desc";
		int estimatedDuration = 500, acceptableDeviation = 50;
		List<Task> dependencies = new ArrayList<Task>();
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
		assertFalse(project.isFinished());
	}

	@Test
	public void testUpdateProjectStateAvailableTasks() {
		//TODO task is unavailable
		assertFalse(project.isFinished());
		String desc = "desc";
		int estimatedDuration = 500, acceptableDeviation = 50;
		List<Task> dependencies = new ArrayList<Task>();
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
		project.getTasks()
				.get(0)
				.addTimeSpan(false, new DateTime(2014, 1, 1, 8, 0),
						new DateTime(2014, 1, 1, 9, 0));
		assertFalse(project.isFinished());
	}

	@Test
	public void testGetStateNameOngoing() {
		assertFalse(project.isFinished());
		assertEquals("ONGOING", project.getStateName());
	}

	@Test
	public void testGetStateNameFinished() {
		//TODO Update Status task
		assertFalse(project.isFinished());
		String desc = "desc";
		int estimatedDuration = 500, acceptableDeviation = 50;
		List<Task> dependencies = new ArrayList<Task>();
		project.addTask(desc, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
		project.getTasks()
				.get(0)
				.addTimeSpan(false, new DateTime(2014, 1, 1, 8, 0),
						new DateTime(2014, 1, 1, 9, 0));
		assertTrue(project.isFinished());
		assertEquals("Finished", project.getStateName());
	}

	@Test
	public void testGetEstimatedFinishTime() {
		assertFalse(project.isFinished());
		String desc = "desc";
		List<Task> dependencies = new ArrayList<Task>();
		project.addTask(desc, 120, 0, dependencies, null, null);
		assertEquals(new DateTime(2014, 1, 1, 10, 0),
				project.getEstimatedFinishTime());
		project.addTask(desc, 480, 0, dependencies, null, null);
		project.addTask(desc, 60, 0, dependencies, null, null);
	}

	@Test
	public void testGetTotalDelay() {
		assertFalse(project.isFinished());
		String desc = "desc";
		List<Task> dependencies = new ArrayList<Task>();
		project.addTask(desc, 600, 0, dependencies, null, null);
		project.getTasks()
				.get(0)
				.addTimeSpan(false, new DateTime(2014, 1, 1, 8, 0),
						new DateTime(2014, 1, 2, 10, 0));
		assertTrue(project.isFinished());
		assertEquals(60, project.getTotalDelay());
	}
	
	@Test
	public void createMomenttoTest(){
		Project p = new Project(name, description, creation, due);
		assertTrue(p.createMemento()instanceof ProjectMemento);
	}

}
