package ezgrocerylist.activity;

import java.util.ArrayList;
import java.util.List;

import com.ezgrocerylist.R;

import ezgrocerylist.sql.DatabaseHandler;
import ezgrocerylist.sql.Item;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

/**
 * ingredient fragment to show all recipe ingredients
 * @author Ye
 *
 */
public class IngredientsFragment extends Fragment implements OnClickListener{
	private String recipeName;
	private View rootView;
	protected final int INGREDIENT_EDIT = 1;
	private ArrayList<Item> shoppingItems;
	private Button btShopping;
	
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
        
        btShopping = null;
        btShopping =(Button) rootView.findViewById(R.id.bt_shopping);
        btShopping.setOnClickListener(this);
		// load recipe information from database if it exists
		DatabaseHandler db = new DatabaseHandler(this.getActivity());
		if (db.getRecipe(recipeName)) {
			ArrayList<Item> ingredients =db.getIngredients(recipeName);
			shoppingItems = new ArrayList<Item>();
			
			//add ingredients (name, quantity, unit) to view
			for (int i = 0; i<ingredients.size();i++){
				LinearLayout llIngredients = (LinearLayout) rootView.findViewById(R.id.layout_ingredients);
				LinearLayout llay1 = new LinearLayout(this.getActivity()); 
				llay1.setOrientation(LinearLayout.HORIZONTAL);
				LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				lparams.setMargins(50,0,0,0);
				final CheckBox cb = new CheckBox(this.getActivity());
				final Item ingredient = ingredients.get(i);
				cb.setOnClickListener(new OnClickListener() {

		            @Override
		            public void onClick(View v) {
		                // TODO Auto-generated method stub
		                if(cb.isChecked()){
		                	shoppingItems.add(ingredient);
		                }else{
		                	shoppingItems.remove(ingredient);
		                }
		            }
		        });
   
				llay1.addView(cb);
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
				llay1.addView(btn);
				llIngredients.addView(llay1);
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
					LinearLayout llay1 = new LinearLayout(this.getActivity()); 
					llay1.setOrientation(LinearLayout.HORIZONTAL);
					LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					lparams.setMargins(50,0,0,0);
					final CheckBox cb = new CheckBox(this.getActivity());
					final Item ingredient = ingredients.get(i);
					cb.setOnClickListener(new OnClickListener() {

			            @Override
			            public void onClick(View v) {
			                // TODO Auto-generated method stub
			                if(cb.isChecked()){
			                	shoppingItems.add(ingredient);
			                }else{
			                	shoppingItems.remove(ingredient);
			                }
			            }
			        });
					llay1.addView(cb);
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
					llay1.addView(btn);
					llIngredients.addView(llay1);
				}
			}
			db.close();
	  }
	}
	public ArrayList<Item> getShoppingItems() {
		return shoppingItems;
	}
	public void setShoppingItems(ArrayList<Item> shoppingItems) {
		this.shoppingItems = shoppingItems;
	}
	public void clickAdd(){
		if (shoppingItems != null && shoppingItems.size()>0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Type the name for your shopping list");

			// Set up the input
			final EditText input = new EditText(getActivity());
			// Specify the type of input expected; this, for example, sets the input
			// as a password, and will mask the text
			input.setInputType(InputType.TYPE_CLASS_TEXT);
			builder.setView(input);

			// Set up the buttons
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					addShopping(input.getText().toString(),shoppingItems);

				}
			});
			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});

			builder.show();
		}
	}
	public void addShopping(String shoppingListName,ArrayList<Item> shoppingItems){
		ArrayList<Item> items = new ArrayList<Item>();
		DatabaseHandler db = new DatabaseHandler(getActivity());
		for (int i = 0;i<shoppingItems.size();i++){
			Item item = shoppingItems.get(i);
			items.add(item);
		}
		//call db to add to shopping table
		if (db.addShoppingItems(shoppingListName,items)){
			Toast.makeText(getActivity(), "Selected items added to shopping list "+shoppingListName,
					Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onClick(View v) {
		  switch (v.getId()) {
		    case R.id.bt_shopping:
		        // your stuff here
		        clickAdd();

		        break;          
		    default:                
		        break;
		   }
		
	}
}
