package taskman;

import taskman.controller.MainSession;
import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.model.UserHandler;
import taskman.model.project.task.TaskFactory;
import taskman.model.time.Clock;
import taskman.view.IView;
import taskman.view.View;

public class TaskMan {

	public static void main(String[] args) {
		try {
			IView commandLineInterface = new View();
			Clock clock = new Clock();
			TaskFactory taskFactory = new TaskFactory(clock);
			ProjectHandler projectHandler = new ProjectHandler(taskFactory);
			ResourceHandler resourceHandler = new ResourceHandler();
			UserHandler userHandler = new UserHandler();
			Parser parser = new Parser(projectHandler, resourceHandler,
					userHandler, clock);
			MainSession controller = new MainSession(commandLineInterface,
					projectHandler, resourceHandler, userHandler, clock);
			parser.parse();
			controller.run();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

	}

}
