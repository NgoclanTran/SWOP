package taskman.model.resource;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import taskman.model.project.task.Task;
import taskman.model.time.TimeSpan;

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
		for (Reservation reservation : reservations) {
			DateTime reservationStart = reservation.getTimeSpan()
					.getStartTime();
			DateTime reservationEnd = reservation.getTimeSpan().getEndTime();
			boolean before = timeSpan.getStartTime().isBefore(reservationStart)
					&& timeSpan.getEndTime().isBefore(reservationStart);
			boolean after = timeSpan.getStartTime().isAfter(reservationEnd)
					&& timeSpan.getEndTime().isAfter(reservationEnd);
			if (!(before || after)) {
				return false;
			}
		}
		return true;
	}

	public void addReservation(Task task, TimeSpan timeSpan) {
		Reservation reservation = new Reservation(task, timeSpan);
		reservations.add(reservation);
	}

	public List<Reservation> getReservations() {
		return new ArrayList<Reservation>(reservations);
	}

	private List<Reservation> reservations = new ArrayList<Reservation>();

}
