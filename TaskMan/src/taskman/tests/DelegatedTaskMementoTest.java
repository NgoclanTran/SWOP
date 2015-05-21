package taskman.tests;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.Before;

import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.memento.DelegatedTaskMemento;
import taskman.model.memento.NormalTaskMemento;
import taskman.model.project.Project;
import taskman.model.task.NormalTask;
import taskman.model.time.Clock;
import taskman.model.time.TimeSpan;

public class DelegatedTaskMementoTest {
	
	ArrayList<NormalTask> dependants = new ArrayList<NormalTask>();
	String stateName;
	TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 11, 0), new DateTime(2015,
			10, 12, 15, 0));
	NormalTask alternative;
	private NormalTaskMemento t;
	private Clock clock = new Clock();
	private Company company;
	private BranchOffice branchOffice = new BranchOffice(company, "", null);
	@Before
	public void setup() {
		branchOffice.getPh().addProject("", "", new DateTime(), new DateTime());
		Project p = branchOffice.getPh().getProjects().get(0);
		p.addTask("", 10, 1, null, null, null);
		DelegatedTaskMemento dtm = new DelegatedTaskMemento(branchOffice.getPh().getProjects().get(0).getTasks().get(0), true, "", timeSpan)
	}
	
	
}

