package taskman.tests;

import static org.junit.Assert.assertEquals;
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
		ph.addProject("Project x", "Test project 1", new DateTime(), new DateTime(2016, 4, 1, 0, 0));
		ph.addProject("Project y", "Test project 2", new DateTime(), new DateTime(2016, 4, 1, 0, 0));
		 
		// Session without projects
		cli1 = new View();
		ph1 = new ProjectHandler();
		session1 = new ShowProjectSession(cli1, ph1);
		
	}
	
	@Test public void useCaseTest_NoProjects(){
		session1.run();
		assertEquals("No projects.\r\n\r\n", log.getLog());
	}
	
	//cannot test with projects -> creation time changes

}
