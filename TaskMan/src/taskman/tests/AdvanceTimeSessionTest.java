package taskman.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import taskman.controller.branch.AdvanceTimeSession;
import taskman.model.time.Clock;
import taskman.view.IView;
import taskman.view.View;

public class AdvanceTimeSessionTest {
	private Clock clock;
	private AdvanceTimeSession session;
	private IView cli;

	@Rule
	public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();
	@Before
	public void setup(){
		clock = new Clock();
		cli = new View();
		session = new AdvanceTimeSession(cli, clock);

	}
	@Test(expected = IllegalArgumentException.class)
	public void nullClockTest(){
		AdvanceTimeSession session1 = new AdvanceTimeSession(cli, null);
	}
	@Test
	public void runSessionTest(){
		systemInMock.provideText("21-04-2020 11:11\n");

		session.run();
		
		//If the system time has been updated, then it should be present
		// in the buffer for printing the current time
		assertTrue(log.getLog().contains("21-04-2020 11:11"));
		
	}
	@Test
	public void runSessionTest_cancel(){
		systemInMock.provideText("cancel\n");

		session.run();
	}
	@Test(expected = NullPointerException.class)
	public void runSessionTest_nullTime(){
		systemInMock.provideText("21-04-1950 11:11\n");

		session.run();
		// Invalid time, the system time was not updated and the new time is not 
		// in the buffer for printing new time
		assertFalse(log.getLog().contains("21-04-1950 11:11"));
	}
}
