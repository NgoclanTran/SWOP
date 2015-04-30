package taskman.controller.planning;

import org.joda.time.DateTime;

import taskman.controller.Session;
import taskman.exceptions.ShouldExitException;
import taskman.model.ProjectHandler;
import taskman.model.time.Clock;
import taskman.view.IView;

public class AdvanceClockSession extends Session {

	Clock clock = Clock.getInstance();

	/**
	 * Creates the advance clock session using the given UI and ProjectHandler.
	 * 
	 * @param cli
	 *            The command line interface.
	 * @param ph
	 *            The project handler.
	 * 
	 * @throws IllegalArgumentException
	 *             Both the given view and the project handler need to be valid.
	 */
	public AdvanceClockSession(IView cli, ProjectHandler ph) {
		super(cli, ph);
	}

	/**
	 * Starts the use case by calling the advance time method.
	 */
	@Override
	public void run() {
		advanceTime();
	}
	
	private void advanceTime() {
		while (true) {
			try {
				DateTime time = getUI().getNewProjectForm().getNewProjectDueTime();

				if (isValidTime(time))
					break;

			} catch (ShouldExitException e) {
				return;
			}
		}
	}
	
	private boolean isValidTime(DateTime time) {
		return false;
	}

}
