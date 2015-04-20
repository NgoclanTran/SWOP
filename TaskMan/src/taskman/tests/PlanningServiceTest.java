package taskman.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.PlanningService;
import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.model.UserHandler;
import taskman.model.project.task.Task;

public class PlanningServiceTest {
	
	UserHandler uh;
	ProjectHandler ph;
	ResourceHandler rh;
	PlanningService planning;
	
	@Before
	public void setUp() throws Exception {
		uh = new UserHandler();
		ph = new ProjectHandler();
		rh = new ResourceHandler();
		planning = new PlanningService(uh, ph, rh);
		
		ph.addProject("PlanningService", "Test", new DateTime(2015,1,1,8,0), new DateTime(2015,2,1,17,0));
		ph.getProjects().get(0).addTask("PlanningServiceTest", 60, 0, null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPlanningServiceNullUserHandler() {
		new PlanningService(null, ph, rh);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPlanningServiceNullProjectHandler() {
		new PlanningService(uh, null, rh);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPlanningServiceNullResourceHandler() {
		new PlanningService(uh, ph, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetPossibleStartTimesNullTask() {
		planning.getPossibleStartTimes(null, 3, new DateTime());
	}

	@Test
	public void testGetPossibleStartTimes() {
		Task task = ph.getProjects().get(0).getTasks().get(0);
		Set<DateTime> expectedStartTimes = new HashSet<DateTime>();
		Set<DateTime> startTimes = planning.getPossibleStartTimes(task, 3, null);
		assertEquals()
	}

	@Test
	public void testIsValidTimeSpan() {
		fail("Not yet implemented");
	}

}
