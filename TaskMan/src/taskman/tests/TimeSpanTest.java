package taskman.tests;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.project.task.TimeSpan;

public class TimeSpanTest {
	private DateTime start;
	private DateTime end;

@Before
public void setup(){
	start = new DateTime(2015,1,1,1,1);
	end = new DateTime(2016,1,1,1,1);
}

@Test
public void constructorTest_TrueCase(){
	TimeSpan ts = new TimeSpan(start, end);
	assertEquals(ts.getStartTime(), start);
	assertEquals(ts.getEndTime(), end);
}

}
