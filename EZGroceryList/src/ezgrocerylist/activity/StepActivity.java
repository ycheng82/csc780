package ezgrocerylist.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.NumberPicker.OnValueChangeListener;

import com.ezgrocerylist.R;

import ezgrocerylist.sql.DatabaseHandler;
import ezgrocerylist.sql.Step;

/**
 * activity to edit a recipe step
 * @author Ye
 *
 */
public class StepActivity extends Activity {
	private NumberPicker npStepId;
	private String recipeName;
	private int stepId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_step);
		
		// set up the values for step id using number picker
		npStepId = (NumberPicker) findViewById(R.id.npStepId);
		npStepId.setMinValue(1);
		npStepId.setMaxValue(100);
		npStepId.setWrapSelectorWheel(false);
		npStepId.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				// TODO Auto-generated method stub

			}
		});
		
		// get list name from intent
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			
			recipeName = extras.getString("recipename");
			stepId = extras.getInt("stepid");
			if (stepId != 0) {
				// get ingredient information from database and display
				
				DatabaseHandler db = new DatabaseHandler(this);
				Step step = db.getStep(recipeName, stepId);
				// set step detail
				((EditText) findViewById(R.id.etStepDetail)).setText(step.getStepDetail());
				// set step id
				((NumberPicker) findViewById(R.id.npStepId)).setValue(step.getStepId());
			}
		}
	}
	
	/**
	 * save the recipe step to the database
	 */
	public void saveStep(View view) {
		// get list name using get extra
		// check the input cannot be null
		// add to database
		DatabaseHandler db = new DatabaseHandler(this);
		String stepDetail = ((EditText) findViewById(R.id.etStepDetail)).getText()
				.toString();
		int stepId = ((NumberPicker) findViewById(R.id.npStepId))
				.getValue();
		Step step = new Step(stepId,stepDetail);
		db.addStep(recipeName, step);
		db.close();
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		Toast.makeText(this, "Step saved", Toast.LENGTH_SHORT).show();
		finish();
	}

	/**
	 * move the item to another list
	 */
	public void moveStep(View view) {

	}

	/**
	 * delete the step from the database
	 */
	public void deleteStep(View view) {
		DatabaseHandler db = new DatabaseHandler(this);
		int stepId = ((NumberPicker) findViewById(R.id.npStepId))
				.getValue();
		db.deleteStep(recipeName, stepId);
		db.close();
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		Toast.makeText(this, "Step deleted", Toast.LENGTH_SHORT).show();
		finish();
	}

}