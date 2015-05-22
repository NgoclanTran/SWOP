package taskman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.memento.DelegatedTaskMemento;
import taskman.model.memento.NormalTaskMemento;
import taskman.model.project.Project;
import taskman.model.resource.ResourceType;
import taskman.model.task.DelegatedTask;
import taskman.model.task.NormalTask;
import taskman.model.time.Clock;
import taskman.model.time.TimeSpan;

public class DelegatedTaskMementoTest {
	
	ArrayList<NormalTask> dependants = new ArrayList<NormalTask>();
	String stateName;
	TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 11, 0), new DateTime(2015,
			10, 12, 15, 0));
	NormalTask alternative;
	private NormalTaskMemento t;
	private Clock clock = new Clock();
	private Company company = new Company();
	List<ResourceType> list = new ArrayList<ResourceType>();
	private BranchOffice branchOffice = new BranchOffice(company, "", list);
	@Before
	public void setup() {
		branchOffice.getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask("", 10, 1, null, null, null);
		DelegatedTask delegatedTask = new DelegatedTask(clock, "description", 10, 1, null, true);
		DelegatedTaskMemento dtm = new DelegatedTaskMemento(delegatedTask, true, "name", new TimeSpan(new DateTime(2015,10,12,10,0), new DateTime(2015,10,12,14,0)));
		
	}
	
	@Test
	public void constructorTest(){
		DelegatedTask delegatedTask = new DelegatedTask(clock, "description", 10, 1, null, true);
		TimeSpan ts = new TimeSpan(new DateTime(2015,10,12,10,0), new DateTime(2015,10,12,14,0));
		DelegatedTaskMemento dtm = new DelegatedTaskMemento(delegatedTask, true, "name", ts );
		assertTrue(dtm.getDependenciesFinished());
		assertEquals(dtm.getStateName(), "name");
		assertEquals(dtm.getTimeSpan(), ts);
		assertEquals(dtm.getObject(), delegatedTask);
	}
	
	
}

