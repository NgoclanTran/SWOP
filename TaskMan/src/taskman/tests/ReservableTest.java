package taskman.tests;

import static org.junit.Assert.*;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.project.task.Reservable;
import taskman.model.project.task.Reservation;
import taskman.model.project.task.Task;
import taskman.model.time.TimeSpan;

public class ReservableTest {
	private LocalTime start;
	private LocalTime end;
	private Task t;
	private TimeSpan ts;
	
	@Before
	public void setUp() throws Exception {
		start = new LocalTime(11,0);
		end = new LocalTime(12,0);
		ts = new TimeSpan(new DateTime(2015,10,12,11,0), new DateTime(2015,10,12,15,0));
		
	}

	@Test
	public void constructorTest_StartAndEnd_Null(){
		Reservable r = new Reservable(null,null);
		assertEquals(r.getDailyAvailability().getStartTime().getHourOfDay(),0);
		assertEquals(r.getDailyAvailability().getStartTime().getMinuteOfHour(),0);
		assertEquals(r.getDailyAvailability().getEndTime().getHourOfDay(),23);
		assertEquals(r.getDailyAvailability().getEndTime().getMinuteOfHour(),59);
	}
	
	@Test
	public void constructorTest_TrueCase(){
		Reservable r = new Reservable(start,end);
		assertEquals(r.getDailyAvailability().getStartTime().getHourOfDay(),11);
		assertEquals(r.getDailyAvailability().getStartTime().getMinuteOfHour(),0);
		assertEquals(r.getDailyAvailability().getEndTime().getHourOfDay(),12);
		assertEquals(r.getDailyAvailability().getEndTime().getMinuteOfHour(),0);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void constructorTest_Start_Null(){
		Reservable r = new Reservable(start,null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void constructorTest_End_Null(){
		Reservable r = new Reservable(null,end);
	}
	
	@Test
	public void addReservationTest_Task_Null(){
		//TODO
	}
	
	@Test
	public void addReservationTest_TimeSpan_Null(){
		//TODO
	}
	
	@Test
	public void addReservationTest_TimeSpan_Invalid(){
		//TODO
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void isAvailableAtTest_TimeSpan_Null(){
		Reservable r = new Reservable(null,null);
		r.isAvailableAt(null);
	}
	
	@Test
	public void isAvailableTest_TrueCase(){
		//TODO example without reservation
		//TODO example with reservation
	}
	
	
	@Test
	public void getReservationsTest(){
		Reservable r = new Reservable(null,null);
		List<Reservation> l = r.getReservations();
		//TODO create reservation and add to l, if nothing change then Ok
		
		
	}
}
