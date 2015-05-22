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

public class ParserTest {
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

	}
	@Test
	public void parseTestNoException(){
		Parser parser = new Parser(company);
		parser.parse();
	}
	
}
