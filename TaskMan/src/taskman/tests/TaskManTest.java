package taskman.tests;

import static org.junit.Assert.*;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import taskman.TaskMan;

public class TaskManTest {

	@Rule
	public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();

	@Test
	public void testMain() {
		systemInMock.provideText("5");
		TaskMan.main(null);
		assertEquals(
				"TaskMan V2.0\r\n\r\n\r\n1. Show projects\r\n2. Create project\r\n3. Create task\r\n4. Update task\r\n5. Quit\r\n\r\nSelect an option:\r\n\r\n\r\n",
				log.getLog());
	}
}
