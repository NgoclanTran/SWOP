package taskman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.UUID;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.resource.ResourceType;

public class CompanyTest {
	private Company c;
	
	@Before
	public void setUp() throws Exception {
		c = new Company();
	}

	@Test
	public void constructorTest(){
		Company c = new Company();
		assertEquals(c.getBranchOffices().size(),0);
		assertEquals(c.getName(), "SWOP");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void addBranchOfficeTest_NullBranchOffice(){
		c.addBranchOffice(null);
	}
	
	@Test 
	public void addBranchOfficeTest_TrueCase(){
		BranchOffice b = new BranchOffice(c, "new york", new ArrayList<ResourceType>());
		c.addBranchOffice(b);
		assertTrue(c.getBranchOffices().contains(b));
	}
	
//	@Test
//	public void setDependenciesFinishedTest(){
//		//TODO
//		BranchOffice b = new BranchOffice(c, "new york", new ArrayList<ResourceType>());
//		c.addBranchOffice(b);
//		UUID id = new UUID(10,12);
//		
//		
//		c.getBranchOffices().get(0).getDth().addDelegatedTask(id, "description", 10, 0, null, false);
//		c.setDependenciesFinished(id);
//		assertTrue(c.getBranchOffices().get(0).getDth().getDelegatedTasks().get(0).dependenciesAreFinished());
//	}
	
	@Test (expected = IllegalArgumentException.class)
	public void delegateTaskTest_NullTask(){
		c.delegateTask(null, new BranchOffice(c, "new york", new ArrayList<ResourceType>()));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void delegateTaskTest_NullBranchOffice(){
		BranchOffice b = new BranchOffice(c, "new york", new ArrayList<ResourceType>());
		c.addBranchOffice(b);
		c.getBranchOffices().get(0).getPh().addProject("name", "", new DateTime(2015,10,12,10,0), new DateTime(2015,10,12,10,1));
		c.getBranchOffices().get(0).getPh().getProjects().get(0).addTask("des", 10, 0, null, null, null);
		c.delegateTask(c.getBranchOffices().get(0).getPh().getProjects().get(0).getTasks().get(0), null);
	}
	@Test
	public void delegateTaskTest(){
		BranchOffice b = new BranchOffice(c, "new york", new ArrayList<ResourceType>());
		c.addBranchOffice(b);
		c.getBranchOffices().get(0).getPh().addProject("name", "", new DateTime(2015,10,12,10,0), new DateTime(2015,10,12,10,1));
		c.getBranchOffices().get(0).getPh().getProjects().get(0).addTask("des", 10, 0, null, null, null);
		
		BranchOffice b2 = new BranchOffice(c, "brussels", new ArrayList<ResourceType>());
		c.addBranchOffice(b2);
		
		assertEquals(b2.getDth().getDelegatedTasks().size(),0);
		c.delegateTask(c.getBranchOffices().get(0).getPh().getProjects().get(0).getTasks().get(0), b2);
		assertEquals(b2.getDth().getDelegatedTasks().size(),1);
	}

}
