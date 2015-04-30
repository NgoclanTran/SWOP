package taskman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import taskman.model.memento.ReservableMemento;
import taskman.model.project.task.Reservation;

public class ReservableMementoTest {
	private ReservableMemento r;
	private ArrayList<Reservation> reservations = new ArrayList<Reservation>();
	@Before
	public void setup(){
		r = new ReservableMemento(reservations);
	}
	@Test
	public void getStateTest(){
		assertEquals(r.getState(), reservations);
	}
}
