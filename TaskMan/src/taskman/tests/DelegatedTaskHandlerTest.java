package taskman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.company.DelegatedTaskHandler;
import taskman.model.memento.DelegatedTaskHandlerMemento;
import taskman.model.memento.DelegatedTaskMemento;
import taskman.model.resource.ResourceType;
import taskman.model.task.DelegatedTask;
import taskman.model.task.TaskFactory;
import taskman.model.time.Clock;

public class DelegatedTaskHandlerTest {
	private TaskFactory tf ;
	
	@Before
	public void setUp() throws Exception {
		Company company = new Company();
		List<ResourceType> list = new ArrayList<ResourceType>();
		BranchOffice branchOffice = new BranchOffice(company, "New York", list);
		Clock clock = new Clock();
		tf = new TaskFactory(branchOffice, clock );
	}

	@Test (expected = IllegalArgumentException.class)
	public void constructorTest_TaskFactoryNull(){
		new DelegatedTaskHandler(null);
	}
	
	
	@Test
	public void constructorTest(){
		DelegatedTaskHandler dth = new DelegatedTaskHandler(tf);
		assertEquals(dth.getDelegatedTasks().size(), 0);
	}
	
	@Test 
	public void addDelegatedTaskTest(){
		DelegatedTaskHandler dth = new DelegatedTaskHandler(tf);
		assertEquals(dth.getDelegatedTasks().size(),0);
		dth.addDelegatedTask(new UUID(20,22), "description", 10, 0,null, true, 1);
		assertEquals(dth.getDelegatedTasks().size(),1);
		
		DelegatedTask t = dth.getDelegatedTasks().get(0);
		assertEquals(t.getDescription(), "description");
		assertEquals(t.getEstimatedDuration(),10);
		assertEquals(t.getAcceptableDeviation(),0);
		assertEquals(t.getRequiredResourceTypes().size(),0);
		assertTrue(t.dependenciesAreFinished());
	}
	
	@Test
	public void createMomentoTest(){
		DelegatedTaskHandler dth = new DelegatedTaskHandler(tf);
		assertEquals(dth.getDelegatedTasks().size(),0);
		dth.addDelegatedTask(new UUID(20,22), "description", 10, 0,null, true, 1);
		assertEquals(dth.getDelegatedTasks().size(),1);
		
		DelegatedTaskHandlerMemento t = dth.createMemento();
		assertEquals(t.getDelegatedTasks().size(), 1);
		assertEquals(t.getObject(), dth);
		
	}
	
	@Test
	public void setMementoTest(){
		DelegatedTaskHandler dth = new DelegatedTaskHandler(tf);
		DelegatedTaskHandlerMemento t = dth.createMemento();
		assertEquals(dth.getDelegatedTasks().size(), 0);
		
		dth.addDelegatedTask(new UUID(20,22), "description", 10, 0,null, true, 1);
		
		assertEquals(dth.getDelegatedTasks().size(), 1);
		dth.setMemento(t);
		assertEquals(dth.getDelegatedTasks().size(), 0);
		
		
	}

}
