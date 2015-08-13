package taskman.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import taskman.TaskMan;

public class ResolveConflictSessionTest {
	
	@Rule
	public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();
	
	@Test
	public void useCaseTestSuccesRescheduleOriginalTask() {
		systemInMock.provideText("3\n1\n1\n1\n4\n2\n1\n1\n1\nn\ny\n1\n2\nn\n8\n2");
		TaskMan.main(null);

		// Check of de taak gepland is
		assertTrue(log.getLog().contains("Task planned."));
	}
	
	@Test
	public void useCaseTestSuccesRescheduleOtherTask() {
		systemInMock.provideText("3\n1\n1\n1\n4\n2\n1\n1\n1\nn\nn\n2\n1\n2\nn\n4\n01-04-2015 10:00\n1\nn\n8\n2");
		TaskMan.main(null);

		// Check of de taak gepland is
		assertTrue(log.getLog().contains("Task planned."));
	}

}
