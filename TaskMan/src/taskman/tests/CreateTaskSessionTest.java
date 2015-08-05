package taskman.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import taskman.controller.branch.CreateTaskSession;
import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.company.ProjectHandler;
import taskman.model.company.ResourceHandler;
import taskman.model.project.Project;
import taskman.model.resource.ResourceType;
import taskman.model.task.NormalTask;
import taskman.model.task.Task;
import taskman.model.task.TaskFactory;
import taskman.model.time.Clock;
import taskman.view.IView;
import taskman.view.View;

public class CreateTaskSessionTest {
	private IView cli;
	private ProjectHandler ph;
	private ResourceHandler rh;
	private CreateTaskSession session;
	private String description;
	private int estimatedDuration;
	private int acceptableDeviation;
	private ArrayList<Task> dependencies;
	private Project p;
	private TaskFactory tf;
	private Clock clock;

	@Rule
	public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();
	private Company company = new Company();
	List<ResourceType> list = new ArrayList<ResourceType>();
	private BranchOffice branchOffice = new BranchOffice(company, "", list);

	@Before
	public void setup() {

		clock = new Clock();
		tf = new TaskFactory(branchOffice, clock);
		// cli = Mockito.mock(View.class);
		cli = new View();
		ph = new ProjectHandler(tf);
		rh = new ResourceHandler(new ArrayList<ResourceType>());
		//rh = new ResourceHandler(null);
		session = new CreateTaskSession(cli, ph, rh);
		ph.addProject("Project x", "Test project 1", new DateTime(),
				new DateTime(2016, 4, 1, 0, 0));
		ph.addProject("Project y", "Test project 2", new DateTime(),
				new DateTime(2016, 4, 1, 0, 0));
		ph.getProjects()
				.get(0)
				.addTask("Task description", 10, 1, new ArrayList<NormalTask>(),
						null, null, 1);
		ph.getProjects()
				.get(0)
				.addTask("Task description", 10, 1, new ArrayList<NormalTask>(),
						null, null, 1);
		description = "description";
		estimatedDuration = 10;
		acceptableDeviation = 1;
		dependencies = new ArrayList<Task>();
		p = ph.getProjects().get(0);

	}
	@Test(expected = IllegalArgumentException.class)
	public void nullPhTest(){
		CreateTaskSession session2 = new CreateTaskSession(cli, null, rh);
	}
	@Test(expected = IllegalArgumentException.class)
	public void useCaseTest_nullRh() {
		CreateTaskSession session2 = new CreateTaskSession(cli, ph, null);
	}

	// @Test
	// public void useCaseTest_NoProject(){
	// IView cli = new View();
	// ProjectHandler ph = new ProjectHandler();
	// ResourceHandler rh = new ResourceHandler();
	// CreateTaskSession session = new CreateTaskSession(cli, ph, rh);
	//
	// session.run();
	// String output ="No projects.\r\n\r\n";
	// assertEquals(output,log.getLog());
	// }

	@Test
	public void useCaseTest_WithProjects_WrongInput_Exit_CancelCreation() {

		// ---------- Before running ----------
		ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();
		ArrayList<NormalTask> tasksOfProject1 = (ArrayList<NormalTask>) projects.get(0)
				.getTasks();
		ArrayList<NormalTask> tasksOfProject2 = (ArrayList<NormalTask>) projects.get(1)
				.getTasks();

		systemInMock.provideText("-1\n0");
		session.run();

		// -------- Check if nothing has been changed -------
		assertEquals(ph.getProjects(), projects);
		assertEquals(ph.getProjects().get(0).getTasks(), tasksOfProject1);
		assertEquals(ph.getProjects().get(1).getTasks(), tasksOfProject2);

	}

	@Test
	public void useCaseTest_WithProjects_InputExit_CancelCreation() {
		// ---------- Before running ----------
		ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();
		ArrayList<NormalTask> tasksOfProject1 = (ArrayList<NormalTask>) projects.get(0)
				.getTasks();
		ArrayList<NormalTask> tasksOfProject2 = (ArrayList<NormalTask>) projects.get(1)
				.getTasks();

		systemInMock.provideText("0");
		session.run();

		// -------- Check if nothing has been changed -------
		assertEquals(ph.getProjects(), projects);
		assertEquals(ph.getProjects().get(0).getTasks(), tasksOfProject1);
		assertEquals(ph.getProjects().get(1).getTasks(), tasksOfProject2);

	}

