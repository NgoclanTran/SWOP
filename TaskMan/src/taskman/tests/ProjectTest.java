package taskman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.exceptions.IllegalDateException;
import taskman.model.project.Project;

public class ProjectTest {

	Project project;
	String name = "Name";
	String description = "Description";
	DateTime creation = new DateTime();
	DateTime due = new DateTime();

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
	public void testGetDueTime() {
		assertEquals(due, project.getDueTime());
	}

	@Test
	public void testGetTasks() {
		String desc = "desc";
		int estimatedDuration = 500, acceptableDeviation = 50;
		assertEquals(0, project.getTasks().size());
		project.makeTask(desc, estimatedDuration, acceptableDeviation);
		assertEquals(1, project.getTasks().size());
		assertEquals(desc, project.getTasks().get(0).getDescription());
		assertEquals(estimatedDuration, project.getTasks().get(0)
				.getEstimatedDuration());
		assertEquals(acceptableDeviation, project.getTasks().get(0)
				.getAcceptableDeviation());
	}

	@Test
	public void testMakeTaskStringIntInt() {
		String desc = "desc";
		int estimatedDuration = 500, acceptableDeviation = 50;
		project.makeTask(desc, estimatedDuration, acceptableDeviation);
		assertEquals(1, project.getTasks().size());
		assertEquals(desc, project.getTasks().get(0).getDescription());
		assertEquals(estimatedDuration, project.getTasks().get(0)
				.getEstimatedDuration());
		assertEquals(acceptableDeviation, project.getTasks().get(0)
				.getAcceptableDeviation());
		assertFalse(project.isFinished());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testMakeTaskStringIntIntFinishedProject(){
		String desc = "desc";
		int estimatedDuration = 500, acceptableDeviation = 50;
		project.makeTask(desc, estimatedDuration, acceptableDeviation);
		project.getTasks().get(0).updateStatusAndTimeSpan(Status.FINISHED, new DateTime(), new DateTime());
		project.updateProject();
		assertTrue(project.isFinished());
		project.makeTask(desc, estimatedDuration, acceptableDeviation);
	}

	@Test
	public void testMakeTaskStringIntIntArrayListOfTask() {
		String desc = "desc", desc2 = "desc2";
		int estimatedDuration = 500, acceptableDeviation = 50;
		int estimatedDuration2 = 600, acceptableDeviation2 = 60;
		project.makeTask(desc, estimatedDuration, acceptableDeviation);
		List<Task> dependencies = new ArrayList<Task>();
		dependencies.add(project.getTasks().get(0));
		project.makeTask(desc2, estimatedDuration2, acceptableDeviation2,
				dependencies);
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
	public void testMakeTaskStringIntIntArrayListOfTaskFinishedProject(){
		String desc = "desc", desc2 = "desc2";
		int estimatedDuration = 500, acceptableDeviation = 50;
		int estimatedDuration2 = 600, acceptableDeviation2 = 60;
		project.makeTask(desc, estimatedDuration, acceptableDeviation);
		project.getTasks().get(0).updateStatusAndTimeSpan(Status.FINISHED, new DateTime(), new DateTime());
		project.updateProject();
		assertTrue(project.isFinished());
		List<Task> dependencies = new ArrayList<Task>();
		dependencies.add(project.getTasks().get(0));
		project.makeTask(desc2, estimatedDuration2, acceptableDeviation2,
				dependencies);
	}
	
	@Test
	public void testUpdateProjectEmptyTasks(){
		assertFalse(project.isFinished());
		assertEquals(0, project.getTasks().size());
		project.updateProject();
		assertFalse(project.isFinished());
	}
	
	@Test
	public void testUpdateProjectFinishedTask(){
		assertFalse(project.isFinished());
		String desc = "desc";
		int estimatedDuration = 500, acceptableDeviation = 50;
		project.makeTask(desc, estimatedDuration, acceptableDeviation);
		project.getTasks().get(0).updateStatusAndTimeSpan(Status.FINISHED, new DateTime(), new DateTime());
		assertTrue(project.isFinished());
	}
	
	@Test
	public void testUpdateProjectAvailableTask(){
		assertFalse(project.isFinished());
		String desc = "desc";
		int estimatedDuration = 500, acceptableDeviation = 50;
		project.makeTask(desc, estimatedDuration, acceptableDeviation);
		assertFalse(project.isFinished());
	}

}
