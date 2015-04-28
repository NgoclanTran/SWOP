package taskman;

import taskman.controller.MainSession;
import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.model.UserHandler;
import taskman.view.IView;
import taskman.view.View;

public class TaskMan {

	public static void main(String[] args) {
		try {
			IView commandLineInterface = new View();
			ProjectHandler projectHandler = new ProjectHandler();
			ResourceHandler resourceHandler = new ResourceHandler();
			UserHandler userHandler = new UserHandler();
			Parser parser = new Parser(projectHandler, resourceHandler,
					userHandler);
			MainSession controller = new MainSession(commandLineInterface,
					projectHandler, resourceHandler, userHandler);
			parser.parse();
			controller.run();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

	}

	// TODO getters for handlers and service

}
