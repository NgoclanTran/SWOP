package taskman.model.project.task;

import org.joda.time.DateTime;

public class TimeSpan {

public TimeSpan(DateTime startTime, DateTime endTime){
	this.startTime = startTime;
	this.endTime = endTime;
	
}
private DateTime startTime;
public DateTime getStartTime(){
	return this.startTime;
}

private DateTime endTime;
public DateTime getEndTime(){
	return this.endTime;
}
}
