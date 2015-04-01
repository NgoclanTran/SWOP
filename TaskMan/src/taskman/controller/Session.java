package taskman.controller;

import java.util.List;

import taskman.UI.UI;
import taskman.exceptions.ShouldExitException;
import taskman.model.facade.ProjectHandler;

public abstract class Session {

	/**
	 * Constructor of the session.
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
	public Session(UI cli, ProjectHandler ph) throws IllegalArgumentException {
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
	 * @return Returns true if the command line interface is different from
	 *         null.
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

	// TODO: Deze methode is niet nodig in elke onderliggende klasse...
	/**
	 * Returns an integer representing the chosen object from the given list.
	 * 
	 * @param list
	 *            The given list to chose from.
	 * @param question
	 *            The question to accompany the input request.
	 * 
	 * @return Returns the number of the chosen object from the given list.
	 * 
	 * @throws ShouldExitException
	 */
	protected int getListChoice(List<?> list, String question)
			throws ShouldExitException {
		int input = getUI().getNumberInput(question);
		while (!shouldExit(input) && !(input > 0 && input <= list.size())) {
			input = getUI().getNumberInput("Invalid selection!\n" + question);
		}
		if (shouldExit(input)) {
			throw new ShouldExitException();
		}
		return input;
	}

	/**
	 * Checks if the user requested to exit.
	 * 
	 * @param index
	 * 
	 * @return Returns true if the index equals the exit value.
	 */
	private boolean shouldExit(int index) {
		return index == 0;
	}

	public abstract void run();

}