	@Test
	public void useCaseTest_WithProjects_InputOutOfBound_CancelCreation() {
		// ---------- Before running ----------
		ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();
		ArrayList<NormalTask> tasksOfProject1 = (ArrayList<NormalTask>) projects.get(0)
				.getTasks();
		ArrayList<NormalTask> tasksOfProject2 = (ArrayList<NormalTask>) projects.get(1)
				.getTasks();

		systemInMock.provideText("3\n0");
		session.run();

		// -------- Check if nothing has been changed -------
		assertEquals(ph.getProjects(), projects);
		assertEquals(ph.getProjects().get(0).getTasks(), tasksOfProject1);
		assertEquals(ph.getProjects().get(1).getTasks(), tasksOfProject2);

	}

	@Test
	public void useCaseTest_NoDescription_Stop_CancelCreation() {
		// ---------- Before running ----------
		ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();
		ArrayList<NormalTask> tasksOfProject1 = (ArrayList<NormalTask>) projects.get(0)
				.getTasks();
		ArrayList<NormalTask> tasksOfProject2 = (ArrayList<NormalTask>) projects.get(1)
				.getTasks();

		systemInMock.provideText("1\ncancel\n");
		session.run();

		// -------- Check if nothing has been changed -------
		assertEquals(ph.getProjects(), projects);
		assertEquals(ph.getProjects().get(0).getTasks(), tasksOfProject1);
		assertEquals(ph.getProjects().get(1).getTasks(), tasksOfProject2);

	}

	@Test
	public void useCaseTest_Description_Stop_CancelCreation() {
		// ---------- Before running ----------
		ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();
		ArrayList<NormalTask> tasksOfProject1 = (ArrayList<NormalTask>) projects.get(0)
				.getTasks();
		ArrayList<NormalTask> tasksOfProject2 = (ArrayList<NormalTask>) projects.get(1)
				.getTasks();

		systemInMock.provideText("1\ndescription\ncancel\n0");
		session.run();

		// -------- Check if nothing has been changed -------
		assertEquals(ph.getProjects(), projects);
		assertEquals(ph.getProjects().get(0).getTasks(), tasksOfProject1);
		assertEquals(ph.getProjects().get(1).getTasks(), tasksOfProject2);

	}

	@Test
	public void useCaseTest_EnterEstimatedDurationUntilCorrectInput_CancelCreation() {
		// ---------- Before running ----------
		ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();
		ArrayList<NormalTask> tasksOfProject1 = (ArrayList<NormalTask>) projects.get(0)
				.getTasks();
		ArrayList<NormalTask> tasksOfProject2 = (ArrayList<NormalTask>) projects.get(1)
				.getTasks();

		systemInMock.provideText("1\ndescription\n-1\n0\n10\ncancel\n0");
		session.run();

		// -------- Check if nothing has been changed -------
		assertEquals(ph.getProjects(), projects);
		assertEquals(ph.getProjects().get(0).getTasks(), tasksOfProject1);
		assertEquals(ph.getProjects().get(1).getTasks(), tasksOfProject2);
	}

	@Test
	public void useCaseTest_EnterAcceptableDeviationUntilCorrectInput_CancelCreation() {
		// ---------- Before running ----------
		ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();
		ArrayList<NormalTask> tasksOfProject1 = (ArrayList<NormalTask>) projects.get(0)
				.getTasks();
		ArrayList<NormalTask> tasksOfProject2 = (ArrayList<NormalTask>) projects.get(1)
				.getTasks();

		systemInMock.provideText("1\ndescription\n10\n-1\na\n10\ncancel\n0");
		session.run();

		// -------- Check if nothing has been changed -------
		assertEquals(ph.getProjects(), projects);
		assertEquals(ph.getProjects().get(0).getTasks(), tasksOfProject1);
		assertEquals(ph.getProjects().get(1).getTasks(), tasksOfProject2);
	}

