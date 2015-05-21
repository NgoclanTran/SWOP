package taskman.tests;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.memento.ClockMemento;
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
		assertEquals(clock.getSystemTime(), dt);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void advanceTimeTest_Null(){
		clock.advanceSystemTime(null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void advanceTimeTest_BeforeTime(){
		//TODO werkt niet voor jaar 2000
		clock.advanceSystemTime(new DateTime(200,10,12,10,10));
	}
	
	@Test
	public void advanceTimeTest_True(){
		DateTime dt = new DateTime(2016,10,12,10,10);
		clock.advanceSystemTime(dt);
		assertEquals(clock.getSystemTime(), dt);
	}
	
	@Test
	public void createMementoTest(){
		ClockMemento c = clock.createMemento();
		assertEquals(c.getObject(), clock);
		assertEquals(c.getState(), clock.getSystemTime());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void setMemento_Null(){
		clock.setMemento(null);
	}
	
	@Test
	public void setMementoTest(){
		DateTime time = clock.getSystemTime();
		ClockMemento c = clock.createMemento();
		
		DateTime dt = new DateTime(2016,10,12,10,10);
		clock.advanceSystemTime(dt);
		
		clock.setMemento(c);
		assertEquals(clock.getSystemTime(), time);
	}
}
