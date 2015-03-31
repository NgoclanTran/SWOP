package taskman.controller;

import java.util.Arrays;
import java.util.List;

import taskman.UI.UI;
import taskman.controller.project.CreateProjectSession;
import taskman.controller.project.CreateTaskSession;
import taskman.controller.project.ShowProjectSession;
import taskman.controller.project.UpdateTaskStatusSession;
import taskman.model.facade.ProjectHandler;

public class MainSession extends Session {

	/**
	 * Constructor of the main session. This session will start the main menu on
	 * the UI.
	 * 
	 * @param cli
	 *            The command line interface.
	 * @param ph
	 *            The project handler.
	 * 
	 * @throws IllegalArgumentException
	 */
	public MainSession(UI cli, ProjectHandler ph) throws IllegalArgumentException {
		super(cli, ph);
	}

	/**
	 * Runs the initial main menu asking the user what to do.
	 */
	public void run() {
		getUI().displayVersion();

		while (true) {
			List<String> menu = Arrays.asList("Show projects",
					"Create project", "Create task", "Update task", "Quit");
			getUI().displayList(menu);

			int menuId = getUI().getNumberInput("Select an option: ");
			while (menuId <= 0 || menuId > menu.size()) {
				menuId = getUI().getNumberInput(
						"Invalid selection! Select an option: ");
			}

			switch (menuId) {
			case 1:
				new ShowProjectSession(getUI(), getPH()).run();
				break;
			case 2:
				new CreateProjectSession(getUI(),getPH()).run();
				break;
			case 3:
				new CreateTaskSession(getUI(), getPH()).run();
				break;
			case 4:
				new UpdateTaskStatusSession(getUI(), getPH()).run();
				break;
			case 5:
				return;
			default:
				getUI().display("Option available.");
			}

			getUI().displayEmptyLine();
		}
	}

}
