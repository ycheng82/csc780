package ezgrocerylist.activity;

import com.ezgrocerylist.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 
public class PlanetFragment extends Fragment {
    public static final String ARG_PLANET_NUMBER = "1";// ����,��������
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // ������
        return inflater.inflate(R.layout.fragment_planet, container, false);
    }
 
}
