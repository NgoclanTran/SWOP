package taskman.model.memento;

import java.util.ArrayList;

import taskman.model.project.task.Reservation;

public class ReservableMemento {

	private ArrayList<Reservation> reservations;
	/**
	 * The constructor of the reservable memento class
	 * @param reservations
	 * 			The list of reservations for this reservable memento
	 */
	public ReservableMemento(ArrayList<Reservation> reservations) {
		this.reservations = reservations;
	}
	/**
	 * Will return a copy of the list of reservations
	 * @return Returns a copy of the list of reservations for this reservable memento
	 */
	public ArrayList<Reservation> getState() {
		return new ArrayList<Reservation>(reservations);
	}

}
