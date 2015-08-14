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
		systemInMock.provideText("3\n1\n1\n1\n4\n2\n1\n1\n1\nn\n4\n2\n1\n1\n1\nn\ny\n4\n14-08-2015 09:00\n1\nn\n8\n2");
		TaskMan.main(null);

		// Check of de taak gepland is
		String logging = log.getLog();
		int start = logging.indexOf("Conflicting tasks:");
		logging = logging.substring(start);
		assertTrue(logging.contains("Task planned."));
	}

	@Test
	public void useCaseTestSuccesRescheduleOtherTask() {
		systemInMock.provideText("3\n1\n1\n1\n4\n2\n1\n1\n1\nn\n4\n2\n1\n1\n1\nn\nn\n1\n4\n14-08-2015 09:00\n1\nn\n1\n1\nn\n8\n2");
		TaskMan.main(null);

		// Check of de taak gepland is
		String logging = log.getLog();
		int start = logging.indexOf("Conflicting tasks:");
		logging = logging.substring(start);
		assertTrue(logging.contains("Task planned."));
	}

}
