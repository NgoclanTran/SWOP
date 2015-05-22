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

import taskman.controller.LoginSession;
import taskman.controller.Session;
import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.company.ProjectHandler;
import taskman.model.company.ResourceHandler;
import taskman.model.company.UserHandler;
import taskman.model.project.Project;
import taskman.model.resource.ResourceType;
import taskman.model.task.Task;
import taskman.model.task.TaskFactory;
import taskman.model.time.Clock;
import taskman.view.IView;
import taskman.view.View;

public class MainSessionTest {
	private IView cli;
	private ProjectHandler ph;
	private ResourceHandler rh;
	private UserHandler uh;
	private Session session;
	private String description;
	private ArrayList<Task> dependencies;
	private Project p;
	private TaskFactory tf;
	private Clock clock;

	@Rule
	public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();
	private Company company = new Company();
	private BranchOffice branchOffice = new BranchOffice(company, "", null);

	@Before
	public void setup() {
		clock = new Clock();
		tf = new TaskFactory(branchOffice , clock);
		cli = new View();
		ph = branchOffice.getPh();
		rh = branchOffice.getRh();
		uh = branchOffice.getUh();
		session = new LoginSession(cli, company);

	}

	@Test(expected = IllegalArgumentException.class)
	public void MainSession_nullCli() {
		Session testSession = new LoginSession(null, company);
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
		assertTrue(log.getLog().contains(
				"Enter the name of the project (or cancel):"));
	}

	@Test
	public void runTest_createTask() {
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
