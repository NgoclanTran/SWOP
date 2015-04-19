package taskman.tests;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;



import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import taskman.controller.project.CreateTaskSession;
import taskman.model.ProjectHandler;
import taskman.model.project.Project;
import taskman.model.project.task.Task;
import taskman.view.IView;
import taskman.view.View;

public class CreateTaskSessionTest {
	private IView cli;
	private ProjectHandler ph;
	private CreateTaskSession session;
	private String description;
	private int estimatedDuration ;
	private int acceptableDeviation ;
	private ArrayList<Task> dependencies;
	private Project p;

	@Before
	public void setup(){
		cli = Mockito.mock(View.class);
		ph = new ProjectHandler();
		session = new CreateTaskSession(cli, ph);
		ph.addProject("Project x", "Test project 1", new DateTime(), new DateTime(2016, 4, 1, 0, 0));
		ph.addProject("Project y", "Test project 2", new DateTime(), new DateTime(2016, 4, 1, 0, 0));
		ph.getProjects().get(0).addTask("Task description", 10, 1, new ArrayList<Task>(), null);
		ph.getProjects().get(0).addTask("Task description", 10, 1, new ArrayList<Task>(), null);
		description = "description";
		estimatedDuration = 10;
		acceptableDeviation = 11;
		dependencies = new ArrayList<Task>();
		p = ph.getProjects().get(0);

	}
	// Test without Session, with ProjectHandler
	@Test
	public void useCase_Succes(){

		// 1. User chose to create a task
		// 2. Show creation form
		// 3. User give all required informations
		// 4. User select a project and create the task- for this use case, select the first one
		Project p = ph.getProjects().get(0); // this should be project x
		assertEquals(p.getName(), "Project x");
		//Examples as information for task - valid information
		String description = "description";
		int estimatedDuration = 10;
		int acceptableDeviation = 11;
		ArrayList<Task> dependencies = new ArrayList<Task>();

		p.addTask(description, estimatedDuration, acceptableDeviation, dependencies, null);

		// Task is created and should be part of the choosen project
		// The new Task is added at the end of the list - we work with arraylist
		// The new Task have the same informations as given
		assertEquals(p.getTasks().get(2).getDescription(), description);
		assertEquals(p.getTasks().get(2).getEstimatedDuration(), estimatedDuration);
		assertEquals(p.getTasks().get(2).getAcceptableDeviation(), acceptableDeviation);
		assertEquals(p.getTasks().get(2).getDependencies(),dependencies);
		assertNull(p.getTasks().get(2).getAlternative());

	}

	@Test
	public void useCaseTest_SuccesScenario(){

	}

	@Test
	public void useCase_SuccesScenario(){
		// Use Case 4.3
		// 1. User chose to create a task
		// 2. Show creation form
		// 3. User give all required informations
		// 4. User select a project and create the task- for this use case, select the first one
		Mockito.when(cli.getProject(ph.getProjects())).thenReturn(p);
		Mockito.when(cli.getNewTaskDescription()).thenReturn(description);
		Mockito.when(cli.getNewTaskEstimatedDuration()).thenReturn(estimatedDuration);
		Mockito.when(cli.getNewTaskAcceptableDeviation()).thenReturn(acceptableDeviation);
		Mockito.when(cli.getNewTaskDependencies(p.getTasks())).thenReturn(dependencies);

		session.run();

		// Last task in project is the new task
		Task t = p.getTasks().get(p.getTasks().size()-1);
		assertEquals(t.getDescription(),description);
		assertEquals(t.getEstimatedDuration(),estimatedDuration);
		assertEquals(t.getAcceptableDeviation(), acceptableDeviation);
		assertEquals(t.getDependencies(), dependencies);
		assertNull(t.getAlternative());
		assertEquals(t.getStatusName(), "AVAILABLE");


	}
	@Test
	public void useCase_SuccesScenario_WithDependencies(){
		//Use Case 4.3
		// Use Case 4.3
		// 1. User chose to create a task
		// 2. Show creation form
		// 3. User give all required informations
		// 4. User select a project and create the task- for this use case, select the first one
		// The new task is not avaible due to his dependencies
		Mockito.when(cli.getNewTaskDescription()).thenReturn(description);
		Mockito.when(cli.getNewTaskEstimatedDuration()).thenReturn(estimatedDuration);
		Mockito.when(cli.getNewTaskAcceptableDeviation()).thenReturn(acceptableDeviation);
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.add(p.getTasks().get(0));
		Mockito.when(cli.getNewTaskDependencies(p.getTasks())).thenReturn(tasks);
		Mockito.when(cli.getProject(ph.getProjects())).thenReturn(p);
		session.run();

		// Last task in project is the new task
		Task t = p.getTasks().get(p.getTasks().size()-1);
		assertEquals(t.getDescription(),description);
		assertEquals(t.getEstimatedDuration(),estimatedDuration);
		assertEquals(t.getAcceptableDeviation(), acceptableDeviation);
		assertEquals(t.getDependencies(), tasks);
		assertNull(t.getAlternative());
		assertEquals(t.getStatusName(), "UNAVAILABLE");


	}
	// ---------------------------------USE CASE STOP 4a ------------------------------------------ 
	// ---------------------------------CANNOT TEST CORRECTLY -------------------------------------
	// this tests never ends - what is normal in the createTask() method

