package ezgrocerylist.activity;

import java.util.ArrayList;

import com.ezgrocerylist.R;

import ezgrocerylist.sql.DatabaseHandler;
import ezgrocerylist.sql.Item;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class PantryActivity extends ActionBarActivity {
	
	private TextView actionName;
	private TextView listName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);
        listName = (TextView) findViewById(R.id.pantry_list_name_new);
        Log.d("Pantry","list name=" + listName.getText().toString());
        actionName = (TextView) findViewById(R.id.pantry_action_name);
        
        //create drop down menu for the pantry list page
        //create a spinnerAdapter for the menu
        SpinnerAdapter adapter =
                ArrayAdapter.createFromResource(getActionBar().getThemedContext(), R.array.pantry_menu,
                android.R.layout.simple_spinner_dropdown_item);

        // Add Callback functions here
        OnNavigationListener callback = new OnNavigationListener() {

            String[] items = getResources().getStringArray(R.array.pantry_menu); // List items from res

            @Override
            public boolean onNavigationItemSelected(int position, long id) {

                // Do stuff when navigation item is selected

                Log.d("NavigationItemSelected", items[position]); // Debug

                switch (position) {
                case 0:
                	//add action for selecting new list
                    actionName.setText("New List");
                    newList();
                    return true;
                case 1:
                	//add action for changing name for the list
                	actionName.setText("Change Name");
                	changListName();
                    return true;
                case 2:
                	//add action for changing category
                	actionName.setText("Category");
                    return true;
                case 3:
                	//add action for saving list
                	actionName.setText("Save");
                    return true;     
                case 4:
                	//add action for removing list
                	actionName.setText("Remove");
                    return true;  
                case 5:
                	//add action for adding list to a shopping list
                	actionName.setText("Add to Shopping");
                    return true;  
                case 6:
                	//add action for showing another list 1
                	actionName.setText("Other List 1");
                    return true;  
                case 7:
                	//add action for showing another list 2
                	actionName.setText("Other List 2");
                    return true;  
                case 8:
                	//add action for showing another list 3
                	actionName.setText("Other List 3");
                    return true;                      
                default:
                    return true;
                }
            }
        };

        // Action Bar
        ActionBar actions = getSupportActionBar();
        actions.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actions.setDisplayShowTitleEnabled(false);
        actions.setListNavigationCallbacks(adapter, callback);
        

        //show the content of the list
        showList();
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
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_bar_code:
	        	actionName.setText("bar code scanning");
	            return true;
	        case R.id.action_text:
	        	actionName.setText("text input");
	        	//call item input screen for an item
	            Intent intent = new Intent(this, ItemActivity.class);
	            
	            intent.putExtra("listname",listName.getText().toString());
	            startActivityForResult(intent,0);
	            return true;
	        case R.id.action_email:
	        	actionName.setText("share by email");
	            return true;
	        case R.id.action_voice:
	        	actionName.setText("voice input");
	            return true;	            
	        case R.id.action_settings:
	        	actionName.setText("setting");
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 * make a new list
	 */
	protected void newList() {
		listName.setText(R.string.new_list);
		
		
	}


	/**
	 * change the list name
	 */
	public void changListName() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Change the name for the list");

		// Set up the input
		final EditText input = new EditText(this);
		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);

		// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        listName.setText(input.getText().toString());
		    }
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        dialog.cancel();
		    }
		});

		builder.show();
	}
    /*
     * This method will be called whenever return from pantry screen, 
     * shop screen and recipe screen.
     * 
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

           // int calls = data.getExtras().getInt("CALLS");
        	showList();
        }
        
    }
	/**
	 * show the item information (read from database) on the screen
	 * 
	 */
	private void showList() {
		DatabaseHandler db = new DatabaseHandler(this);
		
		//retrieve items from database
		ArrayList<Item> items = new ArrayList<Item>();
		items = db.getItems(listName.getText().toString());
		
		//add view for these items if they are not null
		if (items.size()!=0){
			for (int i = 0; i < items.size();i++){
				// create a new textview
			    final TextView rowTextView = new TextView(this);

			    // set some properties of rowTextView or something
			    rowTextView.setText(items.get(i).getItemName() +"	"+
			    items.get(i).getItemQuantity() + "	" + 
			    		items.get(i).getItemUnit());

			    // add the textview to the linearlayout
			    LinearLayout pantryLayout = (LinearLayout) findViewById(R.id.activity_pantry);
			    pantryLayout.addView(rowTextView);
			}
		}

	}
}
