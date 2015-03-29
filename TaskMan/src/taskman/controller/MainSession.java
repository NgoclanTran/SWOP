package taskman.controller;

import java.util.Arrays;
import java.util.List;

import taskman.UI.UI;
import taskman.controller.project.ShowProjectSession;
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
					"Create project", "Create task", "Quit");
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
				getUI().display("Create project");
				break;
			case 3:
				getUI().display("Create task");
				break;
			case 4:
				return;
			default:
				getUI().display("Option available.");
			}

			getUI().displayEmptyLine();
		}
	}

}
