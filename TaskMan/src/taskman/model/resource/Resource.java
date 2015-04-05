package taskman.model.resource;

public class Resource {

	String name;

	/**
	 * Creates a new resource with the given name.
	 * 
	 * @param name
	 * 
	 * @throws IllegalArgumentException
	 */
	public Resource(String name) {
		if (name == null)
			throw new IllegalArgumentException("Name can not be null.");
		this.name = name;
	}

	/**
	 * Returns the name of the resource.
	 * 
	 * @return Returns the name of the resource.
	 */
	public String getName() {
		return this.name;
	}

}
