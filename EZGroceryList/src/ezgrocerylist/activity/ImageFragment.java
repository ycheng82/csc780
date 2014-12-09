package ezgrocerylist.activity;

import android.support.v4.app.Fragment;

/**
 * ingredient fragment to show all recipe images
 * use gallery view here
 * @author Ye
 *
 */

public class ImageFragment extends Fragment {
	private String recipeName;

	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}
}
