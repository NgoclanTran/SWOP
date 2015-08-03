package taskman.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import taskman.controller.branch.ShowProjectsSession;
import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.company.ProjectHandler;
import taskman.model.company.ResourceHandler;
import taskman.model.project.Project;
import taskman.model.resource.ResourceType;
import taskman.model.task.Task;
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
	private Company company = new Company();
	List<ResourceType> list = new ArrayList<ResourceType>();
	private BranchOffice branchOffice = new BranchOffice(company, "Leuven", list);

	@Before
	public void setup() {
		clock = new Clock();
		tf = new TaskFactory(branchOffice ,clock);
		// Session with projects
		cli = new View();
		ph = branchOffice.getPh();
		rh = branchOffice.getRh();
		session = new ShowProjectsSession(cli, ph);
		ArrayList<Task> dependencies = new ArrayList<Task>();
		branchOffice.getPh().addProject("Project test", "description", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask("New Task", 10, 1, null, null, null, 1);
		ph.addProject("Project x", "Test project 1", new DateTime(),
				new DateTime(2016, 4, 1, 0, 0));
		ph.addProject("Project y", "Test project 2", new DateTime(),
				new DateTime(2016, 4, 1, 0, 0));
		ph.getProjects().get(1).addTask("", 10, 10, null, null, null, 1);

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
		systemInMock.provideText("1\n1\n");
		session.run();


		String s = log.getLog().substring(108, 230);

		assertTrue(s.contains("Project test:\n\t\n\tdescription\n"));

		String taskInformation = log.getLog().substring(231, log.getLog().length()-1);
		assertTrue(taskInformation.contains("Task:\n\tDescription: New Task\n\tStatus: UNAVAILABLE\n\tResponsible branch office: Leuven\n\tEstimated duration: 0 day(s), 0 hour(s), 10 minute(s)\n\tAcceptable deviation: 1 %"));
	}

	@Test 
	public void useCaseTest_NoProjects(){
		systemInMock.provideText("1\n1\n");
		session1.run();
		assertEquals(log.getLog().substring(0, 12),"No projects.");
	}
}
