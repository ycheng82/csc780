package ezgrocerylist.activity;

import java.util.ArrayList;
import java.util.HashSet;

import com.ezgrocerylist.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class AllListFragment extends Fragment {
	private HashSet<String> listNames;
	private View rootView;
	private Activity activity;

	public AllListFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_pantry_list, container,
				false);

		// add ingredients (name, quantity, unit) to view
		ArrayList<String> newList = new ArrayList<String>(listNames);
		for (int i = 0; i < newList.size(); i++) {
			LinearLayout llIngredients = (LinearLayout) rootView
					.findViewById(R.id.layout_lists);
			LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			lparams.setMargins(50, 0, 0, 0);
			final Button btn = new Button(this.getActivity());
			// btn.setBackgroundColor(getResources().getColor(R.color.ingredient_btn));
			btn.getBackground().setAlpha(30);
			btn.setLayoutParams(lparams);
			btn.setText(newList.get(i));
			btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					/*Fragment fragment = new DetailFragment();
					((DetailFragment) fragment).setListName(btn.getText()
							.toString());
					Log.e("lisname", btn.getText().toString());
					try {
						((OnAllListSelectedListener) activity).onListPicked(btn
								.getText().toString());
					} catch (ClassCastException cce) {

					}*/
					Fragment fragment = new DetailListFragment();
					((DetailListFragment) fragment).setListName(btn.getText()
							.toString());
					try {
						((OnAllListSelectedListener) activity).onListPicked(btn
								.getText().toString());
					} catch (ClassCastException cce) {

					}
					
					// ((AllListFragment)
					// fragment).setListContents(getListContents());
					FragmentManager fragmentManager = getFragmentManager();
					FragmentTransaction transaction = fragmentManager
							.beginTransaction();
					transaction.replace(
							((ViewGroup) getView().getParent()).getId(),
							fragment);
					transaction.addToBackStack(fragment.getClass().getName());
					transaction.commit();
				}
			});

			llIngredients.addView(btn);
		}
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	public HashSet<String> getListNames() {
		return listNames;
	}

	public void setListNames(HashSet<String> listNames) {
		this.listNames = listNames;
	}

	public interface OnAllListSelectedListener {
		public void onListPicked(String listName);
	}

}
