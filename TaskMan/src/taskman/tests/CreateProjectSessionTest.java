package taskman.tests;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.*;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;
import org.mockito.Mockito;

import taskman.controller.MainSession;
import taskman.controller.project.CreateProjectSession;
import taskman.model.ProjectHandler;
import taskman.model.project.Project;
import taskman.view.IView;
import taskman.view.View;

public class CreateProjectSessionTest {
	@Rule
	public TextFromStandardInputStream systemInMock = emptyStandardInputStream();

	private IView cli;
	private CreateProjectSession createProject;
	private ProjectHandler ph;
	private String description;
	private String name;
	private DateTime dueTime;

	@Before
	public void setup() {
		cli = Mockito.mock(View.class);
		ph = new ProjectHandler();
		createProject = new CreateProjectSession(cli, ph);
		ph = new ProjectHandler();
		MainSession controller = new MainSession(cli, ph);
		createProject = new CreateProjectSession(cli, ph);
		
	}

	@Test
	public void testRun() {
		DateTime creationTime = new DateTime();
		DateTime dueTime = new DateTime();
		createProject.getPH().addProject("test", "", creationTime, dueTime);
		assertEquals("test", createProject.getPH().getProjects().get(0).getName());
		assertEquals("", createProject.getPH().getProjects().get(0).getDescription());
		assertEquals(creationTime, createProject.getPH().getProjects().get(0).getCreationTime());
		assertEquals(dueTime, createProject.getPH().getProjects().get(0).getDueTime());

	}
	@Test
	public void useCase_SuccesScenario(){

		Mockito.when(cli.getNewProjectName()).thenReturn(name);
		Mockito.when(cli.getNewProjectDescription()).thenReturn(description);
		Mockito.when(cli.getNewProjectDueTime()).thenReturn(dueTime);

		createProject.run();

		// Last task in project is the new task
		Project p = ph.getProjects().get(ph.getProjects().size()-1);
		assertEquals(p.getDescription(),description);
		assertEquals(p.getName(),name);
		assertEquals(p.getDueTime(), dueTime);


	}
	// zal blijven lopen, hoe testen?
//	@Test(expected = IllegalArgumentException.class)
//	public void useCase_FailIncorrectDueTimeNull(){
//		Mockito.when(cli.getNewProjectName()).thenReturn(name);
//		Mockito.when(cli.getNewProjectDescription()).thenReturn(description);
//		Mockito.when(cli.getNewProjectDueTime()).thenReturn(null);
//
//		createProject.run();
//		
//	}
//	@Test(expected = IllegalArgumentException.class)
//	public void useCase_FailIncorrectNameNull(){
//		Mockito.when(cli.getNewProjectName()).thenReturn(null);
//		Mockito.when(cli.getNewProjectDescription()).thenReturn(description);
//		Mockito.when(cli.getNewProjectDueTime()).thenReturn(dueTime);
//
//		createProject.run();
//		
//	}
//	@Test(expected = IllegalArgumentException.class)
//	public void useCase_FailIncorrectDescriptionNull(){
//		Mockito.when(cli.getNewProjectName()).thenReturn(name);
//		Mockito.when(cli.getNewProjectDescription()).thenReturn(null);
//		Mockito.when(cli.getNewProjectDueTime()).thenReturn(dueTime);
//
//		createProject.run();
//		
//	}
	
}
