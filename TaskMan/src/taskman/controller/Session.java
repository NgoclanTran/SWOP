package taskman.controller;

import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.model.UserHandler;
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
	public Session(IView cli, ProjectHandler ph, ResourceHandler rh, UserHandler uh) throws IllegalArgumentException {
		if (!isValidUI(cli))
			throw new IllegalArgumentException("The controller needs a UI.");
		if (!isValidProjectHandler(ph))
			throw new IllegalArgumentException(
					"The controller needs a ProjectHandler");
		if (!isValidResourceHandler(rh))
			throw new IllegalArgumentException(
					"The controller needs a ResourceHandler");
		if (!isValidUserHandler(uh))
			throw new IllegalArgumentException(
					"The controller needs a UserHandler");
		this.cli = cli;
		this.ph = ph;
		this.rh = rh;
		this.uh = uh;
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
	 * Returns the resource handler.
	 * 
	 * @return Returns the resource handler.
	 */
	public ResourceHandler getRH() {
		return rh;
	}

	private ResourceHandler rh;

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
	 * Returns the user handler.
	 * 
	 * @return Returns the user handler.
	 */
	public UserHandler getUH() {
		return uh;
	}

	private UserHandler uh;
	
	public abstract void run();

}
