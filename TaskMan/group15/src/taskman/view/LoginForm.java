package taskman.view;

import java.util.List;

import taskman.exceptions.ShouldExitException;
import taskman.model.company.BranchOffice;
import taskman.model.user.User;

public class LoginForm implements ILoginForm {

	private View view;

	/**
	 * The constructor of the login form. It will setup the view to be able to
	 * get input and output.
	 * 
	 * @param view
	 */
	public LoginForm(View view) {
		this.view = view;
	}

	@Override
	public void displayCompany(String companyName) {
		view.output.displayEmptyLine();
		view.displayInfo(companyName);
	}

	/**
	 * This method will show all the branch offices and ask the user to select
	 * one. When the user selected one, it will return this branch office.
	 * 
	 * @param branchOffices
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the login.
	 */
	@Override
	public BranchOffice getBranchOffice(List<BranchOffice> branchOffices)
			throws ShouldExitException {
		try {
			displayBranchOffices(branchOffices);
			view.output.displayEmptyLine();
			int branchOfficeId = view.getListChoice(branchOffices,
					"Select a branch office:");
			return branchOffices.get(branchOfficeId - 1);
		} catch (ShouldExitException e) {
			view.output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	/**
	 * This method will print all the branch offices.
	 * 
	 * @param branchOffices
	 */
	private void displayBranchOffices(List<BranchOffice> branchOffices) {
		view.output.displayList(branchOffices, 0, true);
	}

	/**
	 * This method will show all the users and ask the user to select
	 * one. When the user selected one, it will return this user.
	 * 
	 * @param users
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the login.
	 */
	@Override
	public User getUser(List<User> users) throws ShouldExitException {
		try {
			displayUsers(users);
			view.output.displayEmptyLine();
			int usersId = view.getListChoice(users,
					"Select a user:");
			return users.get(usersId - 1);
		} catch (ShouldExitException e) {
			view.output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}
	
	/**
	 * This method will print all the users.
	 * 
	 * @param users
	 */
	private void displayUsers(List<User> users) {
		view.output.displayList(users, 0, true);
	}

}
