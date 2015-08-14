package taskman.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.memento.ReservableMemento;
import taskman.model.task.Reservable;
import taskman.model.task.Reservation;
import taskman.model.user.Developer;

public class ReservableMementoTest {
	private ReservableMemento r;
	private Developer d;
	private ArrayList<Reservation> reservations = new ArrayList<Reservation>();

	@Before
	public void setup() {
		d = new Developer("name", new LocalTime(10,0), new LocalTime(14,0));
		r = new ReservableMemento(d, reservations);
	}

	@Test
	public void getStateTest() {
		assertEquals(r.getState(), reservations);
	}
	
	@Test
	public void getObjectTest(){
		assertEquals(r.getObject(), d);
	}
}
