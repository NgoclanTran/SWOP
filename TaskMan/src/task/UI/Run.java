package task.UI;

import java.util.Arrays;
import java.util.List;

public class Run {

	public static void main(String[] args) {
		UI ui = new UI();
		List<String> subOptions = Arrays.asList("SubItem 1", "SubItem 2");
		List<Object> options = Arrays.asList("Item 1", subOptions, "Item 3", "Item 4");
		ui.displayList(options);
	}

}
