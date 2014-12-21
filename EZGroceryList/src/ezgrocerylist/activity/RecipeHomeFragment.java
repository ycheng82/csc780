package ezgrocerylist.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.ezgrocerylist.R;

public class RecipeHomeFragment extends Fragment {
		View rootView;
		TextView tv;
		String screenName;
		public RecipeHomeFragment(String screenName) {
			this.screenName = screenName;
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
			tv = (TextView) view.findViewById(R.id.tv_pantry_home);
			tv.setText("This is My Recipe List" + "\n"+ 
			"drag menu from left side to start opening or adding a new recipe" + "\n"+
			"After, you can add ingredients, steps, images and description");
			Animation animation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.text_anim);
			tv.startAnimation(animation); 
			//Toast.makeText(getActivity(),"This is My "+ screenName+" List" + "\n"+ "Click the icon above to open a list",Toast.LENGTH_SHORT).show();
			  
		}
}
