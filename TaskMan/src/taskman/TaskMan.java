package taskman;

import java.util.ArrayList;

import org.joda.time.DateTime;

import taskman.controller.MainSession;
import taskman.model.UserHandler;
import taskman.model.PlanningService;
import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.model.project.task.Task;
import taskman.view.IView;
import taskman.view.View;

public class TaskMan {

	public static IView commandLineInterface;
	public static UserHandler userHandler;
	public static ResourceHandler resourceHandler;
	public static ProjectHandler projectHandler;
	public static PlanningService planningService;
	public static MainSession controller;

	public static void main(String[] args) {
		try {
			commandLineInterface = new View();
			userHandler = new UserHandler();
			projectHandler = new ProjectHandler();
			resourceHandler = new ResourceHandler();
			planningService = new PlanningService(resourceHandler);
			controller = new MainSession(commandLineInterface, projectHandler);

			projectHandler.addProject("Project x", "Test project 1",
					new DateTime(), new DateTime(2016, 4, 1, 0, 0));
			projectHandler.addProject("Project y", "Test project 2",
					new DateTime(), new DateTime(2016, 4, 1, 0, 0));
			projectHandler
					.getProjects()
					.get(0)
					.addTask("Task description", 10, 1, new ArrayList<Task>(),
							null);
			projectHandler
					.getProjects()
					.get(0)
					.addTask("Task description", 10, 1, new ArrayList<Task>(),
							null);

			controller.run();
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}

	}

	// TODO getters for handlers and service

}
