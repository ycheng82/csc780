package ezgrocerylist.activity;

import java.util.ArrayList;
import java.util.HashSet;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ShoppingListFragment extends ListFragment {
	private HashSet<String> listNames;
	private Activity activity;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ArrayList<String> newList = new ArrayList<String>(listNames);
		String[] values = new String[newList.size()];
		values = newList.toArray(values);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
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

}
