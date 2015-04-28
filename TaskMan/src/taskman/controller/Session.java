package taskman.controller;

import taskman.model.ProjectHandler;
import taskman.view.IView;

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
	public Session(IView cli, ProjectHandler ph) throws IllegalArgumentException {
		if (!isValidUI(cli))
			throw new IllegalArgumentException("The controller needs a UI.");
		if (!isValidProjectHandler(ph))
			throw new IllegalArgumentException(
					"The controller needs a ProjectHandler");
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
	private boolean isValidUI(IView cli) {
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
	public IView getUI() {
		return cli;
	}

	protected IView cli;

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
	
	public abstract void run();

}
