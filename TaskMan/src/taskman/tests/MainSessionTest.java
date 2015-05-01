package taskman.tests;

import static org.junit.Assert.*;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import taskman.controller.MainSession;
import taskman.controller.project.CreateProjectSession;
import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.model.UserHandler;
import taskman.model.project.Project;
import taskman.model.project.task.Task;
import taskman.view.IView;
import taskman.view.View;

public class MainSessionTest {
	private IView cli;
	private ProjectHandler ph;
	private ResourceHandler rh;
	private UserHandler uh;
	private MainSession session;
	private String description;
	private ArrayList<Task> dependencies;
	private Project p;

	@Rule
	public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();
	
	@Before
	public void setup() {
		cli = new View();
		ph = new ProjectHandler();
		rh = new ResourceHandler();
		uh = new UserHandler();
		session = new MainSession(cli, ph, rh, uh);

	}
	@Test (expected = IllegalArgumentException.class)
	public void MainSession_nullRh(){
		MainSession testSession = new MainSession(cli,ph,null,uh);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void MainSession_nullPh(){
		MainSession testSession = new MainSession(cli,null,rh,uh);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void MainSession_nullUh(){
		MainSession testSession = new MainSession(cli,ph,rh,null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void MainSession_nullCli(){
		MainSession testSession = new MainSession(null,ph,rh,uh);
	}
	@Test
	public void runTest_showProjects(){
		systemInMock.provideText("1\n8\n");
		session.run();
		assertTrue(log.getLog().contains("No projects."));
	}
	@Test
	public void runTest_createProject(){
		systemInMock.provideText("2\ncancel\n8\n");
		session.run();
		assertTrue(log.getLog().contains("Enter the name of the project (or cancel):"));
	}
	@Test
	public void runTest_createTask(){
		ph.addProject("name", "", new DateTime(), new DateTime());
		systemInMock.provideText("3\ncancel\n8\n");
		session.run();
		assertTrue(log.getLog().contains("1. name: ONGOING"));
	}

	// @Test
	// public void runTest_updateTask(){
	// systemInMock.provideText("4\ncancel\n8\n");
	// session.run();
	// assertTrue(log.getLog().contains("No available tasks"));
	// }
	
	@Test
	public void runTest_planTask(){
		systemInMock.provideText("6\ncancel\n8\n");
		session.run();
		assertTrue(log.getLog().contains("No unplanned tasks"));
	}
	@Test
	public void runTest_simulateSession(){
		systemInMock.provideText("7\n4\n8\n");
		session.run();
		assertTrue(log.getLog().contains("State saved."));
		assertTrue(log.getLog().contains("State reset"));
	}
	@Test
	public void runTest_DisplayWelcome(){
		systemInMock.provideText("8\n");
		session.run();
		assertTrue(log.getLog().contains("TaskMan V2.0"));
	}
}
