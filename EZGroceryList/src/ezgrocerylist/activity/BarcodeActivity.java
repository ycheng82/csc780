package ezgrocerylist.activity;

import com.ezgrocerylist.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BarcodeActivity extends Activity implements OnClickListener {
	private Button scanBtn, addBtn;
	private TextView formatTxt, contentTxt;
	private String scanContent, scanFormat;
	private String listName,editType;
	
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_barcode);
	    

		
		scanBtn = (Button)findViewById(R.id.scan_button);
		addBtn = (Button)findViewById(R.id.add_button);
		formatTxt = (TextView)findViewById(R.id.scan_format);
		contentTxt = (TextView)findViewById(R.id.scan_content);
		
		scanBtn.setOnClickListener(this);
		addBtn.setOnClickListener(this);
		
        //get list name from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            listName = extras.getString("listname");
            editType = extras.getString("type");
        }
	}
	
	public void onClick(View v){
		//respond to clicks
		if(v.getId()==R.id.scan_button){
			//scan

			IntentIntegrator.initiateScan(this);
			
		}
		else if(v.getId()==R.id.add_button ){
			Log.e("scan", "add clicked");
			Log.e("code", scanContent);
			Log.e("listname", listName);
			//return to ItemActivity
			Intent intent = new Intent(this, ItemActivity.class);
			Bundle extras = new Bundle();
			extras.putString("barcode",scanContent);
			extras.putString("listname",listName);
			extras.putString("type",editType);
			intent.putExtras(extras);
			startActivity (intent);
			
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		//retrieve scan result
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanningResult != null) {
			//we have a result
			scanContent = scanningResult.getContents();
			scanFormat = scanningResult.getFormatName();
			formatTxt.setText("FORMAT: " + scanFormat);
			contentTxt.setText("CONTENT: " + scanContent);
		}
		else{
		    Toast toast = Toast.makeText(getApplicationContext(),
		        "No scan data received!", Toast.LENGTH_SHORT);
		    toast.show();
		}
	}
	
}
