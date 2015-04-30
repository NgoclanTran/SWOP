package taskman.view;

import org.joda.time.DateTime;

import taskman.exceptions.ShouldExitException;

public class AdvanceTimeForm implements IAdvanceTimeForm {
	
	private View view;

	/**
	 * The constructor of the advance time form. It will setup the view to be
	 * able to get input and output.
	 * 
	 * @param view
	 */
	public AdvanceTimeForm(View view) {
		this.view = view;
	}
	
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
	@Override
	public DateTime getNewTime(DateTime currentTime) throws ShouldExitException {
		try {
			view.displayInfo("The current system time:");
			view.displayInfo(view.getStringDate(currentTime));
			view.output.displayEmptyLine();
			
			String date = "";
			while (!view.isValidDateTime(date)) {
				view.displayInfo("Enter the new system time with format dd-MM-yyyy HH:mm (or cancel):");
				date = view.input.getInput();
				view.output.displayEmptyLine();
			}

			DateTime time = view.formatter.parseDateTime(date);

			return time;
		} catch (ShouldExitException e) {
			view.output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

}
