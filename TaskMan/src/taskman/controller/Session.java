package taskman.controller;

import taskman.view.IView;

public abstract class Session {

	/**
	 * Constructor of the session.
	 * 
	 * @param cli
	 *            The command line interface.
	 * 
	 * @post The new command line interface equals the given one.
	 * 
	 * @throws IllegalArgumentException
	 *             Both the given view and the project handler need to be valid.
	 */
	public Session(IView cli)
			throws IllegalArgumentException {
		if (!isValidUI(cli))
			throw new IllegalArgumentException("The controller needs a UI.");
		this.cli = cli;
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

	public abstract void run();

}
