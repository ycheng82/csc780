package ezgrocerylist.activity;

import java.util.ArrayList;
import java.util.HashSet;

import com.ezgrocerylist.R;

import ezgrocerylist.sql.DatabaseHandler;
import ezgrocerylist.sql.Item;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class NameFragment extends Fragment {
	private HashSet<String> listNames;

	public NameFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_name, container,
				false);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);

		// Set a linearLayout to add buttons
		LinearLayout linearLayout = new LinearLayout(getActivity());
		// Set the layout full width, full height
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		linearLayout.setLayoutParams(params);
		linearLayout.setOrientation(LinearLayout.VERTICAL); // or VERTICAL
		for (String value : listNames) {
			final Button button = new Button(getActivity());
			// set button label
			button.setText(value);
			// set onclicklistener
			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// replace fragment
					ListFragment fragment = new ListFragment();
					((ListFragment) fragment).setListNames(listNames);
					((ListFragment) fragment).setListContents(getContents(button.getText().toString()));
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.frame_container, fragment).commit();
				}

			});
			// For buttons visibility, you must set the layout params in order
			// to give some width and height:
			LayoutParams paramsB = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			button.setLayoutParams(paramsB);
			linearLayout.addView(button);

		}
		ViewGroup viewGroup = (ViewGroup) view;
		viewGroup.addView(linearLayout);

	}

	public HashSet<String> getListNames() {
		return listNames;
	}

	public void setListNames(HashSet<String> listNames) {
		this.listNames = listNames;
	}

	private String[][] getContents(String name) {
		DatabaseHandler db = new DatabaseHandler(getActivity());
		
		// retrieve items from database
		String listText = "";
		ArrayList<Item> items = new ArrayList<Item>();
		items = db.getItems(name);
		String[][] listContents = null;

		// split items into category
		ArrayList<String> cats = new ArrayList<String>();
		HashSet<String> uCats = null;
		if (items.size() != 0) {
			for (int i = 0; i < items.size(); i++) {
				cats.add(items.get(i).getItemCategory());
			}
			uCats = new HashSet<>(cats);
			listContents = new String[uCats.size()][]; 
			// Log.d("showList","#category: " + uCats.size());
		}

		// add view for these items if they are not null
		if (uCats != null) {
			int k=0;
			for (String value : uCats) {
				// listText ="";
				ArrayList<String> valueDetail = new ArrayList<String>();
				for (int j = 0; j < items.size(); j++) {

					if (items.get(j).getItemCategory().equals(value)) {
						// Log.d("category 1 ",items.get(j).getItemCategory());
						valueDetail.add( items.get(j).getItemName() + "		"
								+ items.get(j).getItemQuantity() + "		"
								+ items.get(j).getItemUnit() + "\n");
					}
				}
				for (int l = 0; l<valueDetail.size();l++){
					listContents[k][l]= valueDetail.get(l);
				}
				
				k++;
			}
		}
		
		return listContents;

	}

}
