package taskman.tests;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.time.Clock;

public class ClockTest {
	private Clock clock;
	@Before
	public void setUp() throws Exception {
		clock = new Clock();
	}


	@Test
	public void constructorTest(){
		Clock c = new Clock();
		assertEquals(c.getSystemTime(), new DateTime(0));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void setSystemTime_Null(){
		clock.setSystemTime(null);
	}
	
	@Test
	public void setSystemTime_TrueCase(){
		
		DateTime dt = new DateTime(2015,10,12,10,10);
		clock.setSystemTime(dt);
		assertEquals(clock.getSystemTime(), dt);ss
	}
}
