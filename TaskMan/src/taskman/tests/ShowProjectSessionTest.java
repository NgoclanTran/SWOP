package taskman.tests;

import static org.junit.Assert.*;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;
import org.mockito.Mockito;

import taskman.controller.project.CreateProjectSession;
import taskman.controller.project.ShowProjectSession;
import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.model.project.Project;
import taskman.model.project.task.Task;
import taskman.view.IView;
import taskman.view.View;

public class ShowProjectSessionTest {
	private IView cli,cli1;
	private ProjectHandler ph, ph1;
	private ResourceHandler rh;
	private ShowProjectSession session, session1;

	@Rule
	public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();

	@Before
	public void setup() {
		
		// Session with projects
		cli = new View();
		ph = new ProjectHandler();
		rh = new ResourceHandler();
		session = new ShowProjectSession(cli, ph);
		ArrayList<Task> dependencies = new ArrayList<Task>();
		Task t1 = new Task("",10,10,null,null,null);
		ph.addProject("Project x", "Test project 1", new DateTime(), new DateTime(2016, 4, 1, 0, 0));
		ph.addProject("Project y", "Test project 2", new DateTime(), new DateTime(2016, 4, 1, 0, 0));
		ph.getProjects().get(1).addTask("",10,10,null,null,null);

		// Session without projects
		cli1 = new View();
		ph1 = new ProjectHandler();
		session1 = new ShowProjectSession(cli1, ph1);
		
	}
	
	@Test public void useCaseTest_NoProjects(){
		session1.run();
		assertEquals("No projects.\r\n\r\n", log.getLog());
	}
	@Test
	public void useCastTest_Projects(){
		systemInMock.provideText("1\n");
		session.run();
		assertTrue(log.getLog().contains(ph.getProjects().get(0).toString()));
	}
	@Test
	public void useCastTest_ProjectsCancel(){
		systemInMock.provideText("0\ncancel\n");
		session.run();
		//assertTrue(log.getLog().contains(ph.getProjects().get(0).toString()));
	}
	@Test
	public void useCastTest_ProjectsGetTask(){
		systemInMock.provideText("2\n1\n");
		session.run();
		assertTrue(log.getLog().contains("Task:"));
	}
	@Test
	public void useCastTest_ProjectsGetTaskCancel(){
		systemInMock.provideText("2\n0\n");
		session.run();
		//assertTrue(log.getLog().endsWith("Select a task:"));
	}
}
