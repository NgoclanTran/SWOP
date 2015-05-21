package taskman.view;

import java.util.List;

import taskman.exceptions.ShouldExitException;
import taskman.model.company.BranchOffice;
import taskman.model.user.User;

public interface ILoginForm {
	
	public void displayCompany(String name);
	
	public BranchOffice getBranchOffice(List<BranchOffice> branchOffices) throws ShouldExitException;
	
	public User getUser(List<User> users) throws ShouldExitException;

}
