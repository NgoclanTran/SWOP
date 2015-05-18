package taskman.controller;

import java.util.Arrays;
import java.util.List;

import taskman.model.company.Company;
import taskman.view.IView;

public class MainSession extends AbstractCompanySession {

	private final List<String> menu = Arrays.asList("Show all projects",
			"Login");

	/**
	 * Constructor of the main controller.
	 * 
	 * @param cli
	 * @param company
	 * 
	 * @throws IllegalArgumentException
	 */
	public MainSession(IView cli, Company company)
			throws IllegalArgumentException {
		super(cli, company);
	}

	/**
	 * Runs the initial main menu asking the user what to do.
	 */
	@Override
	public void run() {
		getUI().displayWelcome();

		while (true) {
			int menuId = getUI().getMainMenuID(menu);

			switch (menuId) {
			case 1:
				new ShowAllProjectsSession(cli, getCompany()).run();
				break;
			case 2:
				new LoginSession(cli, getCompany()).run();
				break;
			case 3:
				return;
			}
		}
	}
}
