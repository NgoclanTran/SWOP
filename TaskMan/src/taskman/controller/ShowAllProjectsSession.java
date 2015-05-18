package taskman.controller;

import taskman.model.company.Company;
import taskman.view.IView;

public class ShowAllProjectsSession extends AbstractCompanySession {

	/**
	 * The show all projects controller.
	 * 
	 * @param cli
	 * @param company
	 * 
	 * @throws IllegalArgumentException
	 */
	public ShowAllProjectsSession(IView cli, Company company)
			throws IllegalArgumentException {
		super(cli, company);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
