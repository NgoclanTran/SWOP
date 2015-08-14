 package taskman.model.user;

import org.joda.time.LocalTime;

import taskman.exceptions.IllegalTimeException;
import taskman.model.task.Reservable;

public class Developer extends Reservable implements User {
	
	/**
	 * The constructor of the developer class
	 * @param name
	 * 			The name of the developer
	 * @param startTime
	 * 			The start time for this developer in terms of daily availability
	 * @param endTime
	 * 			The end time for this developer in terms of availability
	 * @throws IllegalTimeException
	 * @throws IllegalArgumentException
	 */
	public Developer(String name, LocalTime startTime, LocalTime endTime)
			throws IllegalTimeException, IllegalArgumentException {
		super(startTime, endTime);
		if (name == null)
			throw new IllegalArgumentException("Name can not be null.");
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	private final String name;

	@Override
	public boolean isDeveloper() {
		return true;
	}
	
	@Override
	public boolean isProjectManager() {
		return false;
	}
	
	@Override
	/**
	 * Will return the name of the developer
	 * @returns the name of the developer
	 */
	public String toString() {
		return name;
	}

}
