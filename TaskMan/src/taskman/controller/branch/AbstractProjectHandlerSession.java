package taskman.controller.branch;

import taskman.controller.Session;
import taskman.model.company.ProjectHandler;
import taskman.view.IView;

public abstract class AbstractProjectHandlerSession extends Session {

	/**
	 * Constructor of the abstract project handler session.
	 * 
	 * @param cli
	 * @param ph
	 * 
	 * @throws IllegalArgumentException
	 *             The given project handler needs to be valid.
	 */
	public AbstractProjectHandlerSession(IView cli, ProjectHandler ph)
			throws IllegalArgumentException {
		super(cli);
		if (!isValidProjectHandler(ph))
			throw new IllegalArgumentException(
					"The abstract project handler session needs a ProjectHandler");
	}

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

}
