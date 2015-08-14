package taskman.tests;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.memento.ClockMemento;
import taskman.model.time.Clock;

public class ClockMementoTest {
	private ClockMemento c;
	private DateTime SystemTime = new DateTime();
	@Before
	public void setup(){
		c = new ClockMemento(null, SystemTime);
	}
	@Test
	public void testState(){
		assertEquals(SystemTime, c.getState());
	}
	
	@Test
	public void getObjectTest(){
		Clock c = new Clock();
		ClockMemento c1 = new ClockMemento(c, SystemTime);
		assertEquals(c1.getObject(), c);
	}
}
