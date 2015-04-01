package taskman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.UI.UI;
import taskman.controller.MainSession;
import taskman.controller.Session;
import taskman.controller.project.UpdateTaskStatusSession;
import taskman.model.facade.ProjectHandler;
import taskman.model.project.task.Task;

public class UpdateTaskStatusSessionTest {

	UpdateTaskStatusSession updateTask;
	ProjectHandler ph;

	@Before
	public void setup() {
		UI cli = new UI();
		ph = new ProjectHandler();
		MainSession controller = new MainSession(cli, ph);
		updateTask = new UpdateTaskStatusSession(cli, ph);
		ph.addProject("Project x", "Test project 1", new DateTime(),
				new DateTime(2016, 4, 1, 0, 0));
		ph.getProjects()
				.get(0)
				.addTask("Task description", 10, 1, new ArrayList<Task>(), null);

	}

	@Test
	public void testRun() {
		updateTask.getPH().
	}
}
