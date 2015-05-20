package taskman.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import taskman.controller.branch.UpdateTaskStatusSession;
import taskman.model.company.ProjectHandler;
import taskman.model.company.ResourceHandler;
import taskman.model.company.UserHandler;
import taskman.model.task.Task;
import taskman.model.task.TaskFactory;
import taskman.model.time.Clock;
import taskman.model.time.TimeSpan;
import taskman.view.IView;
import taskman.view.View;

public class UpdateTaskStatusSessionTest {

	private UpdateTaskStatusSession updateTask;
	private ProjectHandler ph;
	private ResourceHandler rh;
	private UserHandler uh;
	private UpdateTaskStatusSession session;
	private TextFromStandardInputStream systemMock;
	private IView cli;
	private Clock clock = new Clock();
	private TaskFactory tf;
	@Rule
	public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();

	@Before
	public void setup() {
		tf = new TaskFactory(clock);
		cli = new View();
		clock.setSystemTime(new DateTime(2015, 10, 12, 8, 0));
		ph = new ProjectHandler(tf);
		rh = new ResourceHandler();
		uh = new UserHandler();
		session = new UpdateTaskStatusSession(cli, ph);
		ph.addProject("Project x", "Test project 1", new DateTime(),
				new DateTime(2016, 4, 1, 0, 0));
		ph.addProject("Project y", "Test project 2", new DateTime(),
				new DateTime(2016, 4, 1, 0, 0));
		ph.getProjects()
				.get(0)
				.addTask("Task description", 10, 1, new ArrayList<Task>(),
						null, null);
		uh.addDeveloper("developer");
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015, 10, 12, 8, 0),
				new DateTime(2015, 10, 12, 16, 0));
		uh.getDevelopers()
				.get(0)
				.addReservation(ph.getProjects().get(0).getTasks().get(0),
						timeSpan);
		ph.getProjects().get(0).getTasks().get(0)
				.addRequiredDeveloper(uh.getDevelopers().get(0));
		ph.getProjects().get(0).getTasks().get(0).update();
		ph.getProjects()
				.get(0)
				.addTask("Task description", 10, 1, new ArrayList<Task>(),
						null, null);
	}

	@Test
	public void useCase_NoTask() {
		IView cli = new View();
		ProjectHandler ph = new ProjectHandler(tf);
		ResourceHandler rh = new ResourceHandler();
		systemInMock.provideText("1\n8\n");
		UpdateTaskStatusSession session = new UpdateTaskStatusSession(cli, ph);
		session.run();
		String output = "No available tasks.";
		assertTrue(log.getLog().contains(output));
	}

	// @Test
	// public void useCase_SelectTask_CancelCreation(){
	// // ------------- Before running ---------------
	// ArrayList<Task> tasksOfProject1 = (ArrayList<Task>)
	// ph.getProjects().get(0).getTasks();
	// ArrayList<Task> tasksOfProject2 = (ArrayList<Task>)
	// ph.getProjects().get(1).getTasks();
	//
	// // System has one task
	// // Selection task
	// // Enter incorrect input: a
	// // Enter incorrect input : -1
	// // Enter incorrect input: 2 (system has only on task)
	// // Cancel creation
	//
	// systemInMock.provideText("a\n2\n-1\ncancel\n0");
	// session.run();
	//
	// //-------------- Check if nothing has changed or created ---------
	// assertEquals(session.getPH().getProjects().get(0).getTasks(),
	// tasksOfProject1);
	// assertEquals(session.getPH().getProjects().get(1).getTasks(),
	// tasksOfProject2);
	// }

	// @Test
	// public void useCase_FailedTask_Creation(){
	// // ------------- Before running ---------------
	// ArrayList<Task> tasksOfProject1 = (ArrayList<Task>)
	// ph.getProjects().get(0).getTasks();
	// ArrayList<Task> tasksOfProject2 = (ArrayList<Task>)
	// ph.getProjects().get(1).getTasks();
	//
	// // System has one task
	// // Selection task
	// // Select first task: 1
	// // Enter task has failed: Y
	// // Enter incorrect startTime: 14-10-205510:10
	// // Enter incorrect startTime: 30-02-2015 10:10
	// // Enter correct startTime: 21-04-2015 10:10
	// // Enter correct endTime: 21-04-2015 12:10
	//
	// systemInMock.provideText("1\n1\nY\n14-10-2055-10:10\n30-02-2015 10:10\n21-04-2015 10:10\n21-04-2015 12:10");
	// session.run();
	//
	// //-------------- Check if task has been updated ---------
	// assertEquals(session.getPH().getProjects().get(0).getTasks(),
	// tasksOfProject1);
	// assertEquals(session.getPH().getProjects().get(1).getTasks(),
	// tasksOfProject2);
	// String lastMessage = log.getLog().substring(log.getLog().length()-17);
	// assertEquals("Task updated.\r\n\r\n", lastMessage);
	// Task updatedTask = ph.getProjects().get(0).getTasks().get(0);
	// assertEquals(updatedTask.getStatusName(),"FAILED");
	// assertEquals(updatedTask.getTimeSpan().getStartTime().getMinuteOfDay(),610);
	// assertEquals(updatedTask.getTimeSpan().getEndTime().getMinuteOfDay(),730);
	// //(12:10 -> 730 min)
	//
	// }
	// @Test
	// public void useCase_ExecutingTask_Creation(){
	// // ------------- Before running ---------------
	// ArrayList<Task> tasksOfProject1 = (ArrayList<Task>)
	// ph.getProjects().get(0).getTasks();
	// ArrayList<Task> tasksOfProject2 = (ArrayList<Task>)
	// ph.getProjects().get(1).getTasks();
	//
	// // System has one task
	// // Selection task
	// // Select first task: 1
	// // Enter task has failed: N
	// // Enter correct startTime: 21-04-2015 10:10
	// // Enter incorrect endTime: 14-10-205510:10
	// // Enter incorrect endTime: 30-02-2015 10:10
	// // Enter correct endTime: 21-04-2015 12:10
	//
	// systemInMock.provideText("1\n1\nN\n21-04-2015 10:10\n14-10-2055-10:10\n30-02-2015 10:10\n21-04-2015 12:10");
	// session.run();
	//
	// //-------------- Check if task has been updated ---------
	// assertEquals(session.getPH().getProjects().get(0).getTasks(),
	// tasksOfProject1);
	// assertEquals(session.getPH().getProjects().get(1).getTasks(),
	// tasksOfProject2);
	// String lastMessage = log.getLog().substring(log.getLog().length()-17);
	// assertEquals("Task updated.\r\n\r\n", lastMessage);
	// Task updatedTask = ph.getProjects().get(0).getTasks().get(0);
	// assertEquals(updatedTask.getStatusName(),"EXECUTING");
	// assertEquals(updatedTask.getTimeSpan().getStartTime().getMinuteOfDay(),610);
	// assertEquals(updatedTask.getTimeSpan().getEndTime().getMinuteOfDay(),730);
	// //(12:10 -> 730 min)
	// }
	@Test
	public void useCase_SelectStatus_Cancel() {
		// ------------- Before running ---------------
		ArrayList<Task> tasksOfProject1 = (ArrayList<Task>) ph.getProjects()
				.get(0).getTasks();
		ArrayList<Task> tasksOfProject2 = (ArrayList<Task>) ph.getProjects()
				.get(1).getTasks();

		// System has one task
		// Selection task
		// Select first task: 1
		// Cancel creation when selecting status

		systemInMock.provideText("1\n1\ncancel\n0");
		session.run();

		// -------------- Check if task has been updated ---------
		assertEquals(session.getPH().getProjects().get(0).getTasks(),
				tasksOfProject1);
		assertEquals(session.getPH().getProjects().get(1).getTasks(),
				tasksOfProject2);
		Task updatedTask = ph.getProjects().get(0).getTasks().get(0);
		assertEquals(updatedTask.getStatusName(), "AVAILABLE");

	}

	// TODO TASK
	// Task update his status before checking his timespan
	// If timeSpan is invalid, the task has already changed his status
	@Test
	public void useCaseTest_IncorrectDateWorkingDay() {
		// ------------- Before running ---------------
		ArrayList<Task> tasksOfProject1 = (ArrayList<Task>) ph.getProjects()
				.get(0).getTasks();
		ArrayList<Task> tasksOfProject2 = (ArrayList<Task>) ph.getProjects()
				.get(1).getTasks();

		// System has one task
		// Selection task
		// Select first task: 1
		// Enter task has failed: N
		// Enter correct startTime: 21-04-2015 10:10
		// Enter incorrect endTime: 14-10-205510:10
		// Enter incorrect endTime: 30-02-2015 10:10
		// Enter correct endTime: 21-04-2015 12:10
		// System restart updateTaskStatusSession
		// Enter cancal creation

		systemInMock
				.provideText("1\n1\nN\n21-04-2015 10:10\n14-10-2055-10:10\n30-02-2015 10:10\n21-04-2015 22:10\ncancel\n0");
		session.run();

		// -------------- Check if nothing has changed or created ---------
		assertEquals(session.getPH().getProjects().get(0).getTasks(),
				tasksOfProject1);
		assertEquals(session.getPH().getProjects().get(1).getTasks(),
				tasksOfProject2);
		Task updatedTask = ph.getProjects().get(0).getTasks().get(0);
		assertEquals(updatedTask.getStatusName(), "AVAILABLE");

	}

	@Test
	public void useCaseTest_StatusExecution_Creation() {
		// TODO
	}

}
