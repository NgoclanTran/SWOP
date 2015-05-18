package taskman.model.memento;

import java.util.ArrayList;

import taskman.model.task.Reservable;
import taskman.model.task.Reservation;

public class ReservableMemento {

	private Reservable reservable;
	private ArrayList<Reservation> reservations;

	/**
	 * The constructor of the reservable memento class.
	 * 
	 * @param reservable
	 *            The reservable the memento is made for.
	 * @param reservations
	 *            The list of reservations for this reservable memento.
	 */
	public ReservableMemento(Reservable reservable,
			ArrayList<Reservation> reservations) {
		this.reservable = reservable;
		this.reservations = reservations;
	}

	/**
	 * Returns the reservable the memento is made for.
	 * 
	 * @return Returns the reservable the memento is made for.
	 */
	public Reservable getObject() {
		return reservable;
	}

	/**
	 * Will return a copy of the list of reservations.
	 * 
	 * @return Returns a copy of the list of reservations for this reservable
	 *         memento.
	 */
	public ArrayList<Reservation> getState() {
		return new ArrayList<Reservation>(reservations);
	}

}
