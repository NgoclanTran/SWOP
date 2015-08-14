package taskman.tests;

import static org.junit.Assert.*;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import taskman.TaskMan;

public class SimulateSessionTest {

	@Rule
	public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();
	@Test
	public void useCaseTest_ResetState_Succes() {

		systemInMock.provideText("3\n1\n1\n1\n7\n4\n2\n1\n2\n5\n1\n1\n2\n1\n8\n2");
		TaskMan.main(null);
		
		assertTrue(log.getLog().contains("State reset."));
		assertTrue(log.getLog().contains("Task:\n\tDescription: another task description\n\tStatus: UNAVAILABLE"));
	}
	
	@Test
	public void useCaseTest_executeDelegation_Succes() {

		systemInMock.provideText("3\n1\n1\n1\n7\n4\n2\n1\n2\n6\n1\n1\n2\n1\n8\n2");
		TaskMan.main(null);
		
		assertFalse(log.getLog().contains("State reset."));
		assertTrue(log.getLog().contains("Task:\n\tDescription: another task description\n\tStatus: DELEGATED"));
	}
}
