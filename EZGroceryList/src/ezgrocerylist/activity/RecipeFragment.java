package ezgrocerylist.activity;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.app.Dialog;
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
		registerForContextMenu(this.getListView());
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
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);

	    MenuInflater inflater = this.getActivity().getMenuInflater();
	    inflater.inflate(R.menu.recipe_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
	    final String recipeName = (String) getListAdapter().getItem(info.position);
	    switch (item.getItemId()) {

	        case R.id.recipe_context_edit:
	            // edit recipe
	        	
	    		//Intent intent = new Intent(this.getActivity(),RecipeEditActivity.class);
	    		Intent intent = new Intent(this.getActivity(),EditRecipeActivity.class);
	    		intent.putExtra("recipename", recipeName);
	    		startActivity(intent);
	            return true;
	        case R.id.recipe_context_rename:
	            // rename recipe
	    		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
	    		builder.setTitle("Change the name for the  recipe "+ recipeName);

	    		// Set up the input
	    		final EditText input = new EditText(this.getActivity());
	    		// Specify the type of input expected; this, for example, sets the input
	    		// as a password, and will mask the text
	    		input.setInputType(InputType.TYPE_CLASS_TEXT);
	    		builder.setView(input);

	    		// Set up the buttons
	    		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    			@Override
	    			public void onClick(DialogInterface dialog, int which) {
	    				String newListName = input.getText().toString();
	    				// update records in database
	    				DatabaseHandler db = new DatabaseHandler(((Dialog) dialog).getContext());
	    				int i = db.changeRecipeName(recipeName, newListName);
	    				Toast.makeText(((Dialog) dialog).getContext(),
	    						i + " recodes in " + recipeName + " have been changed.",
	    						Toast.LENGTH_SHORT).show();
	    			}
	    		});
	    		builder.setNegativeButton("Cancel",
	    				new DialogInterface.OnClickListener() {
	    					@Override
	    					public void onClick(DialogInterface dialog, int which) {
	    						dialog.cancel();
	    					}
	    				});

	    		builder.show();

	            return true;
	        case R.id.recipe_context_delete:
	            // delete recipe
	    		AlertDialog.Builder builder2 = new AlertDialog.Builder(this.getActivity());
	    		builder2.setTitle("You are going to delete the recipe " + recipeName + ". Are you sure?");

	    		// Set up the buttons
	    		builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    			@Override
	    			public void onClick(DialogInterface dialog, int which) {
	    				if (deleteRecipe(recipeName)){;
	    					Toast.makeText(((Dialog) dialog).getContext(),"Recipe "+ recipeName + " has been deleted.",
	    						Toast.LENGTH_LONG).show();
	    				}

	    			}
	    		});
	    		builder2.setNegativeButton("Cancel",
	    				new DialogInterface.OnClickListener() {
	    					@Override
	    					public void onClick(DialogInterface dialog, int which) {
	    						dialog.cancel();
	    					}
	    				});
	    		builder2.show();

	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	public boolean deleteRecipe(String recipeName){
		DatabaseHandler db = new DatabaseHandler(this.getActivity());
		if (db.deleteRecipe(recipeName)){
			return true;
		}
		return false;
	}
}