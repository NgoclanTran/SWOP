package taskman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.project.task.Task;
import taskman.model.resource.Reservation;
import taskman.model.time.TimeSpan;

public class ReservationTest {
	private Task task;
	private TimeSpan ts;
	@Before
	public void setUp() throws Exception {
		task = new Task("description", 10, 1,
				new ArrayList<Task>(), null);
		ts = new TimeSpan(new DateTime(2015,10,10,10,0), new DateTime(2015,10,10,16,0));
	}

	@Test (expected = NullPointerException.class)
	public void constructorTest_Task_Null(){
		Reservation r = new Reservation(null, ts);
	}
	
	@Test (expected = NullPointerException.class)
	public void constructorTest_TimeSpan_Null(){
		Reservation r = new Reservation(task, null);	
	}
	
	@Test 
	public void constructorTest_TrueCase(){
		Reservation r = new Reservation(task, ts);
		assertEquals(r.getTask(), task);
		assertEquals(r.getTimeSpan(), ts);
	}

}
