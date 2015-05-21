package taskman.model.user;

public class ProjectManager implements User {
	
	public ProjectManager(String name) {
		if (name == null)
			throw new IllegalArgumentException("Name can not be null.");
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
	
	private final String name;
	
	@Override
	public boolean isDeveloper() {
		return false;
	}
	
	@Override
	public boolean isProjectManager() {
		return true;
	}

}
