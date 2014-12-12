package ezgrocerylist.activity;

import com.ezgrocerylist.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;

public class InforActivity extends Activity {
    ListView listView ;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor);
        
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.infor_list);
        
        // Defined Array values to show in ListView
        String[] values = new String[] { "Author", 
                                         "Privacy Policy",
                                         "Term of Use",
                                         "Support", 
                                         "Version Information", 

                                        };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
          android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter); 
        
        // ListView Item Click Listener
        listView.setOnItemClickListener(new OnItemClickListener() {

              @Override
              public void onItemClick(AdapterView<?> parent, View view,
                 int position, long id) {
                
               // ListView Clicked item index
               int itemPosition     = position;
               
               // ListView Clicked item value
               String  itemValue    = (String) listView.getItemAtPosition(position);
                  
                // Show Alert 
                Toast.makeText(getApplicationContext(),
                  "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                  .show();
                switch (position){
                	case 0:
                		//author
    					Intent authorIntent = new Intent(InforActivity.this,AuthorActivity.class);
    					startActivity(authorIntent);
                		break;
                	case 1:
                		//privacy policy
    					Intent policyIntent = new Intent(InforActivity.this,PolicyActivity.class);
    					startActivity(policyIntent);
                		break;
                	case 2:
                		//term of use
    					Intent useIntent = new Intent(InforActivity.this,UseActivity.class);
    					startActivity(useIntent);
                		break;
                	case 3:
                		//support
    					Intent supportIntent = new Intent(InforActivity.this,SupportActivity.class);
    					startActivity(supportIntent);
                		break;
                	case 4:
                		//version information
    					Intent versionIntent = new Intent(InforActivity.this,VersionActivity.class);
    					startActivity(versionIntent);
                		break;
                		
                }
                
                	
             
              }

         }); 
    }

}