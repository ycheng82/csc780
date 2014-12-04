package ezgrocerylist.activity;

import com.ezgrocerylist.R;

import ezgrocerylist.sql.DatabaseHandler;
import ezgrocerylist.sql.Item;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.NumberPicker.OnValueChangeListener;


public class ItemActivity extends Activity {
	NumberPicker npQuantity;
	String listName, barcode,itemName,itemQuantity,editType;
	final static public String TAG = "storage";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item);
		
		//set up the values for item quantity using number picker 
		npQuantity = (NumberPicker) findViewById(R.id.npQuantity);
        npQuantity.setMinValue(0);
        npQuantity.setMaxValue(100);
        npQuantity.setWrapSelectorWheel(false); 
        npQuantity.setOnValueChangedListener(new OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				// TODO Auto-generated method stub
	
			}
        });
		//set up the values for item unit using spinner
        Spinner spinner = (Spinner) findViewById(R.id.spUnit);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.item_unit, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new item is selected (in the Spinner)
             */
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, 
			            int pos, long id) {
					// TODO Auto-generated method stub
			        String selected = parent.getItemAtPosition(pos).toString();

			        if(selected.equals("other"))
			        {
			        	addUnit();
			        	//NOT COMPLETE YET
			        }

				}            

                public void onNothingSelected(AdapterView<?> parent) {
                    // Do nothing, just another required interface callback
                }
        });
        
   
		//set up the values for item category using spinner
        Spinner spCategory = (Spinner) findViewById(R.id.spCategory);
        ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(this,
                R.array.item_category, android.R.layout.simple_spinner_item);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapterCategory);
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new category is selected (in the Spinner)
             */
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, 
			            int pos, long id) {
					// TODO Auto-generated method stub
			        String selected = parent.getItemAtPosition(pos).toString();

			        if(selected.equals("new"))
			        {
			        	//addCategory();
			        	//NOT COMPLETE YET
			        }

				}            

                public void onNothingSelected(AdapterView<?> parent) {
                    // Do nothing, just another required interface callback
                }
        });
        
        //get list name from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            listName = extras.getString("listname");
            barcode = extras.getString("barcode");
            itemName = extras.getString("itemname");
            itemQuantity = extras.getString("itemquantity");
            editType = extras.getString("type");
            if (barcode!=null){
            	((EditText) findViewById(R.id.etBarcode)).setText(barcode);
            }
            if (itemName!=null){
            	((EditText) findViewById(R.id.etItemName)).setText(itemName);
            	if (itemQuantity!=null){
            		// get item information from database and display
    				DatabaseHandler db = new DatabaseHandler(this);
    				Item item = new Item();
    				if (editType.equals("shopping")){
    					item = db.getShoppingItem(listName, itemName);
    				}
    				else{
    					item = db.getItem(listName, itemName);
    				}
    				
    				//Log.e("Category",item.getItemCategory());
    				// set item name
    				((EditText) findViewById(R.id.etItemName)).setText(item
    						.getItemName());
    				// set item price
    				((EditText) findViewById(R.id.etPrice)).setText(Float.toString(item
    						.getItemPrice()));
    				// set item category
    				((Spinner) findViewById(R.id.spCategory)).setSelection(getIndex(
    						spCategory, item.getItemCategory()));
    				// set item unit
    				((Spinner) findViewById(R.id.spUnit)).setSelection(getIndex(
    						spinner, item.getItemUnit()));
    				// set item quantity
    				((NumberPicker) findViewById(R.id.npQuantity)).setValue(item.getItemQuantity());
    				// set item note
    				((EditText) findViewById(R.id.etNote)).setText(item
    						.getItemNote());
    				// set item barcode
    				((EditText) findViewById(R.id.etBarcode)).setText(item
    						.getItemBarcode());
            	}
            }
        }


    }


	/**
	 * method to add a new unit when click the "other" in the unit spinner
	 * NOT COMPLETE YET
	 */
	public void addUnit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("The unit name you want to add");

		// Set up the input
		final EditText input = new EditText(this);
		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);

		// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	// add (input.getText().toString()
		    	//add to database, also read from database
		    	//TO DO
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
	 * save the item to the database
	 */
	public void saveItem(View view){
		//get list name using get extra
		//check the input cannot be null
		//add to database
        DatabaseHandler db = new DatabaseHandler(this);
        String itemName = ((EditText) findViewById(R.id.etItemName)).getText().toString();
        int itemQuantity = ((NumberPicker) findViewById(R.id.npQuantity)).getValue();
        String itemUnit = ((Spinner) findViewById(R.id.spUnit)).getSelectedItem().toString();
        float itemPrice = Float.parseFloat(((EditText) findViewById(R.id.etPrice)).getText().toString());
        String itemNote = ((EditText) findViewById(R.id.etNote)).getText().toString();
        String itemCategory = ((Spinner) findViewById(R.id.spCategory)).getSelectedItem().toString();
        String itemBarcode = ((EditText) findViewById(R.id.etBarcode)).getText().toString();
        Item item = new Item(itemName,itemQuantity,itemUnit, itemPrice, itemNote, itemCategory,itemBarcode);
        if (editType.equals("shopping")){
        	db.addShoppingItem(listName,item);
        }
        else {
        	db.addItem(listName,item);
        }
        db.close();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        Toast.makeText(this, "Item saved", Toast.LENGTH_LONG).show();
        finish();
	}
	
	/**
	 * move the item to another list
	 */
	public void moveItem(View view){
		
	}
	
	/**
	 * delete the item from the database
	 */
	public void deleteItem(View view){
		DatabaseHandler db = new DatabaseHandler(this);
		String itemName = ((EditText) findViewById(R.id.etItemName)).getText()
				.toString();
        if (editType.equals("shopping")){
        	db.deleteShoppingItem(listName, itemName);
        }
        else {
        	db.deleteItem(listName, itemName);
        }

		db.close();
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		Toast.makeText(this, itemName + " deleted", Toast.LENGTH_LONG).show();
		finish();
	}
	private int getIndex(Spinner spinner, String myString) {

		int index = 0;

		for (int i = 0; i < spinner.getCount(); i++) {
			if (spinner.getItemAtPosition(i).equals(myString)) {
				index = i;
			}
		}
		return index;
	}
	

}
