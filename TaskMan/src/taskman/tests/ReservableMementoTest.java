package taskman.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import taskman.model.memento.ReservableMemento;
import taskman.model.project.task.Reservation;

public class ReservableMementoTest {
	private ReservableMemento r;
	private ArrayList<Reservation> reservations = new ArrayList<Reservation>();

	@Before
	public void setup() {
		r = new ReservableMemento(null, reservations);
	}

	@Test
	public void getStateTest() {
		assertEquals(r.getState(), reservations);
	}
}
