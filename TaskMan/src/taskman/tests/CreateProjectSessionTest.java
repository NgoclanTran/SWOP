package taskman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.UI.UI;
import taskman.controller.MainSession;
import taskman.controller.Session;
import taskman.controller.project.CreateProjectSession;
import taskman.controller.project.UpdateTaskStatusSession;
import taskman.model.facade.ProjectHandler;
import taskman.model.project.task.Task;

public class CreateProjectSessionTest {

	CreateProjectSession createProject;
	ProjectHandler ph;

	@Before
	public void setup() {
		UI cli = new UI();
		ph = new ProjectHandler();
		MainSession controller = new MainSession(cli, ph);
		createProject = new CreateProjectSession(cli, ph);
		
	}

	@Test
	public void testRun() {
		DateTime creationTime = new DateTime();
		DateTime dueTime = new DateTime();
		createProject.getPH().addProject("test", "", creationTime, dueTime);
		assertEquals("test", createProject.getPH().getProjects().get(0).getName());
		assertEquals("", createProject.getPH().getProjects().get(0).getDescription());
		assertEquals(creationTime, createProject.getPH().getProjects().get(0).getCreationTime());
		assertEquals(dueTime, createProject.getPH().getProjects().get(0).getDueTime());

	}
}
