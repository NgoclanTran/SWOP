package taskman.tests;

import static org.junit.Assert.fail;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.PlanningService;
import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.model.UserHandler;

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
		
		ph.addProject("PlanningService", "Test", new DateTime(), dueTime);
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

	@Test
	public void testGetPossibleStartTimes() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsValidTimeSpan() {
		fail("Not yet implemented");
	}

}
