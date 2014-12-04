package ezgrocerylist.activity;

import java.util.ArrayList;
import java.util.HashSet;

import com.ezgrocerylist.R;

import ezgrocerylist.activity.ShoppingListFragment.OnAllListSelectedListener;
import ezgrocerylist.slidingmenu.adapter.NavDrawerListAdapter;
import ezgrocerylist.slidingmenu.model.NavDrawerItem;
import ezgrocerylist.sql.DatabaseHandler;
import ezgrocerylist.sql.Item;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ShoppingActivity extends ActionBarActivity implements
		OnAllListSelectedListener {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	String listName, listContents;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	// private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pantry);
		
		listContents="";

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		// navMenuIcons =
		// getResources().obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0]));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1]));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2]));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3]));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4]));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5]));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6]));

		// Recycle the typed array
		// navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.pantry_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_bar_code:
			// actionName.setText("bar code scanning");
			// call barcode scanner screen for an item
			Intent barcodeIntent = new Intent(this, BarcodeActivity.class);
			barcodeIntent.putExtra("listname", listName);
			startActivity(barcodeIntent);
			return true;
		case R.id.action_text:
			// actionName.setText("text input");
			// call item input screen for an item
			Intent intent = new Intent(this, ItemActivity.class);

			intent.putExtra("listname", listName);
			intent.putExtra("type","shopping");
			startActivityForResult(intent, 0);
			return true;
		case R.id.action_email:
			// actionName.setText("share by email");
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					"I want to share this list with you");

			emailIntent.setType("plain/text");
			DatabaseHandler db = new DatabaseHandler(this);
			ArrayList<Item> items = db.getShoppingItems(listName);
			
			for (int i = 0; i < items.size();i++){
				listContents += items.get(i).getItemName() + "			";
				listContents += items.get(i).getItemQuantity() + "			";
				listContents += items.get(i).getItemUnit() + "\n";
			}
			String emailBody = listContents;
			// (TextView) findViewById(R.id.pantry_action_name)
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailBody);

			startActivity(emailIntent);

			return true;
		case R.id.action_voice:
			// actionName.setText("voice input");
			Intent voiceIntent = new Intent(this, VoiceActivity.class);
			voiceIntent.putExtra("listname", listName);
			startActivity(voiceIntent);
			return true;
		case R.id.action_settings:
			// actionName.setText("setting");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/***
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			// add icon?
			// help information?
			fragment = new HomeFragment("Pantry");
			break;
		case 1:
			// open list, need fragment replacement
			/*
			 * if (getAllLists() != null) { fragment = new ListFragment();
			 * ((ListFragment) fragment).setListNames(getAllLists());
			 * 
			 * ((ListFragment) fragment).setListContents(getListContents()); }
			 * else{ fragment = new HomeFragment("Pantry"); }
			 */
			if (getAllLists() != null) {
				fragment = new ShoppingListFragment();
				((ShoppingListFragment) fragment).setListNames(getAllLists());

				//((AllListFragment) fragment).setListContents(getListContents());
			} else {
				fragment = new HomeFragment("Pantry");
			}

			// ((ListFragment) fragment).setListContents(getListContents());
			// fragment = new NameFragment();
			// ((NameFragment) fragment).setListNames(getAllLists());
			// ((NameFragment) fragment).setListContents(getListContents());
			break;
		case 2:
			// new list, no fragment replacement needed
			newList();
			break;
		case 3:
			// change list name, no fragment replacement needed
			changeListName();
			break;
		case 4:
			// category

			break;
		case 5:
			// remove
			removeList();
			break;
		case 6:
			// add to shopping

			break;
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private HashSet<String> getAllLists() {
		// TODO Auto-generated method stub
		DatabaseHandler db = new DatabaseHandler(this);
		ArrayList<String> listNames = new ArrayList<String>();
		listNames = db.getShoppingLists();
		// get unique list names
		HashSet<String> uCats = null;
		if (listNames.size() != 0) {
			uCats = new HashSet<>(listNames);
			// Log.d("showAllList","#grocery lists: " + listNames.size());
		}
		return uCats;

	}


	public void setListName(String listName) {
		this.listName = listName;

	}

	public String getListName() {
		return this.listName;

	}

	/**
	 * create a new list, pop up an alert dialog to input list name, start
	 * activity to add the first item
	 */
	public void newList() {
		// LinearLayout childLayout = (LinearLayout)
		// findViewById(R.id.layoutChild);
		// childLayout.removeAllViews();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Type the name for the list");

		// Set up the input
		final EditText input = new EditText(this);
		// Specify the type of input expected; this, for example, sets the input
		// as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);

		// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				listName = input.getText().toString();
				Intent intent = new Intent(ShoppingActivity.this,
						ItemActivity.class);
				intent.putExtra("type","shopping");

				intent.putExtra("listname", listName);
				startActivityForResult(intent, 0);
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
	 * change the list name
	 */
	public void changeListName() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Change the name for the list");

		// Set up the input
		final EditText input = new EditText(this);
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
				DatabaseHandler db = new DatabaseHandler(ShoppingActivity.this);
				int i = db.changeShoppingListName(listName, newListName);
				Toast.makeText(ShoppingActivity.this,
						i + " recodes in " + listName + " have been changed.",
						Toast.LENGTH_LONG).show();
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

	public void removeList() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("You are going to delete " + listName
				+ ". Are you sure?");

		// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DatabaseHandler db = new DatabaseHandler(ShoppingActivity.this);
				int i = db.removeShoppingList(listName);
				Toast.makeText(ShoppingActivity.this,
						i + " recodes in " + listName + " have been deleted.",
						Toast.LENGTH_LONG).show();

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



	@Override
	public void onListPicked(String listName) {
		// TODO Auto-generated method stub
		this.listName = listName;
	}
}