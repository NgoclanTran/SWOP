package taskman.tests;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.time.Clock;

public class ClockTest {

	private Clock c;
	@Before
	public void setUp() throws Exception {
		c = new Clock();
	}

	@Test (expected = IllegalArgumentException.class)
	public void setClockTest_Null(){
		c.setSystemTime(null);
	}
	
	@Test
	public void setClockTest(){
		DateTime d = new DateTime(2015,1,1,10,0);
		c.setSystemTime(d);
		assertEquals(c.getSystemTime(), d);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void getFirstPossibleTimeTest_Null(){
		c.getFirstPossibleStartTime(null);
	}
	@Test
	public void getFirstPossibleTimeTest1(){
		assertEquals(c.getFirstPossibleStartTime(new DateTime(2015,1,1,7,0)).getHourOfDay(),8);
		assertEquals(c.getFirstPossibleStartTime(new DateTime(2015,1,1,7,0)).getMinuteOfHour(),0);
		assertEquals(c.getFirstPossibleStartTime(new DateTime(2015,1,1,7,0)).getDayOfMonth(),1);
		assertEquals(c.getFirstPossibleStartTime(new DateTime(2015,1,1,7,0)).getMonthOfYear(),1);
		assertEquals(c.getFirstPossibleStartTime(new DateTime(2015,1,1,7,0)).getYear(),2015);
		
		
	}

	@Test
	public void getFirstPossibleTimeTest2(){
		assertEquals(c.getFirstPossibleStartTime(new DateTime(2015,1,1,17,0)).getHourOfDay(),8);
		assertEquals(c.getFirstPossibleStartTime(new DateTime(2015,1,1,17,0)).getMinuteOfHour(),0);
		//Volgende dag
		assertEquals(c.getFirstPossibleStartTime(new DateTime(2015,1,1,17,0)).getDayOfMonth(),2);
		assertEquals(c.getFirstPossibleStartTime(new DateTime(2015,1,1,17,0)).getMonthOfYear(),1);
		assertEquals(c.getFirstPossibleStartTime(new DateTime(2015,1,1,17,0)).getYear(),2015);
	}
	
	@Test
	public void getFirstPossibleTimeTest3(){
		assertEquals(c.getFirstPossibleStartTime(new DateTime(2015,5,2,10,0)).getDayOfWeek(),1);
		assertEquals(c.getFirstPossibleStartTime(new DateTime(2015,5,2,10,0)).getMonthOfYear(),5);
		assertEquals(c.getFirstPossibleStartTime(new DateTime(2015,5,2,10,0)).getYear(),2015);
		assertEquals(c.getFirstPossibleStartTime(new DateTime(2015,5,2,10,0)).getHourOfDay(),8);
		assertEquals(c.getFirstPossibleStartTime(new DateTime(2015,5,2,10,0)).getMinuteOfHour(),0);
		}
	
	@Test
	public void getFirstPossibleTimeTest4(){
		assertEquals(c.getFirstPossibleStartTime(new DateTime(2015,5,3,10,0)).getDayOfWeek(),1);
		assertEquals(c.getFirstPossibleStartTime(new DateTime(2015,5,3,10,0)).getMonthOfYear(),5);
		assertEquals(c.getFirstPossibleStartTime(new DateTime(2015,5,2,10,0)).getYear(),2015);
		assertEquals(c.getFirstPossibleStartTime(new DateTime(2015,5,2,10,0)).getHourOfDay(),8);
		assertEquals(c.getFirstPossibleStartTime(new DateTime(2015,5,2,10,0)).getMinuteOfHour(),0);	
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void addBreaksTest_Null(){
		c.addBreaks(null);
	}
	@Test
	public void addBreaksTest1(){
		assertEquals(c.addBreaks(new DateTime(2015,5,4,11,01)).getHourOfDay(),12);
		assertEquals(c.addBreaks(new DateTime(2015,5,4,11,01)).getMinuteOfHour(),1);
		assertEquals(c.addBreaks(new DateTime(2015,5,4,11,01)).getDayOfMonth(),4);
		assertEquals(c.addBreaks(new DateTime(2015,5,4,11,01)).getMonthOfYear(),5);
		assertEquals(c.addBreaks(new DateTime(2015,5,4,11,01)).getYear(),2015);
	}
	@Test
	public void addBreaksTest2(){
		assertEquals(c.addBreaks(new DateTime(2015,5,4,17,0)).getHourOfDay(),8);
		assertEquals(c.addBreaks(new DateTime(2015,5,4,17,0)).getMinuteOfHour(),0);
		assertEquals(c.addBreaks(new DateTime(2015,5,4,17,0)).getDayOfMonth(),5);
		assertEquals(c.addBreaks(new DateTime(2015,5,4,17,0)).getMonthOfYear(),5);
		assertEquals(c.addBreaks(new DateTime(2015,5,4,17,0)).getYear(),2015);		
	}
	@Test
	public void addBreaksTest3(){
		assertEquals(c.addBreaks(new DateTime(2015,5,9,11,01)).getHourOfDay(),12);
		assertEquals(c.addBreaks(new DateTime(2015,5,9,11,01)).getMinuteOfHour(),1);
		assertEquals(c.addBreaks(new DateTime(2015,5,9,11,01)).getDayOfMonth(),11);
		assertEquals(c.addBreaks(new DateTime(2015,5,9,11,01)).getMonthOfYear(),5);
		assertEquals(c.addBreaks(new DateTime(2015,5,9,11,01)).getYear(),2015);
	}
	@Test
	public void addBreaksTest4(){
		assertEquals(c.addBreaks(new DateTime(2015,5,8,10,01)).getHourOfDay(),10);
		assertEquals(c.addBreaks(new DateTime(2015,5,8,10,01)).getMinuteOfHour(),1);
		assertEquals(c.addBreaks(new DateTime(2015,5,8,10,01)).getDayOfMonth(),8);
		assertEquals(c.addBreaks(new DateTime(2015,5,8,10,01)).getMonthOfYear(),5);
		assertEquals(c.addBreaks(new DateTime(2015,5,8,10,01)).getYear(),2015);
	}
	
	@Test
	public void addMinutesTest1(){
		DateTime t = new DateTime(2015,5,8,10,1);
		DateTime t1 = c.addMinutes(t, 3);
		assertEquals(t1.getDayOfMonth(),8);
		assertEquals(t1.getMonthOfYear(),5);
		assertEquals(t1.getYear(),2015);
		assertEquals(t1.getHourOfDay(),10);
		assertEquals(t1.getMinuteOfHour(),4);
	}
	
	@Test
	public void addMinutesTest2(){
		DateTime t = new DateTime(2015,5,8,11,00);
		DateTime t1 = c.addMinutes(t, 1);
		assertEquals(t1.getDayOfMonth(),8);
		assertEquals(t1.getMonthOfYear(),5);
		assertEquals(t1.getYear(),2015);
		assertEquals(t1.getHourOfDay(),12);
		assertEquals(t1.getMinuteOfHour(),1);
	}
	
	@Test
	public void getExactHourTest1(){
		DateTime t = new DateTime(2015,5,8,23,00);
		DateTime t1 = c.getExactHour(t);
		assertEquals(t1.getDayOfMonth(),8);
		assertEquals(t1.getMonthOfYear(),5);
		assertEquals(t1.getYear(),2015);
		assertEquals(t1.getHourOfDay(),23);
		assertEquals(t1.getMinuteOfHour(),0);
	}
}
