package taskman.view;

import org.joda.time.DateTime;

public interface ICreateProjectForm {

	public String getNewProjectName();

	public String getNewProjectDescription();

	public DateTime getNewProjectDueTime();

}
