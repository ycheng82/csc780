package ezgrocerylist.activity;

import java.util.ArrayList;

import com.ezgrocerylist.R;

import ezgrocerylist.sql.DatabaseHandler;
import ezgrocerylist.sql.Item;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * ingredient fragment to show all recipe ingredients
 * @author Ye
 *
 */
public class IngredientsFragment extends Fragment {
	private String recipeName;
	private View rootView;
	protected final int INGREDIENT_EDIT = 1;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);
        rootView.findViewById(R.id.ibIngredientsPlus)
        .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	addIngredients(view);
            }
        });

		// load recipe information from database if it exists
		DatabaseHandler db = new DatabaseHandler(this.getActivity());
		if (db.getRecipe(recipeName)) {
			ArrayList<Item> ingredients =db.getIngredients(recipeName);
			
			
			//add ingredients (name, quantity, unit) to view
			for (int i = 0; i<ingredients.size();i++){
				LinearLayout llIngredients = (LinearLayout) rootView.findViewById(R.id.layout_ingredients);
				LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				lparams.setMargins(50,0,0,0);
				Button btn = new Button(this.getActivity());
				//btn.setBackgroundColor(getResources().getColor(R.color.ingredient_btn));
				btn.getBackground().setAlpha(0);
				btn.setLayoutParams(lparams);
				btn.setText(ingredients.get(i).getItemName()+"						"
						+ingredients.get(i).getItemQuantity()+" "
						+ingredients.get(i).getItemUnit());
				final String itemName = ingredients.get(i).getItemName();
				btn.setOnClickListener(new View.OnClickListener() {
		            @Override
		            public void onClick(View view) {
		            	editIngredients(view,itemName);
		            }
		        });

				llIngredients.addView(btn);
			}
		}
		db.close();
        return rootView;
    }
	/**
	 * callback when click the add ingredients button
	 */
	public void addIngredients(View view) {
		Intent intent = new Intent(this.getActivity(),IngredientActivity.class);
		intent.putExtra("recipename",recipeName);
		startActivityForResult(intent,INGREDIENT_EDIT);
	}
	/**
	 * callback when click the ingredients, providing ingredients editting function
	 */
	public void editIngredients(View view,String itemName) {
		Intent intent = new Intent(this.getActivity(),IngredientActivity.class);
		intent.putExtra("recipename",recipeName);
		intent.putExtra("itemname",itemName);
		startActivityForResult(intent,INGREDIENT_EDIT);
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
	  if (requestCode == INGREDIENT_EDIT) {
	     //Refresh the fragment here
			// load recipe information from database if it exists
			DatabaseHandler db = new DatabaseHandler(this.getActivity());
			if (db.getRecipe(recipeName)) {
				ArrayList<Item> ingredients =db.getIngredients(recipeName);
				LinearLayout llIngredients = (LinearLayout) rootView.findViewById(R.id.layout_ingredients);
				llIngredients.removeAllViews();
				//add ingredients (name, quantity, unit) to view
				for (int i = 0; i<ingredients.size();i++){
					//LinearLayout llIngredients = (LinearLayout) rootView.findViewById(R.id.layout_ingredients);
					LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					lparams.setMargins(50,0,0,0);
					Button btn = new Button(this.getActivity());
					//btn.setBackgroundColor(getResources().getColor(R.color.ingredient_btn));
					btn.getBackground().setAlpha(0);
					btn.setLayoutParams(lparams);
					btn.setText(ingredients.get(i).getItemName()+"						"
							+ingredients.get(i).getItemQuantity()+" "
							+ingredients.get(i).getItemUnit());
					final String itemName = ingredients.get(i).getItemName();
					btn.setOnClickListener(new View.OnClickListener() {
			            @Override
			            public void onClick(View view) {
			            	editIngredients(view,itemName);
			            }
			        });

					llIngredients.addView(btn);
				}
			}
			db.close();
	  }
	}

}
