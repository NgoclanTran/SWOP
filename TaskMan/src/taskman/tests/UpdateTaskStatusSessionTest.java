package taskman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.EmptyStackException;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import taskman.UI.UI;
import taskman.controller.MainSession;
import taskman.controller.Session;
import taskman.controller.project.UpdateTaskStatusSession;
import taskman.model.ProjectHandler;
import taskman.model.project.task.Task;

public class UpdateTaskStatusSessionTest {

	UpdateTaskStatusSession updateTask;
	ProjectHandler ph;
	MainSession controller;
	TextFromStandardInputStream systemMock;

	@Before
	public void setup() {
		UI cli = new UI();
		ph = new ProjectHandler();
		controller = new MainSession(cli, ph);
		updateTask = new UpdateTaskStatusSession(cli, ph);
		ph.addProject("Project x", "Test project 1", new DateTime(),
				new DateTime(2016, 4, 1, 0, 0));
		ph.getProjects()
				.get(0)
				.addTask("Task description", 10, 1, new ArrayList<Task>(), null);
	}

	@Test
	public void testRun() {
		assertEquals(updateTask.getPH().getProjects().size(), 1);
		assertEquals(updateTask.getPH().getProjects().get(0).getName(),
				"Project x");
		controller.run();

	}
}
