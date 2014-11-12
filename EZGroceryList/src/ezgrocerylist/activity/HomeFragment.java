package ezgrocerylist.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ezgrocerylist.R;

public class HomeFragment extends Fragment {
		View rootView;
		TextView tv;
		public HomeFragment() {

		}
		
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_home, container, false);
			return rootView;
		}
		
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
			//tv = (TextView) view.findViewById(R.id.tv_pantry_home);
			//tv.setText("This is My Pantry List" + "\n"+ "Click the icon above to open a list");
			Toast.makeText(getActivity(),"This is My Pantry List" + "\n"+ "Click the icon above to open a list",Toast.LENGTH_LONG).show();
			  
		}
}
