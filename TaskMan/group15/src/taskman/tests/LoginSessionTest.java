package taskman.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import taskman.TaskMan;
import taskman.controller.LoginSession;
import taskman.model.company.Company;
import taskman.view.IView;
import taskman.view.View;

public class LoginSessionTest {

	private IView cli = new View();
	private Company company = new Company();
	@Rule
	public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();

	@Test(expected = IllegalArgumentException.class)
	public void constructorNullCompany() {
		LoginSession l = new LoginSession(cli, null);
	}

	public void constructorTest() {
		LoginSession l = new LoginSession(cli, company);
	}

	@Test
	public void succesScenarioTest() {

		systemInMock.provideText("1\n1\n1\n4\n4\n2\n");
		TaskMan.main(null);
		String lastStrings = log.getLog().substring(329, 387);

		// if updateTask status is present , the program selected the correct
		// user as wish

		assertTrue(lastStrings.contains("2. Update task status"));
	}
}
