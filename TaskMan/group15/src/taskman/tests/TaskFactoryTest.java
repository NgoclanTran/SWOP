package taskman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.resource.ResourceType;
import taskman.model.task.DelegatedTask;
import taskman.model.task.NormalTask;
import taskman.model.task.TaskFactory;
import taskman.model.time.Clock;

public class TaskFactoryTest {
	private  Clock clock;
	private  BranchOffice branchOffice;
	@Before
	public void setUp() throws Exception {
		clock = new Clock();
		List<ResourceType> resourceTypes = new ArrayList<ResourceType>();
		Company company = new Company();
		branchOffice = new BranchOffice(company, "New York", resourceTypes);
	}

	@Test (expected = IllegalArgumentException.class)
	public void constructorTest_NullOffice(){
		new TaskFactory(null, clock);
	}

	@Test (expected = IllegalArgumentException.class)
	public void constructorTest_NullClock(){
		new TaskFactory(branchOffice, null);
	}

	@Test
	public void makeTaskTest(){

		TaskFactory t = new TaskFactory(branchOffice, clock);
		NormalTask task = t.makeNormalTask("description", 10, 0, null, null, null, 1);
		assertEquals(task.getDescription(), "description");
		assertEquals(task.getEstimatedDuration(),10);
		assertEquals(task.getAcceptableDeviation(),0);
		assertNull(task.getAlternative());
		assertEquals(task.getDependencies().size(),0);
		assertEquals(task.getRequiredResourceTypes().size(), 0);
	}
	
	@Test
	public void makeDelegatedTaskTest(){

		TaskFactory t = new TaskFactory(branchOffice, clock);
		DelegatedTask task = t.makeDelegatedTask("description", 10, 0, null, true, 1);
		assertEquals(task.getDescription(), "description");
		assertEquals(task.getEstimatedDuration(),10);
		assertEquals(task.getAcceptableDeviation(),0);
		assertEquals(task.getRequiredResourceTypes().size(), 0);
	}

}
