package taskman.controller.planning;

import taskman.controller.Session;
import taskman.model.ProjectHandler;
import taskman.model.project.task.Task;
import taskman.view.IView;

public class ResolveConflictSession extends Session {

	Task task = null;
	Task conflictingTask = null;

	/**
	 * Creates the planning session using the given UI, ProjectHandler and
	 * ResourceHandler.
	 * 
	 * @param cli
	 *            The command line interface.
	 * @param ph
	 *            The project handler.
	 * 
	 * @throws IllegalArgumentException
	 */
	public ResolveConflictSession(IView cli, ProjectHandler ph)
			throws IllegalArgumentException {
		super(cli, ph);
	}

	public void run(Task task, Task conflictingTask) {
		this.task = task;
		this.conflictingTask = conflictingTask;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
