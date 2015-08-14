package taskman.tests;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.exceptions.IllegalDateException;
import taskman.model.time.TimeSpan;

public class TimeSpanTest {
	private DateTime start;
	private DateTime end;

	@Before
	public void setup(){
		start = new DateTime(2015,1,1,10,1);
		end = new DateTime(2015,1,1,11,1);
	}

	@Test
	public void constructorTest_TrueCase(){
		TimeSpan ts = new TimeSpan(start, end);
		assertEquals(ts.getStartTime(), start);
		assertEquals(ts.getEndTime(), end);
	}

	@Test (expected = IllegalArgumentException.class)
	public void constructorTest_FalseCase_StartEndTime(){
		DateTime start = new DateTime(2015,1,1,12,1);
		DateTime end = new DateTime(2014,1,1,12,1);
		TimeSpan ts = new TimeSpan(start, end);
	}

	@Test (expected = IllegalDateException.class)
	public void constructorTest_FalseCase_StartTime(){
		DateTime start = new DateTime(2015,1,1,1,1);
		TimeSpan ts = new TimeSpan(start, end);
	}
	
	@Test (expected = IllegalDateException.class)
	public void constructorTest_FalseCase_EndTime(){
		DateTime t = new DateTime(2016,1,1,20,1);
		TimeSpan ts = new TimeSpan(start, t);
		
	}
	@Test (expected = IllegalDateException.class)
	public void constructorTest_FalseCase_DayOfTheWeekEndTime(){
		DateTime t = new DateTime(2015,7,25,14,1);
		TimeSpan ts = new TimeSpan(start, t);
		
	}
	@Test (expected = IllegalDateException.class)
	public void constructorTest_FalseCase_DayOfTheWeekStartTime(){
		DateTime t = new DateTime(2015,7,25,14,1);
		DateTime t1 = new DateTime(2015,7,29,14,1);
		TimeSpan ts = new TimeSpan(t, t1);
		
	}
	@Test
	public void calculatedPerformedTimeTest_TrueCase(){
		TimeSpan ts = new TimeSpan(start, end);
		assertEquals(ts.calculatePerformedTime(),60);
	}
	
	@Test
	public void isDuringTimeSpanTest(){
		
		TimeSpan ts = new TimeSpan(start, end);
		assertTrue(ts.isDuringTimeSpan(new DateTime(2015,1,1,10,2)));
		assertFalse(ts.isDuringTimeSpan(new DateTime(2016,1,1,10,2)));
		assertFalse(ts.isDuringTimeSpan(new DateTime(2000,1,1,10,2)));

	}
	


}
