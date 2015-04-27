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

import taskman.controller.project.ShowProjectSession;
import taskman.model.ProjectHandler;
import taskman.model.project.task.Task;
import taskman.view.IView;
import taskman.view.View;

public class PlanTaskSessionTest {

	private IView cli,cli1;
	private ProjectHandler ph;
	private PlanTaskSessionTest session;

	@Rule
	public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();

	@Before
	public void setup() {
		
		// Session with projects
		cli = new View();
		ph = new ProjectHandler();
		session = new PlanTaskSessionTest(cli, ph);
		ph.addProject("Project x", "Test project 1", new DateTime(), new DateTime(2016, 4, 1, 0, 0));
		ph.addProject("Project y", "Test project 2", new DateTime(), new DateTime(2016, 4, 1, 0, 0));
		ph.getProjects().get(0).addTask("Task description", 10, 1, new ArrayList<Task>(), null);
		ph.getProjects().get(0).addTask("Task description", 10, 1, new ArrayList<Task>(), null);
		 
	}
	
	
	@Test
	public void useCaseTest_NoTask(){
		session.run();
		assertEquals("No tasks.", log.getLog());
	}
	
	@Test public void useCaseTest_BeforeSelectingTask_cancelPlanning(){
		
		session.run();
		// here comes output for selecting tasks
		assertEquals("", log.getLog());
		//Test wrong input too
		// Check if reservation was not created
	}
	
	@Test 
	public void useCaseTest_BeforeSelectingPlannedTime_cancelPlanning(){
		session.run();
		// here comes output for selecting tasks, selecting plan time
		assertEquals("", log.getLog());
		//Test wrong input too
		// Check if reservation was not created
		
	}
	
	@Test
	public void useCaseTest_BeforeSelectingRequirement_EnterSuggestedTime_cancelPlanning(){
		session.run();
		// here comes output for selecting tasks, selecting plan time, selectin requirements
		assertEquals("", log.getLog());
		//Test wrong input too
		// Check if reservation was not created
	}
	
	@Test
	public void useCaseTest_BeforeSelectionRequirement_EnterSelfChosenTime_cancelPlanning(){
		session.run();
		// here comes output for selecting task, selection plan time, enter plan time, selecting requirements
		assertEquals("", log.getLog());
		//Test wrong input too
		// Check if reservation was not created
	}
	
	@Test
	public void useCaseTest_BeforeSelectionResource_cancelPlanning(){
		session.run();
		// here comes output for selecting task, selection plan time, enter plan time, selecting requirements, selecting resource
		assertEquals("", log.getLog());
		//Test wrong input too
		// Check if reservation was not created
	}
	
	@Test
	public void useCaseTest_BeforeSelectingDeveloper_cancelPlanning(){
		session.run();
		// here comes output for selecting task, selection plan time, enter plan time, selecting requirements,selection developers
		assertEquals("", log.getLog());
		//Test wrong input too
		// Check if reservation was not created
		
	}
	
	@Test
	public void useCase_SuccesScenario_NoConflict(){
		session.run();
		assertEquals("", log.getLog());
		// check if reservation.planning was created
	}
	
	@Test
	public void useCase_SuccesScenario_WithResolveConflict(){
		session.run();
		assertEquals("", log.getLog());
		// check if reservation.planning was created
	}
	

}
