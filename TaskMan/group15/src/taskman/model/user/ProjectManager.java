package taskman.model.user;

public class ProjectManager implements User {
	/**
	 * The constructor for a project manager
	 * @param name
	 * 			The name of the project manager to be created
	 */
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

	@Override
	/**
	 * Will return the name of the project manager
	 * @returns the name of the project manager
	 */
	public String toString() {
		return name;
	}

}
