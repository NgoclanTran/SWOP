package taskman.model.resource;

import org.joda.time.LocalTime;

import taskman.exceptions.IllegalTimeException;
import taskman.model.project.task.Reservable;
import taskman.model.time.DailyAvailability;

public class Resource extends Reservable {

	String name;

	/**
	 * Creates a new resource with the given name.
	 * 
	 * @param name
	 * 
	 * @throws IllegalArgumentException
	 */
	public Resource(String name, LocalTime startTime, LocalTime endTime)
			throws IllegalTimeException, IllegalTimeException {
		super(startTime, endTime);
		if (name == null)
			throw new IllegalArgumentException("Name can not be null.");
		this.name = name;
	}

	/**
	 * Returns the name of the resource.
	 * 
	 * @return Returns the name of the resource.
	 */
	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return name;
	}

}
