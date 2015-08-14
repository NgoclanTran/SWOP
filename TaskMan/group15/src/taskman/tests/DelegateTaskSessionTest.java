package taskman.tests;

import static org.junit.Assert.*;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import taskman.TaskMan;

public class DelegateTaskSessionTest {

	@Rule
	public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();



	@Test
	public void useCaseTest_Succes() {

		systemInMock.provideText("3\n1\n1\n1\n5\n2\n2\n2\n1\n1\n2\n2\n8\n2");
		TaskMan.main(null);

		// Check if the selected task was delegated
		// status is DELEGATED
		// the new Branch Office is Gent
		// there is only one delegated task
		
		assertTrue(log.getLog().contains("Status: DELEGATED\n\tResponsible branch office: Gent"));
	}

}
