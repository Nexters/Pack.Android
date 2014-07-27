package com.nexters.pack.activity;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.nexters.pack.R;
import com.nexters.pack.util.MainItem;

public class ListFragment extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
		
		View view = inflater.inflate(R.layout.fragment_list, container, false);
		ListView listView = (ListView) view.findViewById(R.id.list);
	    ArrayList<MainItem> items = setTempItem();//임시
	    MainItemAdapter adapter = new MainItemAdapter(view.getContext(), R.layout.main_item, items);
	    listView.setAdapter(adapter);
		
        return view;
    }
	
	private ArrayList<MainItem> setTempItem() {
		ArrayList<MainItem> item = new ArrayList<MainItem>();
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("호호", R.drawable.abs__ic_menu_share_holo_dark));
		return item;
	}

	private class MainItemAdapter extends ArrayAdapter<MainItem> {

		private ArrayList<MainItem> items;

		public MainItemAdapter(Context context, int textViewResourceId,
				ArrayList<MainItem> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.main_item, null);
			}
			MainItem mainItem = items.get(position);
			if (mainItem != null) {
				TextView textView = (TextView) v
						.findViewById(R.id.main_item_tv);
				ImageView imageView = (ImageView) v
						.findViewById(R.id.main_item_iv);
				if (textView != null) {
					textView.setText(mainItem.getText());
				}
				if (imageView != null) {
					imageView.setImageResource(mainItem.getImage());
				}
			}
			return v;
		}
	}

}
