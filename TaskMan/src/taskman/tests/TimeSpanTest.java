package taskman.tests;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.exceptions.IllegalDateException;
import taskman.model.project.task.TimeSpan;

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
	
	@Test
	public void calculatedPerformedTimeTest_TrueCase(){
		TimeSpan ts = new TimeSpan(start, end);
		assertEquals(ts.calculatePerformedTime(),60);
	}

}
