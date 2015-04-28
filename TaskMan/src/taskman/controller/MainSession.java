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
	
	ResourceHandler rh;
	UserHandler uh;

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
		super(cli, ph);
		if (!isValidResourceHandler(rh))
			throw new IllegalArgumentException(
					"The main controller needs a ResourceHandler");
		if (!isValidUserHandler(uh))
			throw new IllegalArgumentException(
					"The main controller needs a UserHandler");
		this.rh = rh;
		this.uh = uh;
	}
	
	/**
	 * Checks if the given resource handler is valid.
	 * 
	 * @param rh
	 * 
	 * @return Returns true if the resource handler is different from null.
	 */
	private boolean isValidResourceHandler(ResourceHandler rh) {
		if (rh != null)
			return true;
		else 
			return false;
	}
	
	/**
	 * Checks if the given user handler is valid.
	 * 
	 * @param rh
	 * 
	 * @return Returns true if the user handler is different from null.
	 */
	private boolean isValidUserHandler(UserHandler uh) {
		if (uh != null)
			return true;
		else 
			return false;
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
				new ShowProjectSession(getUI(), getPH()).run();
				break;
			case 2:
				new CreateProjectSession(getUI(),getPH()).run();
				break;
			case 3:
				new CreateTaskSession(getUI(), getPH(), rh).run();
				break;
			case 4:
				new UpdateTaskStatusSession(getUI(), getPH()).run();
				break;
			case 5:
				new PlanTaskSession(getUI(), getPH(), uh).run();
				break;
			case 6:
				return;
			}
		}
	}
}
