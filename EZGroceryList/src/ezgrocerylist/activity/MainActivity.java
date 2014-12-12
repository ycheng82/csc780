package ezgrocerylist.activity;

import ezgrocerylist.circlemenu.view.CircleLayout.OnItemClickListener;
import ezgrocerylist.circlemenu.view.CircleLayout.OnItemSelectedListener;

import android.os.Bundle;

import com.ezgrocerylist.R;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ezgrocerylist.circlemenu.view.CircleImageView;
import ezgrocerylist.circlemenu.view.CircleLayout;
import android.content.Intent;
import android.view.Menu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;

/**
 * Description: <br/>
 * site: <a href="http://www.crazyit.org">crazyit.org</a> <br/>
 * Copyright (C), 2001-2014, Yeeku.H.Lee <br/>
 * This program is protected by copyright laws. <br/>
 * Program Name: <br/>
 * Date:
 * 
 * @author Yeeku.H.Lee kongyeeku@163.com
 * @version 1.0
 */
public class MainActivity extends Activity implements OnItemSelectedListener,
		OnItemClickListener {
	TextView selectedTextView;
	ImageView imageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		CircleLayout circleMenu = (CircleLayout) findViewById(R.id.main_circle_layout);
		circleMenu.setOnItemSelectedListener(this);
		circleMenu.setOnItemClickListener(this);

		selectedTextView = (TextView) findViewById(R.id.main_selected_textView);
		selectedTextView.setText(((CircleImageView) circleMenu
				.getSelectedItem()).getName());

		ExpandableListAdapter adapter = new BaseExpandableListAdapter() {
			int[] logos = new int[] { R.drawable.main_pantry,
					R.drawable.main_recipe, R.drawable.main_shoppinglist,
					R.drawable.main_setting, R.drawable.main_icon, };

			private String[] armTypes = new String[] { "Pantry List",
					"Recipe List", "Shopping List", "Setting", "About" };
			private String[][] arms = new String[][] { { "1", "2", "3", "4" },
					{ "1", "2", "3", "4" }, { "1", "2", "3", "4" },
					{ "1", "2", "3", "4" }, { "1", "2", "3", "4" }, };

			@Override
			public Object getChild(int groupPosition, int childPosition) {
				return arms[groupPosition][childPosition];
			}

			@Override
			public long getChildId(int groupPosition, int childPosition) {
				return childPosition;
			}

			@Override
			public int getChildrenCount(int groupPosition) {
				return arms[groupPosition].length;
			}

			private TextView getTextView() {
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, 64);
				TextView textView = new TextView(MainActivity.this);
				textView.setLayoutParams(lp);
				textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
				textView.setPadding(50, 0, 0, 0);
				textView.setTextSize(20);
				return textView;
			}

			@Override
			public View getChildView(int groupPosition, int childPosition,
					boolean isLastChild, View convertView, ViewGroup parent) {
				LinearLayout ll = new LinearLayout(MainActivity.this);
				ll.setOrientation(1);
				TextView textView = getTextView();
				textView.setText(getChild(groupPosition, childPosition)
						.toString());
				ll.addView(textView);

				SeekBar seek = new SeekBar(MainActivity.this);
				seek.setBackgroundColor(110011);
				ll.addView(seek);
				return ll;
			}

			@Override
			public Object getGroup(int groupPosition) {
				return armTypes[groupPosition];
			}

			@Override
			public int getGroupCount() {
				return armTypes.length;
			}

			@Override
			public long getGroupId(int groupPosition) {
				return groupPosition;
			}

			@Override
			public View getGroupView(int groupPosition, boolean isExpanded,
					View convertView, ViewGroup parent) {
				LinearLayout ll = new LinearLayout(MainActivity.this);
				ll.setOrientation(0);
				ImageView logo = new ImageView(MainActivity.this);

				LayoutParams p1 = new LayoutParams(100, 100);
				logo.setLayoutParams(p1);
				logo.setImageResource(logos[groupPosition]);
				ll.addView(logo);
				TextView textView = getTextView();
				textView.setText(getGroup(groupPosition).toString());
				ll.addView(textView);
				return ll;
			}

			@Override
			public boolean isChildSelectable(int groupPosition,
					int childPosition) {
				return true;
			}

			@Override
			public boolean hasStableIds() {
				return true;
			}

		};
		/*
		 * ExpandableListView expandListView = (ExpandableListView)
		 * findViewById(R.id.list); expandListView.setAdapter(adapter); //
		 * expandListView.setGroupIndicator(null); //
		 * expandListView.setGroupIndicator(this.getResources().getDrawable(
		 * R.drawable.arrow)); int width =
		 * getWindowManager().getDefaultDisplay().getWidth();
		 * expandListView.setIndicatorBounds(width-80, width-0);
		 */

		imageView = (ImageView) findViewById(R.id.center_image);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView txtView = (TextView) findViewById(R.id.main_selected_textView);
				// toast=Toast.maketext("½øÈë"+txtView.getText().toString());
				// toast.show();
				Toast.makeText(
						MainActivity.this,
						"Switch to " + txtView.getText().toString() + " screen",
						Toast.LENGTH_LONG).show();
				switch (txtView.getText().toString()) {
				case "Pantry List":
					Intent pantryIntent = new Intent(MainActivity.this,
							PantryActivity.class);
					startActivity(pantryIntent);
					break;
				case "Recipe List":
					Intent recipeIntent = new Intent(MainActivity.this,
							RecipeActivity.class);
					startActivity(recipeIntent);
					break;
				case "Shopping List":
					Intent shoppingIntent= new Intent(MainActivity.this,
					ShoppingActivity.class);
					startActivity(shoppingIntent);
					break;
				case "Setting":
					// Intent intent= new Intent(MainActivity.this,
					// PantryActivity.class);
					// startActivity(intent);
					break;
				case "About":
					Intent aboutIntent = new Intent(MainActivity.this,
							InforActivity.class);
					startActivity(aboutIntent);
					break;
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflator = new MenuInflater(this);
		inflator.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onItemSelected(View view, int position, long id, String name) {
		selectedTextView.setText(name);
		ImageView center_image = (ImageView) findViewById(R.id.center_image);
		//
		// int width = getWindowManager().getDefaultDisplay().getWidth();
		// LayoutParams p3=new LayoutParams(width, 100);
		// center_image.setLayoutParams(p3);
		// ExpandableListView expandListView = (ExpandableListView)
		// findViewById(R.id.list);

		switch (position) {
		case 0:
			center_image.setImageResource(R.drawable.main_pantry);
			// expandListView.expandGroup(position);
			// expandListView.collapseGroup(position+1);
			// expandListView.collapseGroup(4);
			break;
		case 1:
			center_image.setImageResource(R.drawable.main_recipe);
			// expandListView.expandGroup(position);
			// expandListView.collapseGroup(position+1);
			// expandListView.collapseGroup(position-1);
			break;
		case 2:
			center_image.setImageResource(R.drawable.main_shoppinglist);
			// expandListView.expandGroup(position);
			// expandListView.collapseGroup(position+1);
			// expandListView.collapseGroup(position-1);
			break;
		case 3:
			center_image.setImageResource(R.drawable.main_setting);
			// expandListView.expandGroup(position);
			// expandListView.collapseGroup(position+1);
			// expandListView.collapseGroup(position-1);
			break;
		case 4:
			center_image.setImageResource(R.drawable.main_icon);
			// expandListView.expandGroup(position);
			// expandListView.collapseGroup(0);
			// expandListView.collapseGroup(position-1);
			break;
		default:
			center_image.setImageResource(R.drawable.main_pantry);
			break;
		}
	}

	@Override
	public void onItemClick(View view, int position, long id, String name) {
		Toast.makeText(getApplicationContext(),
				getResources().getString(R.string.start_app) + " " + name,
				Toast.LENGTH_SHORT).show();
	}

}
