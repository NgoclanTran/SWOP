package taskman;

import taskman.model.company.MementoHandler;
import taskman.model.company.ProjectHandler;
import taskman.model.company.ResourceHandler;
import taskman.model.company.UserHandler;
import taskman.model.task.TaskFactory;
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
			MementoHandler mementoHandler = new MementoHandler(clock,
					projectHandler, resourceHandler, userHandler);
			Parser parser = new Parser(projectHandler, resourceHandler,
					userHandler, clock);
			//MainSession controller = new MainSession(commandLineInterface,
			//		projectHandler, resourceHandler, userHandler,
			//		mementoHandler, clock);
			parser.parse();
			//controller.run();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

	}

}
