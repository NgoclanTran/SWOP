package taskman.tests;

import static org.junit.Assert.*;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import taskman.Parser;
import taskman.controller.branch.PlanTaskSession;
import taskman.model.company.Company;
import taskman.model.company.ProjectHandler;
import taskman.model.task.Task;
import taskman.model.task.TaskFactory;
import taskman.model.time.Clock;
import taskman.view.IView;
import taskman.view.View;

public class PlanTaskTestInputfile {
	private Company company;
	private IView cli, cli1;
	private TaskFactory tf;
	private Clock clock;
	@Rule
	public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();
	@Before
	public void setup(){
		clock = new Clock();
		cli = new View();
		company = new Company();
		Parser parser = new Parser(company);
		parser.parse();
	}
	@Test
	public void dummy(){
		tf = new TaskFactory(company.getBranchOffices().get(0), clock);
		for( Task t :company.getBranchOffices().get(0).getPh().getProjects().get(1).getTasks()){
			t.update();
			System.out.println(t.getDescription());
			System.out.println(t.getStatusName());
		}
		PlanTaskSession s = new PlanTaskSession(cli, new ProjectHandler(tf), company.getBranchOffices().get(0).getUh(), clock);
		s.run();

		assertTrue(log.getLog().contains("No unplanned tasks.\r\n\r\n"));
	}
	
}
