package taskman.model.memento;

import java.util.ArrayList;

import taskman.model.project.task.Reservation;

public class ReservableMemento {

	private ArrayList<Reservation> reservations;

	public ReservableMemento(ArrayList<Reservation> reservations) {
		this.reservations = reservations;
	}

	public ArrayList<Reservation> getState() {
		return new ArrayList<Reservation>(reservations);
	}

}
