package taskman.model.resource;

import java.util.ArrayList;
import java.util.List;

import taskman.model.project.task.Task;
import taskman.model.project.task.TimeSpan;

public class Resource {

	String name;

	/**
	 * Creates a new resource with the given name.
	 * 
	 * @param name
	 * 
	 * @throws IllegalArgumentException
	 */
	public Resource(String name) {
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

	public boolean isAvailableAt(TimeSpan timeSpan) {
		// TODO
		return false;
	}

	public void addReservation(Task task, TimeSpan timeSpan) {
		// TODO
	}

	public List<Reservation> getReservations() {
		return new ArrayList<Reservation>(reservations);
	}

	private List<Reservation> reservations = new ArrayList<Reservation>();

}
