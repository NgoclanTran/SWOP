package taskman.model.project.task;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import taskman.exceptions.IllegalTimeException;
import taskman.model.memento.ReservableMemento;
import taskman.model.time.DailyAvailability;
import taskman.model.time.TimeSpan;

public class Reservable {

	DailyAvailability dailyAvailability;

	public Reservable(LocalTime startTime, LocalTime endTime)
			throws IllegalTimeException, IllegalArgumentException {
		if (startTime == null && endTime == null)
			this.dailyAvailability = new DailyAvailability(new LocalTime(0, 0),
					new LocalTime(23, 59));
		else if (startTime != null && endTime != null)
			this.dailyAvailability = new DailyAvailability(startTime, endTime);
		else
			throw new IllegalArgumentException(
					"Both the start time and end time need to be a localtime or null.");
	}

	/**
	 * Returns the daily availability of the resource type.
	 * 
	 * @return Returns the daily availability of the resource type.
	 */
	public DailyAvailability getDailyAvailability() {
		return this.dailyAvailability;
	}

	/**
	 * Returns whether or not the resource is available during the given
	 * timespan.
	 * 
	 * @param timeSpan
	 * 
	 * @return Returns whether or not the resource is available during the given
	 *         timespan.
	 */
	public boolean isAvailableAt(TimeSpan timeSpan) {
		for (Reservation reservation : reservations) {

			DateTime reservationStart = reservation.getTimeSpan()
					.getStartTime();
			DateTime reservationEnd = reservation.getTimeSpan().getEndTime();
			
			if(!timeSpan.isDuringTimeSpan(reservationStart) || !timeSpan.isDuringTimeSpan(reservationEnd))
				return false;
		}
		if (!dailyAvailability.isValidTimeSpan(timeSpan))
			return false;
		return true;
	}

	/**
	 * Adds the reservation to the resource's reservation list.
	 * 
	 * @param task
	 * @param timeSpan
	 * 
	 * @throws NullPointerException
	 */
	public void addReservation(Task task, TimeSpan timeSpan)
			throws NullPointerException {
		if (task == null)
			throw new NullPointerException("The given Task is null.");
		if (timeSpan == null)
			throw new NullPointerException("The given timeSpan is null.");
		if (!this.isAvailableAt(timeSpan))
			throw new IllegalTimeException(
					"The resource cannot be reserved at the given timeSpan.");

		Reservation reservation = new Reservation(task, timeSpan);
		reservations.add(reservation);
	}

	/**
	 * Returns a copy of the list of reservations for the resource.
	 * 
	 * @return Returns a copy of the list of reservations for the resource.
	 */
	public List<Reservation> getReservations() {
		return new ArrayList<Reservation>(reservations);
	}

	private List<Reservation> reservations = new ArrayList<Reservation>();

	/**
	 * Creates a new reservable memento that saves the state of the
	 * reservations.
	 * 
	 * @return Creates a new reservable memento that saves the state of the
	 *         reservations.
	 */
	public ReservableMemento createMemento() {
		return new ReservableMemento(new ArrayList<Reservation>(reservations));
	}

	/**
	 * Returns the state of the reservations to that saved in the reservable
	 * memento.
	 * 
	 * @param m
	 */
	public void setMemento(ReservableMemento m) {
		reservations = m.getState();
	}

}
