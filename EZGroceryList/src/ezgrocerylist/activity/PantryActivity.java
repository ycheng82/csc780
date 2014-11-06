package ezgrocerylist.activity;

import java.util.ArrayList;
import java.util.HashSet;

import com.ezgrocerylist.R;

import ezgrocerylist.sql.DatabaseHandler;
import ezgrocerylist.sql.Item;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class PantryActivity extends ActionBarActivity {
	
	//private TextView actionName;
	//private TextView listName;
	String listName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);
        //listName = (TextView) findViewById(R.id.pantry_list_name_new);
        //Log.d("Pantry","list name=" + listName.getText().toString());
        //actionName = (TextView) findViewById(R.id.pantry_action_name);
        
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
                    return true;
                case 1:
                	//add action for selecting new list
                    //actionName.setText("New List");
                    newList();
                    return true;
                case 2:
                	//add action for changing name for the list
                	//actionName.setText("Change Name");
                	changeListName();
                    return true;
                case 3:
                	//add action for changing category
                	//actionName.setText("Category");
                    return true;
                case 4:
                	//add action for saving list
                	//actionName.setText("Save");
                    return true;     
                case 5:
                	//add action for removing list
                	//actionName.setText("Remove");
                    return true;  
                case 6:
                	//add action for adding list to a shopping list
                	//actionName.setText("Add to Shopping");
                    return true;  
                case 7:
                	//add action for showing another list 1
                	//actionName.setText("Other List 1");
                    return true;  
                case 8:
                	//add action for showing another list 2
                	//actionName.setText("Other List 2");
                    return true;  
                case 9:
                	//add action for showing another list 3
                	//actionName.setText("Other List 3");
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
        
        //show the names for all grocery lists
        showAllLists();

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
	        	//actionName.setText("bar code scanning");
	        	//call barcode scanner screen for an item
	            Intent barcodeIntent = new Intent(this, BarcodeActivity.class);
	            barcodeIntent.putExtra("listname",listName);
	            startActivity(barcodeIntent);
	            return true;
	        case R.id.action_text:
	        	//actionName.setText("text input");
	        	//call item input screen for an item
	            Intent intent = new Intent(this, ItemActivity.class);
	            
	            intent.putExtra("listname",listName);
	            startActivityForResult(intent,0);
	            return true;
	        case R.id.action_email:
	        	//actionName.setText("share by email");
	            return true;
	        case R.id.action_voice:
	        	//actionName.setText("voice input");
	            return true;	            
	        case R.id.action_settings:
	        	//actionName.setText("setting");
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 * make a new list
	 */
	protected void newList() {
		LinearLayout childLayout = (LinearLayout) findViewById(R.id.layoutChild);
		childLayout.removeAllViews();
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
		        listName=input.getText().toString();
				Intent intent = new Intent(PantryActivity.this, ItemActivity.class);
		        
		        intent.putExtra("listname",listName);
		        startActivityForResult(intent,0);
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


	/**
	 * change the list name
	 */
	public void changeListName() {
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
		        listName=input.getText().toString();
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
        	showAllLists();
        	showList(listName);
        }
        
    }
	/**
	 * show the item information (read from database) on the screen
	 * 
	 */
	private void showList(String listName) {
		DatabaseHandler db = new DatabaseHandler(this);
		
		//retrieve items from database
		ArrayList<Item> items = new ArrayList<Item>();
		items = db.getItems(listName);
		
		//split items into category
		ArrayList<String> cats = new ArrayList<String>();
		HashSet<String> uCats = null;
		if (items.size()!=0){
			for (int i = 0; i < items.size();i++){
				cats.add(items.get(i).getItemCategory());
			}
			uCats = new HashSet<>(cats);
			//Log.d("showList","#category: " + uCats.size());
		}
		
		//add view for these items if they are not null
		if (uCats!=null){
			//find childlayout for add list content
			LinearLayout childLayout = (LinearLayout) findViewById(R.id.layoutChild);
			childLayout.removeAllViews();
			
			//add list name label
			TextView lbListName = new TextView(this);
			String txListName =listName;
			lbListName.setBackgroundResource(R.drawable.pantry_listname_label);
			lbListName.setTextColor(Color.rgb(255,255,255));
			// set some properties of rowTextView or something
			lbListName.setText(txListName);
			
			// add the textview (list contents) to the linearlayout
			childLayout.addView(lbListName);
			
			for (String value : uCats){
				// create a new textview
				TextView rowTextView = new TextView(this);
				String listText ="";
				listText += value;
				rowTextView.setBackgroundResource(R.drawable.pantry_textview_label);
				rowTextView.setTextColor(Color.rgb(255,255,255));
				// set some properties of rowTextView or something
				rowTextView.setText(listText);
				// add the textview to the linearlayout
				childLayout.addView(rowTextView);
				
				rowTextView = new TextView(this);
				listText ="";
				for (int j = 0; j < items.size();j++){
					
					if (items.get(j).getItemCategory().equals (value)){
						//Log.d("category 1 ",items.get(j).getItemCategory());
						listText += items.get(j).getItemName() +"		"+ 
								items.get(j).getItemQuantity() + "		" + 
								items.get(j).getItemUnit()+ "\n";
					}
				}
				rowTextView.setBackgroundResource(R.drawable.pantry_textview_back);
				// set some properties of rowTextView or something
				rowTextView.setText(listText);
				// add the textview to the linearlayout
				childLayout.addView(rowTextView);
			}
		}

	}
	

	/**
	 * show the names of all pantry lists
	 */
	private void showAllLists() {
		// TODO Auto-generated method stub
		LinearLayout parentLayout = (LinearLayout) findViewById(R.id.layoutParent);
		parentLayout.removeAllViews();
		DatabaseHandler db = new DatabaseHandler(this);
		ArrayList<String> listNames = new ArrayList<String>();
		listNames = db.getAllLists();
		//get unique list names
		HashSet<String> uCats = null;
		if (listNames.size()!=0){
			uCats = new HashSet<>(listNames);
			//Log.d("showAllList","#grocery lists: " + listNames.size());
		}
		//add view for these list names if they are not null
		if (uCats!=null){
			//find layout for list names
			parentLayout = (LinearLayout) findViewById(R.id.layoutParent);
			//add radio button and radio button group
			final RadioButton[] rb = new RadioButton[uCats.size()];
			final RadioGroup rg = new RadioGroup(this);
			rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
			int i = 0;
			for (String value : uCats){
			        rb[i]  = new RadioButton(this);
			        rg.addView(rb[i]); //the RadioButtons are added to the radioGroup instead of the layout
			        rb[i].setText(value);
			        i++;
			}	
			//set the first radio button to be checked as default
			((RadioButton) rg.getChildAt(0)).setChecked(true);
			parentLayout.addView(rg);
			
		    //add a new button to view list content
			final Button btnView = new Button(this);
			btnView.setTextAppearance(this,R.style.button_view);
		    btnView.setText("View");
		    btnView.setBackgroundResource(R.drawable.button_view);
		    //btnView.setTextSize(14);//add to values later
		    //btnView.setTextColor(Color.rgb(255,255,255));
		    //btnView.setBackgroundColor(Color.rgb(51,0,0));
		    btnView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		    btnView.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View view) {
		            listName = ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();
		            showList(listName);
		        }
		    });
		    parentLayout.addView(btnView);
		}

	}

}
