package taskman;

import taskman.model.company.Company;
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
			//IView commandLineInterface = new View();
			Company company = new Company();
			Parser parser = new Parser(company);
			parser.parse();
			//MainSession controller = new MainSession(commandLineInterface,
			//		projectHandler, resourceHandler, userHandler,
			//		mementoHandler, clock);
			//controller.run();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

	}

}
