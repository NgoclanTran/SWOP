package taskman;

import java.util.ArrayList;

import org.joda.time.DateTime;

import taskman.controller.MainSession;
import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.model.project.task.Task;
import taskman.model.time.Clock;
import taskman.view.IView;
import taskman.view.View;

public class TaskMan {

	public static void main(String[] args) {
		try {
			Clock clock = Clock.getInstance();
			IView commandLineInterface = new View();
			ProjectHandler projectHandler = new ProjectHandler();
			ResourceHandler resourceHandler = new ResourceHandler();
			MainSession controller = new MainSession(commandLineInterface, projectHandler, resourceHandler);

			projectHandler.addProject("Project x", "Test project 1",
					new DateTime(), new DateTime(2015, 5, 1, 0, 0));
			projectHandler.addProject("Project y", "Test project 2",
					new DateTime(2015, 4, 27, 16, 55), new DateTime(2015, 4, 28, 0, 0));
			projectHandler
					.getProjects()
					.get(0)
					.addTask("Task description", 10, 0, new ArrayList<Task>(),
							null, null);
			projectHandler
					.getProjects()
					.get(0)
					.addTask("Task description", 10, 1, new ArrayList<Task>(),
							null, null);
			
			projectHandler
			.getProjects()
			.get(1)
			.addTask("Task description", 5, 0, new ArrayList<Task>(),
					null, null);
			
			resourceHandler.addResourceType("Car", null, null);
			
			clock.setSystemTime(new DateTime(2015, 1, 1, 8, 0));

			controller.run();
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}

	}

	// TODO getters for handlers and service

}
