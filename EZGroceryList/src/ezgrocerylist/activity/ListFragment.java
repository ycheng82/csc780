package ezgrocerylist.activity;

import java.util.HashSet;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;
import com.ezgrocerylist.R;

public class ListFragment extends Fragment {
	View rootView;
	ExpandableListView lv;
	private String[] groups;
	private String[][] children;
	private HashSet<String> listNames;
	private String[][] listContents;
	OnListSelectedListener mCallback;

	public ListFragment() {

	}

	public interface OnListSelectedListener {
		public void onListSelected(String contents, String listName);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnListSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnListSelectedListener");
		}
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		groups = listNames.toArray(new String[listNames.size()]);

		children = listContents;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_list, container, false);

		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		lv = (ExpandableListView) view.findViewById(R.id.expListView);
		final ExpandableListAdapter adapter = new ExpandableListAdapter(groups,
				children);
		lv.setAdapter(adapter);
		lv.setGroupIndicator(null);
		lv.setOnGroupClickListener(new OnGroupClickListener() {
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				String listName = adapter.getGroup(groupPosition).toString();
				String contents = "";
				for (int i = 0; i < children[groupPosition].length; i++) {
					contents += adapter.getChild(groupPosition, i).toString()
							+ "\n";
				}
				mCallback.onListSelected(contents, listName);

				return false;
			}
		});
	}

	public HashSet<String> getListNames() {
		return listNames;
	}

	public void setListNames(HashSet<String> listNames) {
		this.listNames = listNames;
	}

	public String[][] getListContents() {
		return listContents;
	}

	public void setListContents(String[][] listContents) {
		this.listContents = listContents;
	}

	public class ExpandableListAdapter extends BaseExpandableListAdapter {

		private final LayoutInflater inf;
		private String[] groups;
		private String[][] children;

		public ExpandableListAdapter(String[] groups, String[][] children) {
			this.groups = groups;
			this.children = children;
			inf = LayoutInflater.from(getActivity());
		}

		@Override
		public int getGroupCount() {
			return groups.length;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return children[groupPosition].length;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groups[groupPosition];
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return children[groupPosition][childPosition];
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getChildView(int groupPosition, final int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if (convertView == null) {
				convertView = inf.inflate(R.layout.list_item, parent, false);
				holder = new ViewHolder();

				holder.text = (TextView) convertView
						.findViewById(R.id.lblListItem);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.text.setText(getChild(groupPosition, childPosition)
					.toString());

			return convertView;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = inf.inflate(R.layout.list_group, parent, false);

				holder = new ViewHolder();
				holder.text = (TextView) convertView
						.findViewById(R.id.lblListHeader);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.text.setText(getGroup(groupPosition).toString());

			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {

			return true;
		}

		private class ViewHolder {
			TextView text;
		}
	}
}
