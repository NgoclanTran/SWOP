package taskman.tests;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.time.TimeService;

public class TimeServiceTest {
	private TimeService s;

	@Before
	public void setUp() throws Exception {
		s = new TimeService();
	}

	@Test (expected = IllegalArgumentException.class)
	public void getFirstPossibleStartTimeTestt_DateTimeNull(){
		s.getFirstPossibleStartTime(null);
	}
	
	@Test
	public void getFirstPossibleStartTimeTest1(){
		assertEquals(s.getFirstPossibleStartTime(new DateTime(2015,10,12,7,0)), new DateTime(2015,10,12,8,0));
	}
	
	@Test
	public void getFirstPossibleStartTimeTest2(){
		assertEquals(s.getFirstPossibleStartTime(new DateTime(2015,10,12,17,0)), new DateTime(2015,10,13,8,0));
	}
	@Test
	public void getFirstPossibleStartTimeTest3(){
		assertEquals(s.getFirstPossibleStartTime(new DateTime(2015,5,23,8,0)), new DateTime(2015,5,25,8,0));
	}
	
	@Test
	public void getFirstPossibleStartTimeTest4(){
		assertEquals(s.getFirstPossibleStartTime(new DateTime(2015,5,24,8,0)), new DateTime(2015,5,25,8,0));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void addBreaksTest_NullTime(){
		s.addBreaks(null);
	}
	
	@Test
	public void addBreaksTest1(){
		assertEquals(s.addBreaks(new DateTime(2015,5,20,11,1)),new DateTime(2015,5,20,12,1));
	}
	
	@Test
	public void addBreaksTest2(){
		assertEquals(s.addBreaks(new DateTime(2015,5,20,17,0)),new DateTime(2015,5,21,8,0));
	}
	@Test
	public void addBreaksTest3(){
		assertEquals(s.addBreaks(new DateTime(2015,5,23,9,1)),new DateTime(2015,5,25,9,1));
	}
	
	@Test (expected= IllegalArgumentException.class)
	public void addMinutesTest_TimeNull(){
		s.addMinutes(null, 0);
		
	}
	
	@Test
	public void addMinutesTest1(){
		assertEquals(s.addMinutes(new DateTime(2015,5,20,11,1), 10),new DateTime(2015,5,20,12,11)); 
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void subtractMinutes_TimeNull(){
		s.subtractMinutes(null, 0);
	}
	
	@Test
	public void subtractMinutesTest1(){
		assertEquals(s.subtractMinutes(new DateTime(2015,5,20,8,10),10),new DateTime(2015,5,19,17,0));
	}
	
	@Test
	public void subtractMinutesTest2(){
		assertEquals(s.subtractMinutes(new DateTime(2015,5,20,7,10),10),new DateTime(2015,5,19,16,51));
	}
	
	@Test
	public void subtractMinutesTest3(){
		assertEquals(s.subtractMinutes(new DateTime(2015,5,24,8,10),10),new DateTime(2015,5,21,17,0));
	}
	@Test
	public void subtractMinutesTest4(){
		assertEquals(s.subtractMinutes(new DateTime(2015,5,20,12,10),10),new DateTime(2015,5,20,11,0));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void getExactHourTest_TimeNull(){
		s.getExactHour(null);
	}
	
	@Test
	public void getExactHourTest1(){
		assertEquals(s.getExactHour(new DateTime(2015,5,22,10,0)), new DateTime(2015,5,22,10,0));
	}
}
