package taskman.model.resource;

import org.joda.time.LocalTime;

import taskman.exceptions.IllegalTimeException;
import taskman.model.task.Reservable;

public class Resource extends Reservable {

	String name;

	/**
	 * Creates a new resource with the given name.
	 * 
	 * @param name
	 * 			The name for the resource
	 * @param startTime
	 * 			The start time for the resource
	 * @param endTime
	 * 			The end time for the resource
	 * @throws IllegalArgumentException
	 * 			Will throw an exception if one of the parameters is illegal
	 * @throws IllegalTimeException
	 * 			Will throw an exception if the startTime and endTime parameters are conflicting
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

	/**
	 * Returns the name of the resource.
	 * 
	 * @return Returns the name of the resource.
	 */
	@Override
	public String toString() {
		return name;
	}

}
