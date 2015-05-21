package taskman.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import taskman.controller.branch.ShowProjectsSession;
import taskman.model.company.ProjectHandler;
import taskman.model.company.ResourceHandler;
import taskman.model.task.Task2;
import taskman.model.task.TaskFactory;
import taskman.model.time.Clock;
import taskman.view.IView;
import taskman.view.View;

public class ShowProjectSessionTest {
	private IView cli, cli1;
	private ProjectHandler ph, ph1;
	private ResourceHandler rh;
	private ShowProjectsSession session, session1;
	private Clock clock;
	private TaskFactory tf;

	@Rule
	public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();

	@Before
	public void setup() {
		clock = new Clock();
		tf = new TaskFactory(clock);
		// Session with projects
		cli = new View();
		ph = new ProjectHandler(tf);
		rh = new ResourceHandler();
		session = new ShowProjectsSession(cli, ph);
		ArrayList<Task2> dependencies = new ArrayList<Task2>();
		Task2 t1 = new Task2(clock,"", 10, 10, null, null, null);
		ph.addProject("Project x", "Test project 1", new DateTime(),
				new DateTime(2016, 4, 1, 0, 0));
		ph.addProject("Project y", "Test project 2", new DateTime(),
				new DateTime(2016, 4, 1, 0, 0));
		ph.getProjects().get(1).addTask("", 10, 10, null, null, null);

		// Session without projects
		cli1 = new View();
		ph1 = new ProjectHandler(tf);
		session1 = new ShowProjectsSession(cli1, ph1);

	}
	
//	@Test public void useCaseTest_NoProjects(){
//		session1.run();
//		assertEquals("No projects.\r\n\r\n", log.getLog());
//	}
	
	@Test
	public void useCastTest_Projects() {
		systemInMock.provideText("1\n");
		session.run();
		assertTrue(log.getLog().contains(ph.getProjects().get(0).toString()));
	}

	@Test
	public void useCastTest_ProjectsCancel() {
		systemInMock.provideText("0\ncancel\n");
		session.run();
		// assertTrue(log.getLog().contains(ph.getProjects().get(0).toString()));
	}

	@Test
	public void useCastTest_ProjectsGetTask() {
		systemInMock.provideText("2\n1\n");
		session.run();
		assertTrue(log.getLog().contains("Task:"));
	}

	@Test
	public void useCastTest_ProjectsGetTaskCancel() {
		systemInMock.provideText("2\n0\n");
		session.run();
		// assertTrue(log.getLog().endsWith("Select a task:"));
	}
}
