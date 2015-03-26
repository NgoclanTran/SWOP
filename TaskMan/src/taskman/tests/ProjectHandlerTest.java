package taskman.tests;

import static org.junit.Assert.*;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.facade.ProjectHandler;
import taskman.model.project.Project;

public class ProjectHandlerTest {

	ProjectHandler ph;
	Project project1;

	@Before
	public void setUp() throws Exception {
		ph = new ProjectHandler();
		project1 = new Project("name", "description", new DateTime(),
				new DateTime());
	}

	@Test
	public void testGetProjects() {
		String name = "name", description = "description";
		DateTime creation = new DateTime(), due = new DateTime();
		assertEquals(0, ph.getProjects().size());
		ph.makeProject(name, description, creation, due);
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
		ph.makeProject(name, description, creation, due);
		assertEquals(1, ph.getProjects().size());
		assertEquals(name, ph.getProjects().get(0).getName());
		assertEquals(description, ph.getProjects().get(0).getDescription());
		assertEquals(creation, ph.getProjects().get(0).getCreationTime());
		assertEquals(due, ph.getProjects().get(0).getDueTime());
	}
}