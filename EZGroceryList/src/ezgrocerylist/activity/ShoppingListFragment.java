package ezgrocerylist.activity;

import java.util.ArrayList;
import java.util.HashSet;

import com.ezgrocerylist.R;

import ezgrocerylist.activity.PantryListFragment.OnAllListSelectedListener;
import ezgrocerylist.sql.DatabaseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ShoppingListFragment extends ListFragment {
	private HashSet<String> listNames;
	private Activity activity;
	private ArrayList<String> shoppingList;
	private ArrayAdapter<String> adapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		DatabaseHandler db = new DatabaseHandler(getActivity());
		shoppingList = db.getShoppingLists();
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, shoppingList);
		setListAdapter(adapter);
		registerForContextMenu(this.getListView());
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String listName = (String) getListAdapter().getItem(position);
		Fragment fragment = new ShoppingListDetailFragment();
		((ShoppingListDetailFragment) fragment).setListName(listName);
		try {
			((OnAllListSelectedListener) activity).onListPicked(listName);
		} catch (ClassCastException cce) {

		}
		
		// ((AllListFragment)
		// fragment).setListContents(getListContents());
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager
				.beginTransaction();
		transaction.replace(
				((ViewGroup) getView().getParent()).getId(),
				fragment);
		transaction.addToBackStack(fragment.getClass().getName());
		transaction.commit();
	}

	public HashSet<String> getListNames() {
		return listNames;
	}

	public void setListNames(HashSet<String> listNames) {
		this.listNames = listNames;
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}
	public interface OnAllListSelectedListener {
		public void onListPicked(String listName);
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = this.getActivity().getMenuInflater();
	    inflater.inflate(R.menu.pantry_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
	    final String shoppingListName = (String) getListAdapter().getItem(info.position);
	    switch (item.getItemId()) {

	        case R.id.pantry_context_edit:
	            // edit recipe
	    		Fragment fragment = new ShoppingListDetailFragment();
	    		((ShoppingListDetailFragment) fragment).setListName(shoppingListName);
	    		try {
	    			((OnAllListSelectedListener) activity).onListPicked(shoppingListName);
	    		} catch (ClassCastException cce) {

	    		}
	    		
	    		// ((AllListFragment)
	    		// fragment).setListContents(getListContents());
	    		FragmentManager fragmentManager = getFragmentManager();
	    		FragmentTransaction transaction = fragmentManager
	    				.beginTransaction();
	    		transaction.replace(
	    				((ViewGroup) getView().getParent()).getId(),
	    				fragment);
	    		transaction.addToBackStack(fragment.getClass().getName());
	    		transaction.commit();
	            return true;
	        case R.id.pantry_context_rename:
	            // rename recipe
	    		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
	    		builder.setTitle("Change the name for the shopping list "+ shoppingListName);

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
	    				int i = db.changeShoppingListName(shoppingListName, newListName);
	    				refreshFragment();
	    				Toast.makeText(((Dialog) dialog).getContext(),
	    						i + " recodes in " + shoppingListName + " have been changed.",
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
	        case R.id.pantry_context_delete:
	            // delete recipe
	    		AlertDialog.Builder builder2 = new AlertDialog.Builder(this.getActivity());
	    		builder2.setTitle("You are going to delete the shopping list " + shoppingListName + ". Are you sure?");

	    		// Set up the buttons
	    		builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    			@Override
	    			public void onClick(DialogInterface dialog, int which) {
	    				
	    				if (deleteShopping(shoppingListName)){
	    					refreshFragment();
	    					Toast.makeText(((Dialog) dialog).getContext(),"Shopping "+ shoppingListName + " has been deleted.",
	    						Toast.LENGTH_SHORT).show();
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
	/**
	 * refresh the fragment
	 */
	public void refreshFragment(){
		DatabaseHandler db = new DatabaseHandler(getActivity());
		ArrayList<String> tempList = db.getShoppingLists();
		shoppingList.clear();
		shoppingList.addAll(tempList);
		
		adapter.notifyDataSetChanged();
	}
	@Override
	public void onResume() {

	    super.onResume();
	    refreshFragment();

	}
	public boolean deleteShopping(String listName){
		DatabaseHandler db = new DatabaseHandler(this.getActivity());
		if (db.removeShoppingList(listName)>0){
			return true;
		}
		return false;
	}
	
}
