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

import taskman.controller.project.CreateProjectSession;
import taskman.model.ProjectHandler;
import taskman.model.project.Project;
import taskman.model.project.task.Task;
import taskman.model.time.TimeSpan;
import taskman.view.IView;
import taskman.view.View;

public class AdvanceTimeSessionTest {

	private IView cli;
	private ProjectHandler ph;
	private AdvanceTimeSession session;

	@Rule
	public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();

	@Before
	public void setup() {
		cli = new View();
		ph = new ProjectHandler();
		session = new AdvanceTimeSession(cli, ph);
		ph.addProject("Project x", "Test project 1", new DateTime(), new DateTime(2016, 4, 1, 0, 0));
		ph.addProject("Project y", "Test project 2", new DateTime(), new DateTime(2016, 4, 1, 0, 0));
		ph.getProjects().get(0).addTask("Task description", 10, 1, new ArrayList<Task>(), null);
		ph.getProjects().get(0).addTask("Task description", 10, 1, new ArrayList<Task>(), null);

	}

	@Test
	public void useCaseTest_BeforeEnterTimeStamp_CancelAdvanceTime(){
		// --------------- Before running session ------------------------
		TimeSpan timeSpan1 = ph.getProjects().get(0).getTasks().get(0).getTimeSpan();
		TimeSpan timeSpan2 = ph.getProjects().get(1).getTasks().get(0).getTimeSpan();
		session.run();
		// here comes output for advancing time
		assertEquals("", log.getLog());

		// -------------- Check if timespans didn't change -------------
		assertEquals(ph.getProjects().get(0).getTasks().get(0).getTimeSpan(), timeSpan1);
		assertEquals(ph.getProjects().get(1).getTasks().get(0).getTimeSpan(), timeSpan2);
	}

	@Test
	public void useCaseTest_SuccesScenario(){
		// --------------- Before running session ------------------------
		TimeSpan timeSpan1 = ph.getProjects().get(0).getTasks().get(0).getTimeSpan();
		TimeSpan timeSpan2 = ph.getProjects().get(1).getTasks().get(0).getTimeSpan();
		
		session.run();
		// here comes output for advancing time
		assertEquals("", log.getLog());
		
		// -------------- Check if timespans changed -------------
		assertEquals(..);
		assertEquals(..);
		
		//Test wrong input too

	}

}
