package taskman.view;

import org.joda.time.DateTime;

public interface ICreateProjectForm extends IView {

	public String getNewProjectName();

	public String getNewProjectDescription();

	public DateTime getNewProjectDueTime();

}
