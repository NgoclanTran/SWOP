package taskman.tests;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.memento.ClockMemento;

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
}
