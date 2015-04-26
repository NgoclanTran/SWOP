package taskman.controller.planning;

import taskman.controller.Session;
import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.view.IView;

public class PlanTaskSession extends Session {

	/**
	 * Creates the planning session using the given UI, ProjectHandler and
	 * ResourceHandler.
	 * 
	 * @param cli
	 *            The command line interface.
	 * @param ph
	 *            The project handler.
	 * @param rh
	 *            The resource handler.
	 * 
	 * @throws IllegalArgumentException
	 */
	public PlanTaskSession(IView cli, ProjectHandler ph, ResourceHandler rh)
			throws IllegalArgumentException {
		super(cli, ph, rh);
	}

	@Override
	public void run() {

	}

}
