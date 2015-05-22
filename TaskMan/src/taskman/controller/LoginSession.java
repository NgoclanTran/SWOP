package taskman.controller;

import java.util.Arrays;
import java.util.List;

import taskman.exceptions.ShouldExitException;
import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.user.User;
import taskman.view.IView;

public class LoginSession extends Session {

	private final Company company;
	private final List<String> menuMain = Arrays.asList("Login", "Exit");
	private final List<String> menuDevelopers = Arrays.asList("Show projects",
			"Update task status", "Advance time", "Return");
	private final List<String> menuProjectManagers = Arrays.asList(
			"Show projects", "Create project", "Create task", "Plan task",
			"Delegate task", "Advance time", "Start simulation", "Return");

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
		super(cli);
		if (company == null)
			throw new IllegalArgumentException(
					"The login controller needs a company");
		this.company = company;
	}

	/**
	 * Runs the login session.
	 */
	@Override
	public void run() {
		showCompany();
		showMainMenu();
	}

	private void showCompany() {
		getUI().getLoginForm().displayCompany(company.getName());
	}

	private void showMainMenu() {
		while (true) {
			getUI().displayInfo("Main menu:");
			int menuId = getUI().getMainMenuID(menuMain);

			switch (menuId) {
			case 1:
				showBranches();
				break;
			case 2:
				return;
			}
		}
	}

	private void showBranches() {
		List<BranchOffice> branchOffices = company.getBranchOffices();

		if (branchOffices.size() == 0) {
			getUI().displayError("No branch offices.");
			return;
		}

		BranchOffice branchOffice;
		try {
			branchOffice = getUI().getLoginForm()
					.getBranchOffice(branchOffices);
		} catch (ShouldExitException e) {
			return;
		}

		showUsers(branchOffice);
	}

	private void showUsers(BranchOffice branchOffice) {
		List<User> users = branchOffice.getUh().getUsers();

		if (users.size() == 0) {
			getUI().displayError("No users.");
			return;
		}

		User user;
		try {
			user = getUI().getLoginForm().getUser(users);
		} catch (ShouldExitException e) {
			return;
		}

		if (user.isDeveloper())
			showDeveloperOptions();
		else if (user.isProjectManager())
			showProjectManagerOptions();
		else
			return;
	}

	private void showDeveloperOptions() {
		while (true) {
			int menuId = getUI().getMainMenuID(menuDevelopers);

			// TODO
			switch (menuId) {
			case 1:
				new ShowAllProjectsSession(getUI(), company).run();
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				return;
			}
		}
	}

	private void showProjectManagerOptions() {
		while (true) {
			int menuId = getUI().getMainMenuID(menuProjectManagers);

			// TODO
			switch (menuId) {
			case 1:
				new ShowAllProjectsSession(getUI(), company).run();;
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				break;
			case 9:
				return;
			}
		}
	}

}
