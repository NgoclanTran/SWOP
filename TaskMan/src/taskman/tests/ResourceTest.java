package taskman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import taskman.exceptions.IllegalTimeException;
import taskman.model.project.task.Task;
import taskman.model.resource.DailyAvailability;
import taskman.model.resource.Resource;
import taskman.model.time.TimeSpan;

public class ResourceTest {
	private String name;
	private LocalTime startTime, endTime;
	private TimeSpan t;
	private Task task;
	
	@Before
	public void setUp() throws Exception {
		name = "name";
		startTime = new LocalTime(10,0);
		endTime = new LocalTime(16,0);
		DateTime st = new DateTime(2015,10,12,10,0);
		DateTime et = new DateTime(2015,10,12,16,0);
		t = new TimeSpan(st, et);
		task = new Task("description", 10, 1,
				new ArrayList<Task>(), null);
		
	}

	@Test (expected = IllegalArgumentException.class)
	public void constructorTest_Name_Null(){
		Resource r = new Resource(null, startTime, endTime);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void constructorTest_StartTime_Null(){
		Resource r = new Resource(name, null, endTime);
		
	}
	
	@Test
	public void constructor_StartAndEndTime_Null(){
		Resource r = new Resource(name, null, null);
		assertEquals(r.getDailyAvailability().getStartTime().getHourOfDay(), 0);
		assertEquals(r.getDailyAvailability().getStartTime().getMinuteOfHour(), 0);
		assertEquals(r.getDailyAvailability().getEndTime().getHourOfDay(), 0);
		assertEquals(r.getDailyAvailability().getEndTime().getMinuteOfHour(), 0);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void constructorTest_EndTime_Null(){
		Resource r = new Resource(name, startTime, null);
		
	}
	@Test (expected = IllegalTimeException.class)
	public void constructorTest_StartTime_After_EndTime(){
		Resource r = new Resource(name, endTime, startTime);
		
	}
	
	@Test 
	public void constructorTest_TrueCase(){
		Resource r = new Resource(name, startTime, endTime);
		assertEquals(r.getName(), name);
		DailyAvailability d = r.getDailyAvailability();
		assertEquals(d.getStartTime(), startTime);
		assertEquals(d.getEndTime(), endTime);
	}
	
	@Test (expected = NullPointerException.class)
	public void addReservationTest_Task_Null(){
		Resource r = new Resource(name, startTime, endTime);
		r.addReservation(null, t);
	
	}
	
	@Test (expected = NullPointerException.class)
	public void addReservationTest_TimeSpan_Null(){
		Resource r = new Resource(name, startTime, endTime);
		r.addReservation(null, t);
		
	}
	
	@Test(expected = IllegalTimeException.class)
	public void addReservationTest_NotAvailable_DailyAvailibility(){
		Resource r = new Resource(name, startTime, endTime);
		TimeSpan t1 = new TimeSpan(new DateTime(2015,10,12,8,0),new DateTime(2015,10,12,9,0));
		r.addReservation(task, t1);
	}
	
	@Test (expected = IllegalTimeException.class)
	public void addReservationTest_NotAvailable_Reservation(){
		Resource r = new Resource(name, startTime, endTime);
		r.addReservation(task, t);
		r.addReservation(task, t);
		
	}
	
	@Test
	public void addReservationTest_TrueCase(){
		
		Resource r = new Resource(name, startTime, endTime);
		//------------- Before add reservation --------------------
		assertEquals(r.getReservations().size(),0);
		r.addReservation(task, t);
		
		// ------------ After add reservation ---------------------
		assertEquals(r.getReservations().size(),1);
		assertEquals(r.getReservations().get(0).getTask(), task);
		assertEquals(r.getReservations().get(0).getTimeSpan(),t);
	}
	
	@Test
	public void isAvailableTest_TrueCase(){
		Resource r = new Resource(name, startTime, endTime);
		DateTime s = new DateTime(2015,10,12,10,0);
		DateTime e = new DateTime(2015,10,12,11,0);
		TimeSpan ts = new TimeSpan(s, e);
		assertTrue(r.isAvailableAt(ts));
		
	}
	
	@Test 
	public void isAvailableTest_DailyAvailability_FalseCase(){
		Resource r = new Resource(name, startTime, endTime);
		DateTime s = new DateTime(2015,10,12,8,0);
		DateTime e = new DateTime(2015,10,12,9,0);
		TimeSpan ts = new TimeSpan(s, e);
		assertFalse(r.isAvailableAt(ts));
	}
	
	@Test 
	public void isAvailableTest_Reservation_FalseCase(){
		Resource r = new Resource(name, startTime, endTime);
		r.addReservation(task, t);
		assertFalse(r.isAvailableAt(t));
	}

}
