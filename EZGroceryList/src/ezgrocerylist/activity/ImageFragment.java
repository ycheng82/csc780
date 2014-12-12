package ezgrocerylist.activity;

import com.ezgrocerylist.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * ingredient fragment to show all recipe images
 * use gallery view here
 * @author Ye
 *
 */

public class ImageFragment extends Fragment {
	private String recipeName;
	private View rootView;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recipe_image, container, false);
        return rootView;
    }

	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}
}
