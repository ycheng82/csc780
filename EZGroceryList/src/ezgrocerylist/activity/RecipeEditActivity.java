package ezgrocerylist.activity;

import com.ezgrocerylist.R;

import ezgrocerylist.circlemenu.view.CircleLayout;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * Activity to edit a recipe, i.e. add ingredient, add steps
 * 
 * @author Ye
 * 
 */
public class RecipeEditActivity extends Activity {
	private String recipeName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_recipe);
		
		//hide action and status bar
		View decorView = getWindow().getDecorView();
		// Hide the status bar.
		int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
		decorView.setSystemUiVisibility(uiOptions);
		// Remember that you should never show the action bar if the
		// status bar is hidden, so hide that too if necessary.
		ActionBar actionBar = getActionBar();
		actionBar.hide();

		// get list name from intent
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			recipeName = extras.getString("recipename");
		}

		TextView tvRecipeName = (TextView) findViewById(R.id.lbRecipeName);

		tvRecipeName.setText(recipeName);
	}

	/**
	 * callback when click the add ingredients button
	 */
	public void addIngredients(View view) {
		LinearLayout llIngredients = (LinearLayout) findViewById(R.id.layout_ingredients);
		LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		EditText tv = new EditText(this);
		tv.setLayoutParams(lparams);
		tv.setText("ingredients");
		llIngredients.addView(tv);
	}

	/**
	 * callback when click the add steps button
	 */
	public void addSteps(View view) {
		LinearLayout llIngredients = (LinearLayout) findViewById(R.id.layout_steps);
		LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		EditText tv = new EditText(this);
		tv.setLayoutParams(lparams);
		tv.setText("ingredients");
		llIngredients.addView(tv);
	}

	/**
	 * callback when click the add images button
	 */
	public void addImages(View view) {
		LinearLayout llIngredients = (LinearLayout) findViewById(R.id.layout_images);
		LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		EditText tv = new EditText(this);
		tv.setLayoutParams(lparams);
		tv.setText("images");
		llIngredients.addView(tv);
	}
	
	/**
	 * callback when click the add cover for a recipe
	 */
	public void addCover(View view) {
		
	}
}
