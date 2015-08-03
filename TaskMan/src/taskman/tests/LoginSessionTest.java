package taskman.tests;

import static org.junit.Assert.*;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import taskman.TaskMan;
import taskman.controller.LoginSession;
import taskman.model.company.Company;
import taskman.view.IView;

public class LoginSessionTest {

	private IView cli;
	@Rule
	public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();

	
	@Test(expected = IllegalArgumentException.class)
	public void constructorNullCompany(){
		LoginSession l = new LoginSession(cli, null);
	}
	
	@Test
	public void succesScenarioTest(){
		
		systemInMock.provideText("\n\n1\n1\n1\n4\n4\n2\n");
		TaskMan.main(null);
		
		assertTrue(log.getLog().contains("1. Show projects/n2. Update task status/n3. Advance time/n4. Log out/n"));
	}
}
