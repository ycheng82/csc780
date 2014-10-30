package ezgrocerylist.activity;

import com.ezgrocerylist.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
        
        //list content
        
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
		listName.setText("");
		
		
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

}
