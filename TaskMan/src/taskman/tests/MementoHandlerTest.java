package taskman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.company.DelegatedTaskHandler;
import taskman.model.company.MementoHandler;
import taskman.model.company.ProjectHandler;
import taskman.model.company.ResourceHandler;
import taskman.model.company.UserHandler;
import taskman.model.memento.Caretaker;
import taskman.model.resource.ResourceType;
import taskman.model.task.TaskFactory;
import taskman.model.time.Clock;
import taskman.model.user.User;

public class MementoHandlerTest {
	private ProjectHandler ph;
	private ResourceHandler rh;
	private UserHandler uh;
	private DelegatedTaskHandler dth;
	private Caretaker caretaker;
	private Clock clock;

	@Before
	public void setUp() throws Exception {
		clock = new Clock();
		Company company = new Company();
		List<ResourceType> list = new ArrayList<ResourceType>();
		BranchOffice branchOffice = new BranchOffice(company, "New York", list);
		TaskFactory taskFactory = new TaskFactory(branchOffice, clock);
		ph = new ProjectHandler(taskFactory);
		ph.addProject("name", "description",
				new DateTime(2015, 10, 12, 10, 10), new DateTime(2015, 10, 12,
						10, 30));

		ResourceType type = new ResourceType("name", null, null, false);
		List<ResourceType> types = new ArrayList<ResourceType>();
		types.add(type);
		rh = new ResourceHandler(types);

		uh = new UserHandler();
		uh.addProjectManager("name");
		dth = new DelegatedTaskHandler(taskFactory);
		dth.addDelegatedTask(new UUID(100, 120), "desc", 10, 0, null, true, 1);
	}

	// @Test
	// public void TestSaveANDReset(){
	// MementoHandler m = new MementoHandler(clock,ph, rh, uh, dth);
	// m.saveState();
	// uh.addDeveloper("name");
	// List<User> list = uh.getUsers();
	// m.resetState();
	// assertNotEquals(uh.getUsers(), list);
	// //TODO
	//
	//
	// }

}
