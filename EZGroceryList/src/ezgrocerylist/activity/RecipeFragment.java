package ezgrocerylist.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.ListFragment;

import com.ezgrocerylist.R;

import ezgrocerylist.sql.DatabaseHandler;

public class RecipeFragment extends ListFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		DatabaseHandler db = new DatabaseHandler(getActivity());
		ArrayList<String> recipeList = db.getRecipeList();
		String[] values = new String[recipeList.size()];
		values = recipeList.toArray(values);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		//call recipe editing 
		String listName = (String) getListAdapter().getItem(position);
		//Intent intent = new Intent(this.getActivity(),RecipeEditActivity.class);
		Intent intent = new Intent(this.getActivity(),EditRecipeActivity.class);
		intent.putExtra("recipename", listName);
		startActivity(intent);
	}
}