	//	@Test
	//	public void useCase_Failed_DescriptionNull(){
	//		Mockito.when(cli.getNewTaskDescription()).thenReturn(null);
	//		Mockito.when(cli.getNewTaskEstimatedDuration()).thenReturn(estimatedDuration);
	//		Mockito.when(cli.getNewTaskAcceptableDeviation()).thenReturn(acceptableDeviation);
	//		Mockito.when(cli.getNewTaskDependencies(p.getTasks())).thenReturn(dependencies);
	//		Mockito.when(cli.getProject(ph.getProjects())).thenReturn(p);
	//		session.run();
	//
	//
	//		// Last task in project is not a new task
	//		Task t = p.getTasks().get(p.getTasks().size()-1);
	//		assertNotEquals(t.getDescription(),description);
	//		System.out.println("1");
	//		assertNotEquals(t.getEstimatedDuration(),estimatedDuration);
	//		System.out.println("2");
	//		assertNotEquals(t.getAcceptableDeviation(), acceptableDeviation);
	//		System.out.println("3");
	//		assertNotEquals(t.getDependencies(), dependencies);
	//		System.out.println("4");
	//		assertNotNull(t.getAlternative());
	//
	//	}
	//	
	//	@Test
	//	public void useCase_Failed_EstimatedDurationNotPos(){
	//		Mockito.when(cli.getNewTaskDescription()).thenReturn(description);
	//		Mockito.when(cli.getNewTaskEstimatedDuration()).thenReturn(0);
	//		Mockito.when(cli.getNewTaskAcceptableDeviation()).thenReturn(acceptableDeviation);
	//		Mockito.when(cli.getNewTaskDependencies(p.getTasks())).thenReturn(dependencies);
	//		Mockito.when(cli.getProject(ph.getProjects())).thenReturn(p);
	//		session.run();
	//
	//
	//		// Last task in project is not a new task
	//		Task t = p.getTasks().get(p.getTasks().size()-1);
	//		assertNotEquals(t.getDescription(),description);
	//		assertNotEquals(t.getEstimatedDuration(),-1);
	//		assertNotEquals(t.getAcceptableDeviation(), acceptableDeviation);
	//		assertNotEquals(t.getDependencies(), dependencies);
	//		assertNotNull(t.getAlternative());
	//
	//	}
	//	@Test
	//	public void useCase_Failed_AcceptableDeviationNotPos(){
	//		Mockito.when(cli.getNewTaskDescription()).thenReturn(description);
	//		Mockito.when(cli.getNewTaskEstimatedDuration()).thenReturn(estimatedDuration);
	//		Mockito.when(cli.getNewTaskAcceptableDeviation()).thenReturn(-1);
	//		Mockito.when(cli.getNewTaskDependencies(p.getTasks())).thenReturn(dependencies);
	//		Mockito.when(cli.getProject(ph.getProjects())).thenReturn(p);
	//		session.run();
	//
	//
	//		// Last task in project is not a new task
	//		Task t = p.getTasks().get(p.getTasks().size()-1);
	//		assertNotEquals(t.getDescription(),description);
	//		assertNotEquals(t.getEstimatedDuration(),estimatedDuration);
	//		assertNotEquals(t.getAcceptableDeviation(), -1);
	//		assertNotEquals(t.getDependencies(), dependencies);
	//		assertNotNull(t.getAlternative());
	//
	//	}
	//	@Test
	//	public void useCase_Failed_DependenciesNull(){
	//	Mockito.when(cli.getNewTaskDescription()).thenReturn(description);
	//	Mockito.when(cli.getNewTaskEstimatedDuration()).thenReturn(estimatedDuration);
	//	Mockito.when(cli.getNewTaskAcceptableDeviation()).thenReturn(-1);
	//	Mockito.when(cli.getNewTaskDependencies(p.getTasks())).thenReturn(null);
	//	Mockito.when(cli.getProject(ph.getProjects())).thenReturn(p);
	//	session.run();
	//
	//
	//	// Last task in project is not a new task
	//	Task t = p.getTasks().get(p.getTasks().size()-1);
	//	assertNotEquals(t.getDescription(),description);
	//	assertNotEquals(t.getEstimatedDuration(),estimatedDuration);
	//	assertNotEquals(t.getAcceptableDeviation(), -1);
	//	assertNotEquals(t.getDependencies(), null);
	//	assertNotNull(t.getAlternative());
	//
	//}

}
