package taskman.controller;

import java.util.Arrays;
import java.util.List;

import taskman.UI.UI;
import taskman.controller.project.ShowProjectController;
import taskman.exceptions.ShouldExitException;
import taskman.model.facade.ProjectHandler;

public class Controller {

	/**
	 * Constructor of the main controller. This controller will start the main
	 * menu on the UI.
	 * 
	 * @param cli
	 *            The command line interface.
	 * @param ph
	 *            The project handler.
	 *            
	 * @post The new command line interface equals the given one.
	 * @post The new project handler equals the given one.
	 * 
	 * @throws IllegalArgumentException
	 */
	public Controller(UI cli, ProjectHandler ph)
			throws IllegalArgumentException {
		if (!isValidUI(cli))
			throw new IllegalArgumentException("The controller needs a UI.");
		if (!isValidProjectHandler(ph))
			throw new IllegalArgumentException(
					"The ShowProjectController needs a ProjectHandler");
		this.cli = cli;
		this.ph = ph;
	}

	/**
	 * Checks if the given command line interface is valid.
	 * 
	 * @param cli
	 * 
	 * @return Returns true if the command line interface is different from null.
	 */
	private boolean isValidUI(UI cli) {
		if (cli != null)
			return true;
		else
			return false;
	}

	/**
	 * Returns the user interface.
	 * 
	 * @return Returns the command line interface.
	 */
	public UI getUI() {
		return cli;
	}

	protected UI cli;

	/**
	 * Checks if the given project handler is valid.
	 * 
	 * @param ph
	 * 
	 * @return Returns true if the project handler is different from null.
	 */
	private boolean isValidProjectHandler(ProjectHandler ph) {
		if (ph != null)
			return true;
		else
			return false;
	}

	/**
	 * Returns the project handler.
	 * 
	 * @return Returns the project handler.
	 */
	public ProjectHandler getPH() {
		return ph;
	}

	private ProjectHandler ph;

	// TODO: Use strategy pattern?
	protected int getChoiceForList(List<?> list, String question)
			throws ShouldExitException {
		int input = getUI().getNumberInput(question);
		while (!shouldExit(input) && !(input > 0 && input <= list.size())) {
			input = getUI().getNumberInput("Invalid selection. " + question);
		}
		if (shouldExit(input)) {
			throw new ShouldExitException();
		}
		return input;
	}

	private boolean shouldExit(int index) {
		return index == 0;
	}

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
				new ShowProjectController(getUI(), getPH()).run();
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
