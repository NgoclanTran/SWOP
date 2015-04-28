package taskman.controller;

import java.util.Arrays;
import java.util.List;

import taskman.controller.planning.PlanTaskSession;
import taskman.controller.project.CreateProjectSession;
import taskman.controller.project.CreateTaskSession;
import taskman.controller.project.ShowProjectSession;
import taskman.controller.project.UpdateTaskStatusSession;
import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.model.UserHandler;
import taskman.view.IView;

public class MainSession extends Session {
	
	private final List<String> menu = Arrays.asList("Show projects",
			"Create project", "Create task", "Update task", "Plan task", "Quit");

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
	public MainSession(IView cli, ProjectHandler ph, ResourceHandler rh, UserHandler uh) throws IllegalArgumentException {
		super(cli, ph, rh, uh);
	}

	/**
	 * Runs the initial main menu asking the user what to do.
	 */
	public void run() {
		getUI().displayWelcome();

		while (true) {
			int menuId = getUI().getMainMenuID(menu);

			switch (menuId) {
			case 1:
				new ShowProjectSession(getUI(), getPH(), getRH(), getUH()).run();
				break;
			case 2:
				new CreateProjectSession(getUI(),getPH(), getRH(), getUH()).run();
				break;
			case 3:
				new CreateTaskSession(getUI(), getPH(), getRH(), getUH()).run();
				break;
			case 4:
				new UpdateTaskStatusSession(getUI(), getPH(), getRH(), getUH()).run();
				break;
			case 5:
				new PlanTaskSession(getUI(), getPH(), getRH(), getUH()).run();
				break;
			case 6:
				return;
			}
		}
	}

}
