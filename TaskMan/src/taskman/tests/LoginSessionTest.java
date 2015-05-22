package taskman.tests;

import org.junit.Before;
import org.junit.Test;

import taskman.controller.LoginSession;
import taskman.view.IView;

public class LoginSessionTest {

	private IView cli;

	@Before
	public void setup(){
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void constructorNullCompany(){
		LoginSession l = new LoginSession(cli, null);
	}
}
