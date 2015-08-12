package taskman.tests;

import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import taskman.Parser;
import taskman.model.company.Company;
import taskman.model.task.TaskFactory;
import taskman.model.time.Clock;
import taskman.view.IView;
import taskman.view.View;
import taskman.view.commandline.Input;

public class ParserTest {
	private Company company;
	private IView cli, cli1;
	private TaskFactory tf;
	private Clock clock;
	private Input input;
	@Rule
	public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();

	@Before
	public void setup() {
		clock = new Clock();
		cli = new View();
		company = new Company();
		input = new Input();

	}

	@Test
	public void parseTestNoException() {
		Parser parser = new Parser(company, input);
		parser.parse();
	}

}
