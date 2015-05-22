package taskman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.memento.ClockMemento;
import taskman.model.memento.ProjectMemento;
import taskman.model.project.Project;
import taskman.model.resource.ResourceType;
import taskman.model.task.NormalTask;
import taskman.model.task.Task;
import taskman.model.task.TaskFactory;
import taskman.model.task.Task;
import taskman.model.time.Clock;

public class ProjectMementoTest {
	private ArrayList<NormalTask> tasks = new ArrayList<NormalTask>();
	private String stateName = "";
	private ProjectMemento p;
	private Clock clock;
	private Company company = new Company();
	List<ResourceType> list = new ArrayList<ResourceType>();
	private BranchOffice branchOffice = new BranchOffice(company, "", list);
	private ProjectMemento pm;
		
	@Before
	public void setup(){
		clock = new Clock();
		branchOffice.getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask("", 10, 1, null, null, null);
		tasks.add(p.getTasks().get(0));
		Project project = new Project("name", "description", new DateTime(2015,10,12,10,10), new DateTime(2015,10,12,12,10), new TaskFactory(branchOffice, clock));
		pm = new ProjectMemento(project, tasks, stateName);
	}
	@Test
	public void stateNameTest(){
		assertEquals(pm.getStateName(), stateName);
	}
	@Test
	public void tasksTest(){
		assertEquals(pm.getTasks(), tasks);
	}
	@Test
	public void getObjectTest(){
		Project project = new Project("name", "description", new DateTime(2015,10,12,10,10), new DateTime(2015,10,12,12,10), new TaskFactory(branchOffice, clock));
		ProjectMemento p1= new ProjectMemento(project, tasks, stateName);
		assertEquals(p1.getObject(), project);
	}
}
