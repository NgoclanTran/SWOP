package taskman.controller;

import taskman.controller.project.CreateProjectSession;
import taskman.controller.project.CreateTaskSession;
import taskman.controller.project.ShowProjectSession;
import taskman.controller.project.UpdateTaskStatusSession;
import taskman.model.facade.ProjectHandler;
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
	public MainSession(IView cli, ProjectHandler ph) throws IllegalArgumentException {
		super(cli, ph);
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
			}
		}
	}

}
