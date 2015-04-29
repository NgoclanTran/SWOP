package taskman.tests;

import taskman.exceptions.IllegalDateException;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.project.task.Task;
import taskman.model.time.TimeSpan;

public class TaskTest {
	private String description;
	private int estimatedDuration;
	private int acceptableDeviation;
	private List<Task> dependencies;
	private TimeSpan timespan;

	@Before
	public void setup() {
		description = "description";
		estimatedDuration = 10;
		acceptableDeviation = 1;
		dependencies = new ArrayList<Task>();

	}

	@Test(expected = NullPointerException.class)
	public void constructor1Test_FalseCase_DescriptionNull() {
		Task t = new Task(null, estimatedDuration, acceptableDeviation,
				dependencies, null, null);

	}

	@Test(expected = IllegalArgumentException.class)
	public void constructor1Test_FalseCase_EstimatedDuration() {
		Task t = new Task(description, -10, acceptableDeviation, dependencies,
				null, null);

	}

	@Test(expected = IllegalArgumentException.class)
	public void constructor1Test_FalseCase_acceptableDeviation() {
		Task t = new Task(description, estimatedDuration, -10, dependencies,
				null, null);
	}

	@Test(expected = NullPointerException.class)
	public void constructor2Test_FalseCase_DescriptionNull() {
		Task t = new Task(null, estimatedDuration, acceptableDeviation,
				dependencies, null, null);

	}

	@Test(expected = IllegalArgumentException.class)
	public void constructor2Test_FalseCase_EstimatedDuration() {
		Task t = new Task(description, -10, acceptableDeviation, dependencies,
				null, null);

	}

	@Test(expected = IllegalArgumentException.class)
	public void constructor2Test_FalseCase_acceptableDeviation() {
		Task t = new Task(description, estimatedDuration, -10, dependencies,
				null, null);
	}
	