	@Test
	public void useCaseTest_InputDependenciesYes_CancelCreation() {
		// ---------- Before running ----------
		ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();
		ArrayList<NormalTask> tasksOfProject1 = (ArrayList<NormalTask>) projects.get(0)
				.getTasks();
		ArrayList<NormalTask> tasksOfProject2 = (ArrayList<NormalTask>) projects.get(1)
				.getTasks();

		// select project 1
		// enter description "description"
		// enter estimated duration: 10
		// enter acceptable devation: 10
		// enter wrong input for dependencies: a
		// enter task has dependency: Y
		// select first task in given list
		// cancel creation

		systemInMock.provideText("1\ndescription\n10\n10\na\nY\n1\ncancel\n0");
		session.run();

		// -------- Check if nothing has been changed -------
		assertEquals(ph.getProjects(), projects);
		// assertEquals(ph.getProjects().get(0).getTasks(), tasksOfProject1);
		assertEquals(ph.getProjects().get(1).getTasks(), tasksOfProject2);
		Task lt = ph.getProjects().get(0).getTasks()
				.get(ph.getProjects().get(0).getTasks().size() - 1);
		assertEquals(lt.getDescription(), "Task description");
		assertEquals(lt.getEstimatedDuration(), 10);
		assertEquals(lt.getAcceptableDeviation(), 1);
		assertEquals(((NormalTask) lt).getDependencies().size(), 0);
		assertNull(((NormalTask) lt).getAlternative());
	}

	@Test
	public void useCaseTest_InputDependenciesNo_CancelCreation() {
		// ---------- Before running ----------
		ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();
		ArrayList<NormalTask> tasksOfProject1 = (ArrayList<NormalTask>) projects.get(0)
				.getTasks();
		ArrayList<NormalTask> tasksOfProject2 = (ArrayList<NormalTask>) projects.get(1)
				.getTasks();

		// select project 1
		// enter description "description"
		// enter estimated duration: 10
		// enter acceptable devation: 1
		// enter wrong input for dependencies: a
		// enter task has dependency: N
		// cancel creation

		systemInMock.provideText("1\ndescription\n10\n10\na\ncancel\n0");
		session.run();

		// -------- Check if nothing has been changed -------
		assertEquals(ph.getProjects(), projects);
		assertEquals(ph.getProjects().get(0).getTasks(), tasksOfProject1);
		assertEquals(ph.getProjects().get(1).getTasks(), tasksOfProject2);
	}

	@Test
	public void useCaseTest_InputAlterativeForNo_CancelCreation() {

		// ---------- Before running ----------
		ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();
		ArrayList<NormalTask> tasksOfProject1 = (ArrayList<NormalTask>) projects.get(0)
				.getTasks();
		ArrayList<NormalTask> tasksOfProject2 = (ArrayList<NormalTask>) projects.get(1)
				.getTasks();

		// select project 1
		// enter description "description"
		// enter estimated duration: 10
		// enter acceptable devation: 1
		// enter task has dependency: N
		// cancel creation

		systemInMock.provideText("1\ndescription\n10\n10\nN\nN\ncancel\n0");
		session.run();

		// -------- Check if task was created -------
		assertEquals(ph.getProjects(), projects);
		Task lastAddedTask = ph.getProjects().get(0).getTasks()
				.get(tasksOfProject1.size()-1);
		assertEquals(lastAddedTask.getDescription(), "Task description");
		assertNull(((NormalTask) lastAddedTask).getAlternative());
		assertEquals(lastAddedTask.getEstimatedDuration(), 10);
		assertEquals(lastAddedTask.getAcceptableDeviation(), 1);
		assertEquals(ph.getProjects().get(1).getTasks(), tasksOfProject2);
	}

	@Test
	public void useCaseTest_SuccesScenario_WithoutDependencies_WithoutAlternative() {
		// ---------- Before running ----------
		ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();
		ArrayList<NormalTask> tasksOfProject1 = (ArrayList<NormalTask>) projects.get(0)
				.getTasks();
		ArrayList<NormalTask> tasksOfProject2 = (ArrayList<NormalTask>) projects.get(1)
				.getTasks();

		// select project 1
		// enter description "description"
		// enter estimated duration: 10
		// enter acceptable devation: 1
		// enter wrong input for dependencies: a
		// enter task has dependency: Y
		// select first task in given list
		// cancel creation

		systemInMock
				.provideText("1\ndescription\n10\n10\na\nY\n1\nN\ncancel\n0");
		session.run();

		// -------- Check if nothing has been changed -------
		assertEquals(ph.getProjects(), projects);
		// New task was created
		assertEquals(ph.getProjects().get(1).getTasks(), tasksOfProject2);

		Task newTask = ph.getProjects().get(0).getTasks()
				.get(tasksOfProject1.size()-1);
		assertEquals(newTask.getDescription(), "Task description");
		assertEquals(newTask.getAcceptableDeviation(), 1);
		assertEquals(newTask.getEstimatedDuration(), 10);
	}
}
