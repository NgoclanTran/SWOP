package taskman.controller.planning;

import org.joda.time.DateTime;

import taskman.controller.Session;
import taskman.exceptions.ShouldExitException;
import taskman.model.ProjectHandler;
import taskman.model.project.Project;
import taskman.model.project.task.Task;
import taskman.model.time.Clock;
import taskman.view.IView;

public class AdvanceTimeSession extends Session {

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
	public AdvanceTimeSession(IView cli, ProjectHandler ph) {
		super(cli, ph);
	}

	/**
	 * Starts the use case by calling the advance time method.
	 */
	@Override
	public void run() {
		advanceTime();
	}

	/**
	 * This method will show the user the current time and ask the user to enter
	 * a new system time in the future. Afterwards it will check the time and
	 * update all the appropriate object.
	 */
	private void advanceTime() {
		while (true) {
			try {
				DateTime time = getUI().getAdvanceTimeForm().getNewTime(
						clock.getSystemTime());

				if (isValidTime(time)) {
					getUI().getAdvanceTimeForm().displayCurrentTime(
							clock.getSystemTime());
					updateAll();
					break;
				}
			} catch (ShouldExitException e) {
				return;
			}
		}
	}

	/**
	 * This method will check if the given time is a valid time.
	 * 
	 * @param time
	 * 
	 * @return Returns true if the given time is after the current system time,
	 *         else it returns false.
	 */
	private boolean isValidTime(DateTime time) {
		if (clock.getSystemTime().isBefore(time)) {
			clock.setSystemTime(time);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method will update all the tasks.
	 */
	private void updateAll() {
		for (Project project : getPH().getProjects()) {
			for (Task task : project.getTasks()) {
				try {
					task.updateTaskAvailability();
				} catch (Exception e) {

				}
			}
		}
	}

}
