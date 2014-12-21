package ezgrocerylist.activity;

import com.ezgrocerylist.R;

import java.util.ArrayList;
import java.util.Locale;
 
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
 
public class VoiceActivity extends Activity {
 
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private String listName,editType;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
 
        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
 
        // hide the action bar
        getActionBar().hide();
 
        btnSpeak.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        
        //get list name from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            listName = extras.getString("listname");
            editType = extras.getString("type");
        }
 
    }
 
    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
 
    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 
        switch (requestCode) {
        case REQ_CODE_SPEECH_INPUT: {
            if (resultCode == RESULT_OK && null != data) {
 
                final ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                txtSpeechInput.setText(result.get(0));
                
                //alert dialog to confirm
                //yes to return to item input screen, no to say again
        		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        		builder.setTitle("Do you mean " + result.get(0)+ " ?");

        		// Set up the buttons
        		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() { 
        		    @Override
        		    public void onClick(DialogInterface dialog, int which) {
        		        //return to item input screen
        				Intent intent = new Intent(VoiceActivity.this, ItemActivity.class);
        				Bundle extras = new Bundle();
        				extras.putString("listname",listName);
        				extras.putString("itemname",result.get(0));
        				extras.putString("type",editType);
        				intent.putExtras(extras);
        				startActivity (intent);
        		    }
        		});
        		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
        		    @Override
        		    public void onClick(DialogInterface dialog, int which) {
        		        //say again
        		    	promptSpeechInput();
        		    }
        		});

        		builder.show();
            }
            break;
        }
 
        }
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
 
}
