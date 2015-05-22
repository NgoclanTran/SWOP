package taskman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.company.DelegatedTaskHandler;
import taskman.model.memento.DelegatedTaskHandlerMemento;
import taskman.model.resource.ResourceType;
import taskman.model.task.DelegatedTask;
import taskman.model.task.TaskFactory;
import taskman.model.time.Clock;

public class DelegatedTaskHandlerMementoTest {
	private DelegatedTaskHandler dth;
	private List<DelegatedTask> delegatedTasks;
	@Before
	public void setUp() throws Exception {
		Clock clock = new Clock();
		Company company = new Company();
		List<ResourceType> list = new ArrayList<ResourceType>();
		BranchOffice branchOffice = new BranchOffice(company, "", list);
		TaskFactory factory = new TaskFactory(branchOffice, clock);
		dth = new DelegatedTaskHandler(factory );
		delegatedTasks = new ArrayList<DelegatedTask>();
	}

	@Test
	public void constructorTest(){
		DelegatedTaskHandlerMemento m = new DelegatedTaskHandlerMemento(dth, delegatedTasks);
		assertEquals(m.getObject(), dth);
		assertEquals(m.getDelegatedTasks(), delegatedTasks);

	}

}
