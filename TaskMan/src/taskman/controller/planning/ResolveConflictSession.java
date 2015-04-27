package taskman.controller.planning;

import taskman.controller.Session;
import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.model.project.task.Task;
import taskman.view.IView;

public class ResolveConflictSession extends Session {
	
	Task task;
	Task conflictingTask;

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
	public ResolveConflictSession(IView cli, ProjectHandler ph,
			ResourceHandler rh, Task task, Task conflictingTask) throws IllegalArgumentException {
		super(cli, ph, rh);
		this.task = task;
		this.conflictingTask = conflictingTask;
	}

	@Override
	public void run() {

	}

}
