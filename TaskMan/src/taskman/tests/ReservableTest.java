package taskman.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import taskman.exceptions.IllegalTimeException;
import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.memento.ReservableMemento;
import taskman.model.project.Project;
import taskman.model.task.Reservable;
import taskman.model.task.Reservation;
import taskman.model.task.Task;
import taskman.model.task.TaskFactory;
import taskman.model.time.Clock;
import taskman.model.time.TimeSpan;

public class ReservableTest {
	private LocalTime start;
	private LocalTime end;
	private Task t;
	private TimeSpan ts;
	private Clock clock = new Clock();
	Company company = new Company();
	private BranchOffice branchOffice = new BranchOffice(company, "", null);

	@Before
	public void setUp() throws Exception {
		start = new LocalTime(11, 0);
		end = new LocalTime(12, 0);
		ts = new TimeSpan(new DateTime(2015, 10, 12, 11, 0), new DateTime(2015,
				10, 12, 15, 0));

	}

	@Test
	public void constructorTest_StartAndEnd_Null() {
		Reservable r = new Reservable(null, null);
		assertEquals(r.getDailyAvailability().getStartTime().getHourOfDay(), 0);
		assertEquals(r.getDailyAvailability().getStartTime().getMinuteOfHour(),
				0);
		assertEquals(r.getDailyAvailability().getEndTime().getHourOfDay(), 23);
		assertEquals(r.getDailyAvailability().getEndTime().getMinuteOfHour(),
				59);
	}

	@Test
	public void constructorTest_TrueCase() {
		Reservable r = new Reservable(start, end);
		assertEquals(r.getDailyAvailability().getStartTime().getHourOfDay(), 11);
		assertEquals(r.getDailyAvailability().getStartTime().getMinuteOfHour(),
				0);
		assertEquals(r.getDailyAvailability().getEndTime().getHourOfDay(), 12);
		assertEquals(r.getDailyAvailability().getEndTime().getMinuteOfHour(), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorTest_Start_Null() {
		Reservable r = new Reservable(start, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorTest_End_Null() {
		Reservable r = new Reservable(null, end);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addReservationTest_Task_Null() {
		Reservable r = new Reservable(start, end);
		r.addReservation(null, ts);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addReservationTest_TimeSpan_Null() {
		Reservable r = new Reservable(start, end);
		branchOffice.getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask("", 10, 1, null, null, null);
		r.addReservation(branchOffice.getPh().getProjects().get(0).getTasks().get(0), null);
	}

	@Test(expected = IllegalTimeException.class)
	public void addReservationTest_TimeSpan_Invalid() {
		Reservable r = new Reservable(start, end);
		branchOffice.getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask("", 10, 1, null, null, null);
		r.addReservation(branchOffice.getPh().getProjects().get(0).getTasks().get(0), ts);
		r.addReservation(branchOffice.getPh().getProjects().get(0).getTasks().get(0), ts);
	}

	@Test(expected = IllegalArgumentException.class)
	public void isAvailableAtTest_TimeSpan_Null() {
		Reservable r = new Reservable(null, null);
		r.isAvailableAt(null);
	}

	@Test
	public void isAvailableTest_TrueCase1() {
		Reservable r = new Reservable(start, end);
		assertTrue(r.isAvailableAt(ts));
		// example without reservation
	}

	@Test
	public void isAvailableTestTest_TrueCase() {
		TimeSpan ts1 = new TimeSpan(new DateTime(2015, 10, 12, 10, 0),
				new DateTime(2015, 10, 12, 16, 0));
		Reservable r = new Reservable(start, end);
		assertFalse(r.isAvailableAt(ts1));

	}

	@Test
	public void isAvailableTest_TrueCase2() {
		Reservable r = new Reservable(start, end);
		branchOffice.getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask("", 10, 1, null, null, null);
		r.addReservation(branchOffice.getPh().getProjects().get(0).getTasks().get(0), ts);
		assertFalse(r.isAvailableAt(ts));
		// example with reservation
	}

	@Test
	public void getReservationsTest() {
		Reservable r = new Reservable(null, null);
		List<Reservation> l = r.getReservations();
		branchOffice.getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask("", 10, 1, null, null, null);
		
		Reservation rv = new Reservation(branchOffice.getPh().getProjects().get(0).getTasks().get(0), ts);
		l.add(rv);
		assertNotEquals(r.getReservations(), rv);
		// create reservation and add to l, if nothing change then Ok
	}

	@Test
	public void createMomentoTest() {
		Reservable r = new Reservable(null, null);
		assertTrue(r.createMemento() instanceof ReservableMemento);
	}
	
	@Test
	public void getObjectTest(){
		Reservable r = new Reservable(null, null);
		ReservableMemento r1 = new ReservableMemento(r, null);
		assertEquals(r1.getObject(), r);
	}
}
