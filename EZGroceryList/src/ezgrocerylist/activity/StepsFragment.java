package ezgrocerylist.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.ezgrocerylist.R;

import ezgrocerylist.sql.DatabaseHandler;
import ezgrocerylist.sql.Item;
import ezgrocerylist.sql.Step;

/**
 * recipe steps fragment to show all recipe steps
 * @author Ye
 *
 */
public class StepsFragment extends Fragment {
	private String recipeName;
	private View rootView;
	protected final int STEP_EDIT = 1;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_steps, container, false);
        rootView.findViewById(R.id.ibStepsPlus)
        .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	addSteps(view);
            }
        });

		// load recipe information from database if it exists
		DatabaseHandler db = new DatabaseHandler(this.getActivity());
		if (db.getRecipe(recipeName)) {
			ArrayList<Step> steps =db.getSteps(recipeName);
			
			
			//add ingredients (name, quantity, unit) to view
			for (int i = 0; i<steps.size();i++){
				LinearLayout llSteps = (LinearLayout) rootView.findViewById(R.id.layout_steps);
				LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				lparams.setMargins(50,0,0,0);
				Button btn = new Button(this.getActivity());
				//btn.setBackgroundColor(getResources().getColor(R.color.ingredient_btn));
				btn.getBackground().setAlpha(0);
				btn.setLayoutParams(lparams);
				btn.setText(steps.get(i).getStepId()+"						"
						+steps.get(i).getStepDetail());
				final int stepId = steps.get(i).getStepId();

				btn.setOnClickListener(new View.OnClickListener() {
		            @Override
		            public void onClick(View view) {
		            	editSteps(view,stepId);
		            }
		        });

				llSteps.addView(btn);
			}
		}
		db.close();
        return rootView;
    }
	/**
	 * callback when click the add ingredients button
	 */
	public void addSteps(View view) {
		Intent intent = new Intent(this.getActivity(),StepActivity.class);
		intent.putExtra("recipename",recipeName);
		startActivityForResult(intent,STEP_EDIT);
	}
	/**
	 * callback when click the ingredients, providing ingredients editting function
	 */
	public void editSteps(View view,int stepId) {
		Log.e("stepId",stepId+"");
		Intent intent = new Intent(this.getActivity(),StepActivity.class);
		intent.putExtra("recipename",recipeName);
		intent.putExtra("stepid",stepId);
		startActivityForResult(intent,STEP_EDIT);
	}
	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
	  if (requestCode == STEP_EDIT) {
	     //Refresh the fragment here
			// load recipe information from database if it exists
			DatabaseHandler db = new DatabaseHandler(this.getActivity());
			if (db.getSteps(recipeName)!=null) {
				ArrayList<Step> steps =db.getSteps(recipeName);
				LinearLayout llIngredients = (LinearLayout) rootView.findViewById(R.id.layout_steps);
				llIngredients.removeAllViews();
				//add ingredients (name, quantity, unit) to view
				for (int i = 0; i<steps.size();i++){
					//LinearLayout llIngredients = (LinearLayout) rootView.findViewById(R.id.layout_ingredients);
					LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					lparams.setMargins(50,0,0,0);
					Button btn = new Button(this.getActivity());
					//btn.setBackgroundColor(getResources().getColor(R.color.ingredient_btn));
					btn.getBackground().setAlpha(0);
					btn.setLayoutParams(lparams);
					btn.setText(steps.get(i).getStepId()+"						"
							+steps.get(i).getStepDetail());
					final int stepId = steps.get(i).getStepId();
					
					btn.setOnClickListener(new View.OnClickListener() {
			            @Override
			            public void onClick(View view) {
			            	editSteps(view,stepId);
			            }
			        });

					llIngredients.addView(btn);
				}
			}
			db.close();
	  }
	}

}

