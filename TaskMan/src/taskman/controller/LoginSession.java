package taskman.controller;

import java.util.Arrays;
import java.util.List;

import taskman.controller.branch.AdvanceTimeSession;
import taskman.controller.branch.CreateProjectSession;
import taskman.controller.branch.CreateTaskSession;
import taskman.controller.branch.DelegateTaskSession;
import taskman.controller.branch.PlanTaskSession;
import taskman.controller.branch.SimulateSession;
import taskman.controller.branch.UpdateTaskStatusSession;
import taskman.exceptions.ShouldExitException;
import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.user.Developer;
import taskman.model.user.ProjectManager;
import taskman.model.user.User;
import taskman.view.IView;

public class LoginSession extends Session {

	private final Company company;
	private final List<String> menuMain = Arrays.asList("Login", "Exit");
	private final List<String> menuDevelopers = Arrays.asList("Show projects",
			"Update task status", "Advance time", "Log out");
	private final List<String> menuProjectManagers = Arrays.asList(
			"Show projects", "Create project", "Create task", "Plan task",
			"Delegate task", "Advance time", "Start simulation", "Log out");

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
			showOptions((Developer) user, branchOffice);
		else if (user.isProjectManager())
			showOptions((ProjectManager) user, branchOffice);
		else
			return;
	}

	private void showOptions(Developer user, BranchOffice branchOffice) {
		while (true) {
			int menuId = getUI().getMainMenuID(menuDevelopers);

			switch (menuId) {
			case 1:
				new ShowAllProjectsSession(getUI(), company).run();
				break;
			case 2:
				new UpdateTaskStatusSession(getUI(), branchOffice.getPh(),
						branchOffice.getDth(), user).run();
				break;
			case 3:
				new AdvanceTimeSession(getUI(), branchOffice.getClock()).run();
				break;
			case 4:
				return;
			}
		}
	}

	private void showOptions(ProjectManager user, BranchOffice branchOffice) {
		while (true) {
			int menuId = getUI().getMainMenuID(menuProjectManagers);

			switch (menuId) {
			case 1:
				new ShowAllProjectsSession(getUI(), company).run();
				;
				break;
			case 2:
				new CreateProjectSession(getUI(), branchOffice.getPh(),
						branchOffice.getClock()).run();
				break;
			case 3:
				new CreateTaskSession(getUI(), branchOffice.getPh(),
						branchOffice.getRh()).run();
				;
				break;
			case 4:
				new PlanTaskSession(getUI(), branchOffice.getPh(),
						branchOffice.getUh(), branchOffice.getClock()).run();
				;
				break;
			case 5:
				new DelegateTaskSession(getUI(), branchOffice.getPh(),
						branchOffice.getDth(), company).run();
				break;
			case 6:
				new AdvanceTimeSession(getUI(), branchOffice.getClock()).run();
				break;
			case 7:
				new SimulateSession(getUI(), branchOffice.getPh(),
						branchOffice.getMh(), branchOffice.getRh(),
						branchOffice.getUh(), branchOffice.getDth(),
						branchOffice.getClock(), company).run();
				break;
			case 8:
				return;
			}
		}
	}

}
