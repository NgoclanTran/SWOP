package taskman.view;

import java.util.List;

import taskman.exceptions.ShouldExitException;
import taskman.model.company.BranchOffice;
import taskman.model.project.Project;

public interface IShowAllBranchesForm {

	public BranchOffice getBranchOffice(List<BranchOffice> branchOffices,
			List<List<Project>> projects) throws ShouldExitException;

}
