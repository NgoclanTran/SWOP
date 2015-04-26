package taskman.controller;

import taskman.controller.project.CreateProjectSession;
import taskman.controller.project.CreateTaskSession;
import taskman.controller.project.ShowProjectSession;
import taskman.controller.project.UpdateTaskStatusSession;
import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.view.IView;

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
	public MainSession(IView cli, ProjectHandler ph, ResourceHandler rh) throws IllegalArgumentException {
		super(cli, ph, rh);
	}

	/**
	 * Runs the initial main menu asking the user what to do.
	 */
	public void run() {
		getUI().displayWelcome();

		while (true) {
			int menuId = getUI().getMainMenuID();

			switch (menuId) {
			case 1:
				new ShowProjectSession(getUI(), getPH(), getRH()).run();
				break;
			case 2:
				new CreateProjectSession(getUI(),getPH(), getRH()).run();
				break;
			case 3:
				new CreateTaskSession(getUI(), getPH(), getRH()).run();
				break;
			case 4:
				new UpdateTaskStatusSession(getUI(), getPH(), getRH()).run();
				break;
			case 5:
				return;
			}
		}
	}

}