	@Test
	public void constructorTest_WithAlternative(){
		Task t1 = new Task(description, estimatedDuration, acceptableDeviation,
				dependencies, null, null);
		assertEquals(t1.getStatusName(),"AVAILABLE");
//		DateTime startTime = new DateTime(2015,1,1,10,1);
//		DateTime endTime = new DateTime(2015,2,2,11,1);
//		t1.addTimeSpan(true, startTime, endTime);
//		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, t1, null);
//		assertEquals(t1.getAlternative(),t2);
	}

//	@Test
//	public void constructorTest_TrueCase() {
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		assertEquals(t.getDescription(), description);
//		assertEquals(t.getEstimatedDuration(), estimatedDuration);
//		assertEquals(t.getAcceptableDeviation(), acceptableDeviation);
//
//		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		assertEquals(t2.getDescription(), description);
//		assertEquals(t2.getEstimatedDuration(), estimatedDuration);
//		assertEquals(t2.getAcceptableDeviation(), acceptableDeviation);
//		assertEquals(t2.getDependencies(), dependencies);
//		// test getStatus
//	}
//
//	@Test
//	public void getDependenciesTest_TrueCase() {
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		dependencies.add(t2);
//
//		// test if the dependencies of our task didn't changed
//		assertNotEquals(t.getDependencies(), dependencies);
//	}
//	
//	@Test (expected = NullPointerException.class)
//	public void attachDependantTest_FalseCase_Null(){
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		t.attachDependant(null);
//	}
//
//
//	@Test(expected = NullPointerException.class)
//	public void addTimeSpanTest_FalseCase_StartNull() {
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime endTime = new DateTime(2015, 1, 1, 1, 1);
//		t.addTimeSpan(true, null, endTime);
//	}
//
//	@Test(expected = NullPointerException.class)
//	public void addTimeSpanTest_FalseCase_EndNull() {
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 1, 1);
//		t.addTimeSpan(true, startTime, null);
//	}
//
//	@Test(expected = IllegalDateException.class)
//	public void addTimeSpanTest_FalseCase_Date() {
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 1, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 1, 1);
//		t.addTimeSpan(true, endTime, startTime);
//	}
//
//	@Test(expected = IllegalStateException.class)
//	public void addTimeSpanTest_StatusUNAVAILABLE() {
//
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 10, 1);
//
//		t.addTimeSpan(true, startTime, endTime);
//		assertEquals(t.getStatusName(), "FAILED");
//		ArrayList<Task> dep = new ArrayList<Task>();
//		dep.add(t);
//		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
//				dep, null, null);
//		assertEquals(t2.getStatusName(), "UNAVAILABLE");
//		assertFalse(t2.isAvailable());
//		assertEquals(t2.getStatusName(), "UNAVAILABLE");
//		t2.addTimeSpan(false, startTime, endTime);
//	}
//
//	@Test
//	public void addTimeSpanTest_StatusAVAILABLE() {
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
//		assertEquals(t.getStatusName(), "AVAILABLE");
//		t.addTimeSpan(true, startTime, endTime);
//		assertEquals(t.getStatusName(), "FAILED");
//	}
//	
//	@Test (expected = IllegalStateException.class)
//	public void addTimeSpanTest_StatusFAILED(){
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 10, 1);
//		t.addTimeSpan(true, startTime, endTime);
//		assertEquals(t.getStatusName(),"FAILED");
//		assertEquals(t.getTimeSpan().getStartTime(), startTime);
//		assertEquals(t.getTimeSpan().getEndTime(), endTime);
//		
//		t.addTimeSpan(false, startTime, endTime);
//	}
//	
//	@Test (expected = IllegalStateException.class)
//	public void addTimeSpanTest_StatusFINISHED(){
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 10, 1);
//		t.addTimeSpan(false, startTime, endTime);
//		t.addTimeSpan(false, startTime, endTime);
//	}
//
//	@Test(expected = IllegalDateException.class)
//	public void addTimeSpanTest_FalseCase_StartTime() {
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 6, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 12, 1);
//		t.addTimeSpan(true, startTime, endTime);
//		assertEquals(t.getStatusName(), "FAILED");
//	}
//
//	@Test(expected = IllegalDateException.class)
//	public void addTimeSpanTest_FalseCase_EndTime() {
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 18, 1);
//		assertEquals(t.getStatusName(), "AVAILABLE");
//		t.addTimeSpan(true, startTime, endTime);
//		assertEquals(t.getStatusName(), "FAILED");
//	}
//	
//	@Test
//	public void getTimeSpanTest_TrueCase(){
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 11, 1);
//		t.addTimeSpan(true, startTime, endTime);
//		
//		assertEquals(t.getTimeSpan().getStartTime(), startTime);
//		assertEquals(t.getTimeSpan().getEndTime(), endTime);
//	}
//
//	@Test(expected = NullPointerException.class)
//	public void addAlternativeTest_FalseCase_AlternativeNull() {
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		t.addAlternative(null);
//	}
//	@Test (expected = IllegalStateException.class)
//	public void addAlternativeTest_StatusAVAILABLE(){
//		Task t1 = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		t1.addAlternative(t2);
//		
//	}
//	@Test(expected = IllegalStateException.class)
//	public void addAlternativeTest_StatusUNAVAILABLE() {
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 10, 1);
//
//		t.addTimeSpan(true, startTime, endTime);
//		assertEquals(t.getStatusName(), "FAILED");
//		ArrayList<Task> dep = new ArrayList<Task>();
//		dep.add(t);
//		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
//				dep, null, null);
//		assertEquals(t2.getStatusName(), "UNAVAILABLE");
//		assertFalse(t2.isAvailable());
//		assertEquals(t2.getStatusName(), "UNAVAILABLE");
//		t2.addAlternative(t);
//	}
//
//	@Test
//	public void addAlternativeTest_StatusFAILED() {
//
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 10, 1);
//
//		t.addTimeSpan(true, startTime, endTime);
//		assertEquals(t.getStatusName(), "FAILED");
//		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//
//		t.addAlternative(t2);
//		assertEquals(t.getAlternative(), t2);
//		assertEquals(t.getStatusName(), "FAILED");
//	}
//	
//	@Test (expected = IllegalStateException.class)
//	public void addAlternativeTest_StatusFINISHED(){
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 10, 1);
//
//		t.addTimeSpan(false, startTime, endTime);
//		assertEquals(t.getStatusName(), "FINISHED");
//		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//
//		t.addAlternative(t2);
//	}
//
//	@Test
//	public void isAvailableTest_StatusAVAILABLE() {
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		assertTrue(t.isAvailable());
//		assertEquals(t.getStatusName(), "AVAILABLE");
//
//	}
//
//	@Test
//	public void isAvailableTest_StatusUNAVAILABLE() {
//
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 10, 1);
//
//		t.addTimeSpan(true, startTime, endTime);
//		assertEquals(t.getStatusName(), "FAILED");
//		ArrayList<Task> dep = new ArrayList<Task>();
//		dep.add(t);
//		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
//				dep, null, null);
//		assertEquals(t2.getStatusName(), "UNAVAILABLE");
//		assertFalse(t2.isAvailable());
//		assertEquals(t2.getStatusName(), "UNAVAILABLE");
//	}
//
//	@Test
//	public void isAvailableTest_StatusFINISHED() {
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 10, 1);
//		t.addTimeSpan(false, startTime, endTime);
//		assertFalse(t.isAvailable());
//		assertEquals(t.getStatusName(), "FINISHED");
//	}
//
//	@Test
//	public void isAvailableTest_StatusFAILED() {
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 10, 1);
//		t.addTimeSpan(true, startTime, endTime);
//		assertFalse(t.isAvailable());
//		assertEquals(t.getStatusName(), "FAILED");
//	}
//
//	@Test
//	public void isFinishedTest_StatusAVAILABLE() {
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		assertFalse(t.isFinished());
//		assertEquals(t.getStatusName(), "AVAILABLE");
//
//	}
//
//	@Test
//	public void isFinishedTest_StatusUNAVAILABLE() {
//
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 10, 1);
//
//		t.addTimeSpan(true, startTime, endTime);
//		assertEquals(t.getStatusName(), "FAILED");
//		ArrayList<Task> dep = new ArrayList<Task>();
//		dep.add(t);
//		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
//				dep, null, null);
//
//		assertFalse(t2.isFinished());
//		assertEquals(t2.getStatusName(), "UNAVAILABLE");
//	}
//
//	@Test
//	public void isFinishedTest_StatusFINISHED() {
//
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 10, 1);
//
//		t.addTimeSpan(false, startTime, endTime);
//
//		assertTrue(t.isFinished());
//		assertEquals(t.getStatusName(), "FINISHED");
//	}
//
//	@Test
//	public void isFinishedTest_StatusFAILED() {
//
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 10, 1);
//
//		t.addTimeSpan(true, startTime, endTime);
//		assertFalse(t.isFinished());
//		assertEquals(t.getStatusName(), "FAILED");
//	}
//
//	@Test
//	public void isFailedTest_StatusAVAILABLE() {
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//
//		assertFalse(t.isFailed());
//		assertEquals(t.getStatusName(), "AVAILABLE");
//
//	}
//
//	@Test
//	public void isFailedTest_StatusUNAVAILABLE() {
//
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 10, 1);
//
//		t.addTimeSpan(true, startTime, endTime);
//		assertEquals(t.getStatusName(), "FAILED");
//		ArrayList<Task> dep = new ArrayList<Task>();
//		dep.add(t);
//		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
//				dep, null, null);
//
//		assertFalse(t2.isFailed());
//		assertEquals(t2.getStatusName(), "UNAVAILABLE");
//	}
//
//	@Test
//	public void isFailedTest_StatusFINISHED() {
//
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 10, 1);
//
//		t.addTimeSpan(false, startTime, endTime);
//		assertFalse(t.isFailed());
//
//	}
//
//	@Test
//	public void isFailedTest_StatusFAILED() {
//
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 10, 1);
//
//		t.addTimeSpan(true, startTime, endTime);
//
//		assertTrue(t.isFailed());
//		assertEquals(t.getStatusName(), "FAILED");
//	}
//
//
//	@Test 
//	public void isCompletedTest_StatusAVAILABLE(){
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		assertFalse(t.isCompleted());
//	}
//	
//	@Test
//	public void isCompletedTest_StatusUNAVAILABLE(){
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 10, 1);
//
//		t.addTimeSpan(true, startTime, endTime);
//		assertEquals(t.getStatusName(), "FAILED");
//		ArrayList<Task> dep = new ArrayList<Task>();
//		dep.add(t);
//		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
//				dep, null, null);
//		assertFalse(t2.isCompleted());
//	}
//	
//	@Test
//	public void isCompletedTest_StatusFAILED(){
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 10, 1);
//
//		t.addTimeSpan(true, startTime, endTime);
//		assertTrue(t.isCompleted());
//	}
//	
//	@Test
//	public void isCompletedTest_StatusFINISHED(){
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 10, 1);
//
//		t.addTimeSpan(false, startTime, endTime);
//		assertTrue(t.isCompleted());
//	}
//
//	@Test(expected = IllegalStateException.class)
//	public void updateTaskAvailabilityTest_TrueCase_StatusAVAILABLE() {
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		t.updateTaskAvailability();
//		assertEquals(t.getStatusName(), "AVAILABLE");
//		t.updateTaskAvailability();
//	}
//
//	@Test(expected = IllegalStateException.class)
//	public void updateTaskAvailabilityTest_TrueCase_StatusFINISHED() {
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 10, 1);
//
//		t.addTimeSpan(false, startTime, endTime);
//		t.updateTaskAvailability();
//		assertEquals(t.getStatusName(), "FINISHED");
//	}
//
//	@Test(expected = IllegalStateException.class)
//	public void updateTaskAvailabilityTest_FalseCase_StatusFAILED() {
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 10, 1);
//
//		t.addTimeSpan(true, startTime, endTime);
//		assertEquals(t.getStatusName(), "FAILED");
//		t.updateTaskAvailability();
//
//	}
//
//	@Test
//	public void calculateTotalExecutionTimeTest_TrueCase() {
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 14, 0);
//		DateTime endTime = new DateTime(2015, 1, 1, 16, 0);
//		t.addTimeSpan(true, startTime, endTime);
//		assertEquals(t.getTotalExecutionTime(), 2 * 60);
//	}
//	
//	@Test (expected = IllegalStateException.class)
//	public void calculateTotalExecutionTimeTest_StatusUNAVAILABLE(){
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 1);
//		DateTime endTime = new DateTime(2016, 1, 1, 10, 1);
//
//		t.addTimeSpan(true, startTime, endTime);
//		assertEquals(t.getStatusName(), "FAILED");
//		ArrayList<Task> dep = new ArrayList<Task>();
//		dep.add(t);
//		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
//				dep, null, null);
//		assertEquals(t2.getStatusName(), "UNAVAILABLE");
//		assertFalse(t2.isAvailable());
//		assertEquals(t2.getStatusName(), "UNAVAILABLE");
//		int time = t2.getTotalExecutionTime();
//	}
//	@Test
//	public void calculateTotalExecutionTimeTest_StatusFAILED(){
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 14, 0);
//		DateTime endTime = new DateTime(2015, 1, 1, 16, 0);
//		t.addTimeSpan(true, startTime, endTime);
//		assertEquals(t.getTotalExecutionTime(), 2 * 60);
//	}
//	
//	@Test
//	public void calculateTotalExecutionTimeTest_StatusFINISHEED(){
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 14, 0);
//		DateTime endTime = new DateTime(2015, 1, 1, 16, 0);
//		t.addTimeSpan(false, startTime, endTime);
//		assertEquals(t.getStatusName(),"FINISHED");
//		int time = t.getTotalExecutionTime();
//		assertEquals(time, 2 * 60);
//	}
//
//	@Test(expected = IllegalStateException.class)
//	public void calculateTotalExecutionTimeTest_TrueCase_NoTimeSpan() {
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		int exeTime = t.getTotalExecutionTime();
//	}
//
//	@Test
//	public void calculateTotalExecutionTimeTest_TrueCase_Alternative() {
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		Task t2 = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		Task t3 = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 0);
//		DateTime endTime = new DateTime(2015, 1, 1, 11, 0);
//		assertEquals(t.getStatusName(), "AVAILABLE");
//		t.addTimeSpan(true, startTime, endTime);
//		t.addAlternative(t2);
//		assertEquals(t.getTotalExecutionTime(), 60);
//
//		t2.addTimeSpan(true, startTime, endTime);
//		assertEquals(t.getTotalExecutionTime(), 120);
//		t2.addAlternative(t3);
//
//		t3.addTimeSpan(false, startTime, endTime);
//		assertEquals(t.getTotalExecutionTime(), 180);
//		assertEquals(t3.getTotalExecutionTime(), 60);
//
//	}
//
//	@Test(expected = IllegalStateException.class)
//	public void calculateOverduePercentageTest_TrueCase_NoTimeSpan() {
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		int overduePerc = t.getOverduePercentage();
//	}
//
//	@Test
//	public void calculateOverduePercentageTest_TrueCase() {
//		Task t = new Task(description, estimatedDuration, acceptableDeviation,
//				dependencies, null, null);
//		DateTime startTime = new DateTime(2015, 1, 1, 10, 0);
//		DateTime endTime = new DateTime(2015, 1, 1, 11, 0);
//		t.addTimeSpan(true, startTime, endTime);
//		assertEquals(t.getOverduePercentage(), (60 - this.estimatedDuration) *100
//				/ this.estimatedDuration);
//
//	}

}
