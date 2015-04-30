 package taskman.model.user;

import org.joda.time.LocalTime;

import taskman.exceptions.IllegalTimeException;
import taskman.model.project.task.Reservable;

public class Developer extends Reservable {

	String name;

	public Developer(String name, LocalTime startTime, LocalTime endTime)
			throws IllegalTimeException, IllegalArgumentException {
		super(startTime, endTime);
		if (name == null)
			throw new IllegalArgumentException("Name can not be null.");
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}
