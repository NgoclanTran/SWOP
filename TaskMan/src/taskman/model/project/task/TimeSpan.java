package taskman.model.project.task;

import org.joda.time.DateTime;

public class TimeSpan {

public TimeSpan(DateTime startTime, DateTime endTime){
	if(startTime.compareTo(endTime) < 0) throw new IllegalArgumentException("The end time cannot be alter than de start time.");
	
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
public int calculatePerformedTime(){
	return 0;
}
public int calculateDelay(int estimatedDuration){
	return 0;
}

public boolean isBefore(TimeSpan timeSpan) {
	// TODO Auto-generated method stub
	return false;
}
}
