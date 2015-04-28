package taskman.model.user;

public class Developer {

	String name;

	public Developer(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
