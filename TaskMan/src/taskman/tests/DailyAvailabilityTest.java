package taskman.tests;

import static org.junit.Assert.*;

import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import taskman.exceptions.IllegalTimeException;
import taskman.model.time.DailyAvailability;

public class DailyAvailabilityTest {
	private LocalTime startTime, endTime;
	@Before
	public void setUp() throws Exception {
		startTime = new LocalTime(10, 0);
		endTime = new LocalTime(16,0);
	}

	@Test (expected = NullPointerException.class)
	public void constructorTest_StartTime_Null() {
		DailyAvailability d = new DailyAvailability(null, endTime);
	}

	@Test (expected = NullPointerException.class)
	public void cosntructorTest_EndTime_Null(){
		DailyAvailability d = new DailyAvailability(startTime, null);	
	}

	@Test (expected = IllegalTimeException.class)
	public void constructorTest_StartTime_NotBefore_EndTime(){
		DailyAvailability d = new DailyAvailability(endTime, startTime);	
	}
	@Test
	public void constructorTest_StartTime_Equal_EndTime(){
		DailyAvailability d = new DailyAvailability(startTime, startTime);
		assertEquals(d.getStartTime(), startTime);
		assertEquals(d.getEndTime(), startTime);
	}

	@Test
	public void cosntructorTest_TrueCase(){
		DailyAvailability d = new DailyAvailability(startTime, endTime);	
		assertEquals(d.getStartTime(), startTime);
		assertEquals(d.getEndTime(), endTime);

	}
	
	@Test
	public void getterStartTimeTest(){
		DailyAvailability d = new DailyAvailability(startTime, endTime);
		LocalTime ld = d.getStartTime().plusHours(2);
		assertNotEquals(d.getStartTime(), ld);
	}
	
	@Test
	public void getterEndTimeTest(){
		DailyAvailability d = new DailyAvailability(startTime, endTime);
		LocalTime ld = d.getEndTime().plusHours(2);
		assertNotEquals(d.getEndTime(), ld);
	}
}
