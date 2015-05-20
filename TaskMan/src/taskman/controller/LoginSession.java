package taskman.controller;

import taskman.model.company.Company;
import taskman.view.IView;

public class LoginSession extends AbstractCompanySession {

	/**
	 * Constructor of the login session.
	 * 
	 * @param cli
	 * @param company
	 * 
	 * @throws IllegalArgumentException
	 */
	public LoginSession(IView cli, Company company)
			throws IllegalArgumentException {
		super(cli, company);
	}

	/**
	 * Runs the login session.
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
