package taskman.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.memento.NormalTaskMemento;
import taskman.model.memento.ProjectMemento;
import taskman.model.project.Project;
import taskman.model.resource.ResourceType;
import taskman.model.task.NormalTask;
import taskman.model.task.Task;
import taskman.model.task.TaskFactory;
import taskman.model.time.Clock;
import taskman.model.time.TimeSpan;

public class TaskMementoTest {

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
		p.addTask("", 10, 1, null, null, null, 1);
		branchOffice.getPh().addProject("", "", new DateTime(), new DateTime());
		p.addTask("aze", 10, 1, null, null, null, 1);
		dependants.add(branchOffice.getPh().getProjects().get(0).getTasks().get(1));
		t = new NormalTaskMemento(branchOffice.getPh().getProjects().get(0).getTasks().get(0), dependants, "state", timeSpan, alternative);

	}

	@Test
	public void getDependantsTest() {
		assertEquals(t.getDependants(), dependants);
	}

	@Test
	public void getStateTest() {
		assertEquals(t.getStateName(), "state");
	}

	@Test
	public void getTimeSpanTest() {
		assertEquals(t.getTimeSpan(), timeSpan);
	}

	@Test
	public void getAlternativeTest() {
		assertEquals(t.getAlternative(), alternative);
	}
	@Test
	public void getObjectTest(){
		NormalTask t1 = new NormalTask(clock, "", 10, 1, null, null, null, 1);
		t = new NormalTaskMemento(t1, dependants, "state", timeSpan, alternative);
		assertEquals(t1, t.getObject());
	}

}
