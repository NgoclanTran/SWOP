package taskman.model.user;

public interface User {
	/**
	 * Will return the name of the user
	 * @return
	 * 			Returns the name of the user
	 */
	public String getName();
	/**
	 * Will return whether the user is a developer or not
	 * @return
	 * 			Returns a boolean indicating whether the user is a developer or not
	 */
	public boolean isDeveloper();
	/**
	 * Will return whether the user is a project manager or not
	 * @return
	 * 			Returns a boolean indicating whether the user is a project manager or not
	 */
	public boolean isProjectManager();

}
