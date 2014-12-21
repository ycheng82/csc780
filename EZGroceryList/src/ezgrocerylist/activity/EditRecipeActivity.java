package ezgrocerylist.activity;

import com.ezgrocerylist.R;

import ezgrocerylist.sql.DatabaseHandler;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity to edit a recipe, i.e. add ingredient, add steps
 * 
 * @author Ye
 * 
 */
public class EditRecipeActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the three primary sections of the app. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will display the three primary sections of the
	 * app, one at a time.
	 */
	ViewPager mViewPager;

	private static String recipeName;
	protected static final int GALLERY_PICTURE = 1;
	protected static final int REQUEST_TAKE_PHOTO = 0;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe_edit);
		// get list name from intent
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			setRecipeName(extras.getString("recipename"));
		}
		// load recipe information from database if it exists
		/*
		 * DatabaseHandler db = new DatabaseHandler(this); if
		 * (db.getRecipe(recipeName)||db.getCoverImage(recipeName) != null) { //
		 * load cover image if (db.getCoverImage(recipeName) != null) { try {
		 * InputStream stream = getContentResolver().openInputStream(
		 * db.getCoverImage(recipeName)); bitmap =
		 * BitmapFactory.decodeStream(stream); stream.close();
		 * drawCover(bitmap); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 * 
		 * } // load ingredients if (db.getRecipe(recipeName)){ ArrayList<Item>
		 * ingredients =db.getIngredients(recipeName); //add ingredients (name,
		 * quantity, unit) to view for (int i = 0; i<ingredients.size();i++){
		 * LinearLayout llIngredients = (LinearLayout)
		 * findViewById(R.id.layout_ingredients); LayoutParams lparams = new
		 * LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		 * lparams.setMargins(50,0,0,0); Button btn = new Button(this);
		 * btn.setBackgroundColor
		 * (getResources().getColor(R.color.ingredient_btn));
		 * btn.setLayoutParams(lparams);
		 * btn.setText(ingredients.get(i).getItemName());
		 * llIngredients.addView(btn); }
		 * 
		 * } // load steps
		 * 
		 * // load images }
		 */
		// Create the adapter that will return a fragment for each of the three
		// primary sections
		// of the app.
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();

		// Specify that the Home/Up button should not be enabled, since there is
		// no hierarchical
		// parent.
		actionBar.setHomeButtonEnabled(false);

		// Specify that we will be displaying tabs in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set up the ViewPager, attaching the adapter and setting up a listener
		// for when the
		// user swipes between sections.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// When swiping between different app sections, select
						// the corresponding tab.
						// We can also use ActionBar.Tab#select() to do this if
						// we have a reference to the
						// Tab.
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter.
			// Also specify this Activity object, which implements the
			// TabListener interface, as the
			// listener for when this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mAppSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 0:
				// show intro fragment
				LaunchpadSectionFragment launchpadSectionFragment = new LaunchpadSectionFragment();
				launchpadSectionFragment.setRecipeName(recipeName);
				return launchpadSectionFragment;
			
			case 1:
				// show ingredient fragment
				IngredientsFragment ingredientsFragment = new IngredientsFragment();
				ingredientsFragment.setRecipeName(recipeName);
				return ingredientsFragment;
				
			case 2:
				// show step fragment
				StepsFragment stepsFragment = new StepsFragment();
				stepsFragment.setRecipeName(recipeName);
				return stepsFragment;
			
			/*case 3:
				// show image fragment
				ImageFragment imageFragment = new ImageFragment();
				imageFragment.setRecipeName(recipeName);
				return imageFragment;*/

			default:
				// The other sections of the app are dummy placeholders.
				Fragment fragment = new DummySectionFragment();
				Bundle args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
				fragment.setArguments(args);
				return fragment;
			}
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "Introductions";
			case 1:
				return "Ingredients";
			case 2:
				return "Steps";
			//case 3:
				//return "Images";
			default:
				return "Introductions";
			}
			// return "Section " + (position + 1);

		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {

		public static final String ARG_SECTION_NUMBER = "section_number";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_section_dummy,
					container, false);
			Bundle args = getArguments();
			((TextView) rootView.findViewById(android.R.id.text1))
					.setText(getString(R.string.dummy_section_text,
							args.getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}
	}
	/**
	 * delete this recipe from database
	 */
	/*public void clickDelete(View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("You are going to delete the recipe " + recipeName + ". Are you sure?");

		// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (deleteRecipe()){;
					Toast.makeText(((Dialog) dialog).getContext(),"Recipe "+ recipeName + " has been deleted.",
						Toast.LENGTH_LONG).show();
				}

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

	public boolean deleteRecipe(){
		DatabaseHandler db = new DatabaseHandler(this);
		if (db.deleteRecipe(recipeName)){
			return true;
		}
		return false;
	}*/

}