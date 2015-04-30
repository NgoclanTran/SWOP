package taskman.view;

import org.joda.time.DateTime;

import taskman.exceptions.ShouldExitException;

public interface IAdvanceTimeForm {
	
	/**
	 * This method will ask the user to enter a new system time.
	 * 
	 * @param currentTime
	 * 
	 * @return Returns a date time of the new system time.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the advance time.
	 */
	public DateTime getNewTime(DateTime currentTim) throws ShouldExitException;

}
