package taskman.view;

import org.joda.time.DateTime;

import taskman.exceptions.ShouldExitException;

public interface IAdvanceTimeForm {
	
	/**
	 * This method will display the given current time.
	 * 
	 * @param currentTime
	 */
	public void displayCurrentTime(DateTime currentTime);
	
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
	public DateTime getNewTime(DateTime currentTime) throws ShouldExitException;

}
