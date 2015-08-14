package taskman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.company.ProjectHandler;
import taskman.model.project.Project;
import taskman.model.resource.ResourceType;
import taskman.model.task.TaskFactory;
import taskman.model.time.Clock;

public class ProjectHandlerTest {

	ProjectHandler ph;
	Project project1;
	private Clock clock;
	private TaskFactory tf;

	@Before
	public void setUp() throws Exception {
		clock = new Clock();
		List<ResourceType> list = new ArrayList<ResourceType>();
		BranchOffice b = new BranchOffice(new Company(), "New York", list);
		tf = new TaskFactory(b,clock);
		ph = new ProjectHandler(tf);
		project1 = new Project("name", "description", new DateTime(),
				new DateTime(),tf);
	}

	@Test (expected = IllegalArgumentException.class)
	public void constructorTest_TaskFactoryNull(){
		ProjectHandler ph = new ProjectHandler(null);
	}
	@Test
	public void testGetProjects() {
		String name = "name", description = "description";
		DateTime creation = new DateTime(), due = new DateTime();
		assertEquals(0, ph.getProjects().size());
		ph.addProject(name, description, creation, due);
		assertEquals(1, ph.getProjects().size());
		assertEquals(name, ph.getProjects().get(0).getName());
		assertEquals(description, ph.getProjects().get(0).getDescription());
		assertEquals(creation, ph.getProjects().get(0).getCreationTime());
		assertEquals(due, ph.getProjects().get(0).getDueTime());
	}

	@Test
	public void testGetProjectsAndTryToModify() {
		assertEquals(0, ph.getProjects().size());
		List<Project> modifiedList = ph.getProjects();
		modifiedList.add(project1);
		assertEquals(0, ph.getProjects().size());
	}

	@Test
	public void testMakeProject() {
		String name = "name", description = "description";
		DateTime creation = new DateTime(), due = new DateTime();
		assertEquals(0, ph.getProjects().size());
		ph.addProject(name, description, creation, due);
		assertEquals(1, ph.getProjects().size());
		assertEquals(name, ph.getProjects().get(0).getName());
		assertEquals(description, ph.getProjects().get(0).getDescription());
		assertEquals(creation, ph.getProjects().get(0).getCreationTime());
		assertEquals(due, ph.getProjects().get(0).getDueTime());
	}
}