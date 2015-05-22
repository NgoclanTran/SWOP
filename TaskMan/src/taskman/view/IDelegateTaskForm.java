package taskman.view;

import java.util.List;

import taskman.exceptions.ShouldExitException;
import taskman.model.company.BranchOffice;

public interface IDelegateTaskForm {

	public BranchOffice getBranchOffice(List<BranchOffice> branchOffices)
			throws ShouldExitException;

}
