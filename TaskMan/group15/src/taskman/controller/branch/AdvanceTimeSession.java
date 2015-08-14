package taskman.controller.branch;

import org.joda.time.DateTime;

import taskman.controller.Session;
import taskman.exceptions.ShouldExitException;
import taskman.model.time.Clock;
import taskman.view.IView;

public class AdvanceTimeSession extends Session {

	private Clock clock;

	/**
	 * Creates the advance clock session using the given UI and ProjectHandler.
	 * 
	 * @param cli
	 *            The command line interface.
	 * @param clock
	 *            The system clock.
	 * 
	 * @throws IllegalArgumentException
	 *             The view needs to be valid.
	 * @throws IllegalArgumentException
	 *             The given clock needs to be valid.
	 */
	public AdvanceTimeSession(IView cli, Clock clock)
			throws IllegalArgumentException {
		super(cli);
		if (clock == null)
			throw new IllegalArgumentException(
					"The advance time controller needs a clock");
		this.clock = clock;
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
		try {
			clock.advanceSystemTime(time);
			return true;
		} catch(IllegalArgumentException ex) {
			return false;
		}
	}

}
