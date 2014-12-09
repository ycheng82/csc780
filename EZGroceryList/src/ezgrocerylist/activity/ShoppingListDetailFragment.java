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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
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
public class ShoppingListDetailFragment extends Fragment {
	private ExpandableListView expandableList;
	//private Button showBtn;
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
		rootView = inflater.inflate(R.layout.fragment_detail_shopping_list, container,
				false);

		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		expandableList = (ExpandableListView) getView().findViewById(
				R.id.expandable_list);
		/*showBtn = (Button) getView().findViewById(R.id.showBtn);

		showBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				editCheckedItem();
			}
		});*/
		
		registerForContextMenu(expandableList);

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
		groups = db.getShoppingListCategory(listName);

		// children = db.getListContents(listName);
		for (int i = 0; i < groups.length; i++) {
			List<Item> list = db.getShoppingCategoryContents(listName, groups[i]);
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
				String itemName = checkedChildren.get(0).split("			")[0];
				String itemQuantity = checkedChildren.get(0).split("			")[1];
				String itemUnit = checkedChildren.get(0).split("			")[2];
				intent.putExtra("itemname", itemName);
				intent.putExtra("itemquantity", itemQuantity);
				intent.putExtra("itemunit", itemUnit);
				intent.putExtra("type", "shopping");

				startActivity(intent);
			}
		}
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ITEM_EDIT) {
			// Refresh the fragment here

		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;

		int type = ExpandableListView
				.getPackedPositionType(info.packedPosition);
		int groupPosition = ExpandableListView
				.getPackedPositionGroup(info.packedPosition);
		int childPosition = ExpandableListView
				.getPackedPositionChild(info.packedPosition);

		// Show context menu for groups
		if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
			menu.setHeaderTitle(((GroupItem) adapter.getGroup(groupPosition))
					.getId());
			// menu.add(0, 0, 1, "Edit");
			menu.add(0, 0, 1, "Delete");
			//menu.add(0, 1, 2, "Add to Shopping");

			// Show context menu for children
		} else if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
			menu.setHeaderTitle(((ChildrenItem) adapter.getChild(groupPosition,
					childPosition)).getId().split("	")[0]);
			menu.add(0, 0, 1, "Edit");
			menu.add(0, 1, 2, "Delete");
			//menu.add(0, 2, 3, "Add to Shopping");
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item
				.getMenuInfo();

		int type = ExpandableListView
				.getPackedPositionType(info.packedPosition);
		int groupPosition = ExpandableListView
				.getPackedPositionGroup(info.packedPosition);
		int childPosition = ExpandableListView
				.getPackedPositionChild(info.packedPosition);

		if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
			// do something with parent
			switch (item.getItemId()) {
			case 0:
				Toast.makeText(this.getActivity(), "delete", Toast.LENGTH_SHORT)
						.show();
				// delete group
				delteGroupDialog(listName,((GroupItem) adapter.getGroup(groupPosition))
						.getId());
				break;
			}

		} else if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
			// do someting with child
			switch (item.getItemId()) {
			case 0:
				Toast.makeText(this.getActivity(), "edit", Toast.LENGTH_SHORT)
						.show();
				// edit item
				editItem(((ChildrenItem) adapter.getChild(groupPosition,
						childPosition)).getId());
				break;
			case 1:
				Toast.makeText(this.getActivity(), "delete", Toast.LENGTH_SHORT)
						.show();
				// delte item
				delteItemDialog(((ChildrenItem) adapter.getChild(groupPosition,
						childPosition)).getId());
				break;
			}
		}

		return super.onContextItemSelected(item);
	}
	
	/**
	 * delete items in a category of a given shopping list
	 * @param shoppingListName
	 * @param catName
	 */
	private void delteGroupDialog(String shoppingListName,String catName) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				this.getActivity());
		final String listName = shoppingListName;
		final String groupName = catName;
		builder.setTitle("You are going to delete " + groupName
				+ ". Are you sure?");

		// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				deleteItems(listName, groupName);
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
	
	/**
	 * delete items in a category in a shopping list
	 * @param shoppingListName
	 * @param groupName
	 */
	protected void deleteItems(String shoppingListName, String groupName) {
		DatabaseHandler db = new DatabaseHandler(getActivity());
		// call db to add to shopping table
		if (db.delteShoppingItems(shoppingListName,groupName)>0) {
			Toast.makeText(
					getActivity(),db.delteShoppingItems(shoppingListName,groupName)+
					" items in " + listName + " have been deleted",
					Toast.LENGTH_SHORT).show();
		}
		
	}
	
	/**
	 * edit an item in a shopping list
	 * @param item
	 */
	private void editItem(String item) {
		Intent intent = new Intent(getActivity(), ItemActivity.class);

		intent.putExtra("listname", listName);
		String itemName = item.split("	")[0];
		String itemQuantity = item.split("	")[1];
		String itemUnit = item.split("	")[2];
		intent.putExtra("itemname", itemName);
		intent.putExtra("itemquantity", itemQuantity);
		intent.putExtra("itemunit", itemUnit);
		intent.putExtra("type", "shopping");

		startActivity(intent);
	}
	
	/**
	 * delete one item from a shopping list
	 */
	private void delteItemDialog(String item) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				this.getActivity());
		final String itemName = item.split("	")[0];
		builder.setTitle("You are going to delete " + itemName
				+ ". Are you sure?");

		// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				deleteItem(listName, itemName);
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
	
	/**
	 * delet an item from a shopping list
	 * @param listName
	 * @param itemName
	 */
	public void deleteItem(String listName, String itemName) {
		DatabaseHandler db = new DatabaseHandler(this.getActivity());
		if (db.deleteShoppingItem(listName, itemName)) {
			Toast.makeText(this.getActivity(),
					itemName + " in " + listName + " has been deleted.",
					Toast.LENGTH_SHORT).show();
		}
	}


}

