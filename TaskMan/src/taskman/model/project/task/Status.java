package taskman.model.project.task;

import org.joda.time.DateTime;

public interface Status{

public String getName();
public void addAlternative(Task task, Task alternative);
public boolean isAvailable();
public boolean isFinished();
public boolean isFailed();
public void updateTaskAvailability(Task task);
public int calculateTotalExecutedTime(Task task);
public int calculateOverDuePercentage(Task task);
public void addTimeSpan(Task task, boolean failed, DateTime startTime, DateTime endTime);

}
