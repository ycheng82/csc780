package ezgrocerylist.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.ezgrocerylist.R;
import ezgrocerylist.activity.ListViewAdapter;
import ezgrocerylist.activity.DetailFragment.ExpandableListAdapter;
import ezgrocerylist.sql.DatabaseHandler;
import ezgrocerylist.sql.Item;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout.LayoutParams;

/**
 * fragment to show all items in a given pantry list
 * 
 * @author Ye
 * 
 */
public class DetailListFragment extends Fragment {
	private ExpandableListView expandableList;
	private Button showBtn, shoppingBtn;
	private ListViewAdapter adapter;
	private List<GroupItem> dataList = new ArrayList<GroupItem>();
	private View rootView;

	private String listName;
	private HashSet<String> listCategory;
	private String[][] listContents;
	private String[] groups;
	private Item[] children;

	protected final int ITEM_EDIT = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_detail_list, container,
				false);

		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		expandableList = (ExpandableListView) getView().findViewById(
				R.id.expandable_list);
		showBtn = (Button) getView().findViewById(R.id.showBtn);

		showBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				editCheckedItem();
			}
		});

		shoppingBtn = (Button) getView().findViewById(R.id.shoppingBtn);

		shoppingBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				shopItems();
			}
		});

		initData();

		adapter = new ListViewAdapter(getActivity().getApplicationContext(),
				dataList);
		expandableList.setAdapter(adapter);

		expandableList
				.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

					@Override
					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPosition, int childPosition,
							long id) {
						Log.e("children", "clicked");
						Intent intent = new Intent(v.getContext(),
								ItemActivity.class);

						intent.putExtra("listname", listName);
						String itemName = adapter
								.getChild(groupPosition, childPosition)
								.toString().split("	")[0];
						String itemQuantity = adapter
								.getChild(groupPosition, childPosition)
								.toString().split("	")[1];
						String itemUnit = adapter
								.getChild(groupPosition, childPosition)
								.toString().split("	")[2];
						intent.putExtra("itemname", itemName);
						intent.putExtra("itemquantity", itemQuantity);
						intent.putExtra("itemunit", itemUnit);
						

						startActivityForResult(intent, ITEM_EDIT);
						return true;
					}
				});
	}

	/**
	 * initiate data for list view
	 */
	private void initData() {
		DatabaseHandler db = new DatabaseHandler(this.getActivity());
		groups = db.getListCategory(listName);

		// children = db.getListContents(listName);
		for (int i = 0; i < groups.length; i++) {
			List<Item> list = db.getCategoryContents(listName, groups[i]);
			children = list.toArray(new Item[list.size()]);
			List<ChildrenItem> childList = new ArrayList<ChildrenItem>();
			for (int j = 0; j < children.length; j++) {

				childList.add(new ChildrenItem(children[j].getItemName()
						+ "			" + children[j].getItemQuantity() + "			"
						+ children[j].getItemUnit(), children[j].getItemName()
						+ "			" + children[j].getItemQuantity() + "			"
						+ children[j].getItemUnit()));
			}
			GroupItem groupItem = new GroupItem(groups[i], groups[i], childList);
			dataList.add(groupItem);

		}

	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public HashSet<String> getListCategory() {
		return listCategory;
	}

	public void setListCategory(HashSet<String> listCategory) {
		this.listCategory = listCategory;
	}

	public String[][] getListContents() {
		return listContents;
	}

	public void setListContents(String[][] listContents) {
		this.listContents = listContents;
	}

	private void editCheckedItem() {
		List<String> checkedChildren = adapter.getCheckedChildren();
		if (checkedChildren != null && !checkedChildren.isEmpty()) {
			if (checkedChildren.size() > 1) {
				Toast.makeText(getActivity(), "Select one item to edit!",
						Toast.LENGTH_LONG).show();
			} else {
				Intent intent = new Intent(getActivity(), ItemActivity.class);

				intent.putExtra("listname", listName);
				String itemName = checkedChildren.get(0).split("	")[0];
				String itemQuantity = checkedChildren.get(0).split("	")[1];
				String itemUnit = checkedChildren.get(0).split("	")[2];
				intent.putExtra("itemname", itemName);
				intent.putExtra("itemquantity", itemQuantity);
				intent.putExtra("itemunit", itemUnit);
				intent.putExtra("type","Pantry");

				startActivity(intent);
			}
		}
	}

	/**
	 * add checked items to a shopping list
	 */
	private void shopItems() {
		final List<String> checkedChildren = adapter.getCheckedChildren();
		if (checkedChildren != null && !checkedChildren.isEmpty()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Type the name for your shopping list");

			// Set up the input
			final EditText input = new EditText(getActivity());
			// Specify the type of input expected; this, for example, sets the input
			// as a password, and will mask the text
			input.setInputType(InputType.TYPE_CLASS_TEXT);
			builder.setView(input);

			// Set up the buttons
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					addShopping(input.getText().toString(),checkedChildren);

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
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ITEM_EDIT) {
			// Refresh the fragment here

		}
	}
	public void addShopping(String shoppingListName,List<String> shoppingItems){
		ArrayList<Item> items = new ArrayList<Item>();
		DatabaseHandler db = new DatabaseHandler(getActivity());
		for (int i = 0;i<shoppingItems.size();i++){
			Item item = db.getItem(listName,shoppingItems.get(i).split("	")[0]);
			items.add(item);
		}
		//call db to add to shopping table
		if (db.addShoppingItems(shoppingListName,items)){
			Toast.makeText(getActivity(), "Selected items added to shopping list "+shoppingListName,
					Toast.LENGTH_LONG).show();
		}
	}

}
