 package taskman.model.user;

import org.joda.time.LocalTime;

import taskman.exceptions.IllegalTimeException;
import taskman.model.project.task.Reservable;

public class Developer extends Reservable {

	String name;
	/**
	 * The constructor of the developper class
	 * @param name
	 * 			The name of the developper
	 * @param startTime
	 * 			The start time for this developper in terms of daily availability
	 * @param endTime
	 * 			The end time for this developper in terms of availability
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
	/**
	 * Will return the name of the developper
	 * @returns the name of the developper
	 */
	public String toString() {
		return name;
	}

}
