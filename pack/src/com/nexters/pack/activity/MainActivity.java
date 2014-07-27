package com.nexters.pack.activity;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;
import com.nexters.pack.R;
import com.nexters.pack.util.MainItem;

public class MainActivity extends BaseSherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initResources();
	}
	
	private void initResources(){
		FadingActionBarHelper helper = new FadingActionBarHelper()
        .actionBarBackground(R.color.window_actionbar_background)
        .headerLayout(R.layout.header)
        .contentLayout(R.layout.activity_main)
        .headerOverlayLayout(R.layout.header_overlay);
		
	    setContentView(helper.createView(this));
	    helper.initActionBar(this);
	    
	    ListView listView = (ListView) findViewById(R.id.main_list);
	    ArrayList<MainItem> items = setTempItem();//임시 아이템
	    MainItemAdapter adapter = new MainItemAdapter(this, R.layout.main_item, items);
	    listView.setAdapter(adapter);
	}
	
	private ArrayList<MainItem> setTempItem() {
		ArrayList<MainItem> item = new ArrayList<MainItem>();
		item.add(new MainItem("첫줄", R.drawable.abs__ic_menu_share_holo_dark));
		item.add(new MainItem("두줄", R.drawable.abs__ab_bottom_solid_inverse_holo));
		return item;
	}
	
    private class MainItemAdapter extends ArrayAdapter<MainItem> {
    	 
		private ArrayList<MainItem> items;
 
        public MainItemAdapter(Context context, int textViewResourceId, ArrayList<MainItem> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.main_item, null);
                }
                MainItem mainItem = items.get(position);
                if (mainItem != null) {
                        TextView textView = (TextView) v.findViewById(R.id.main_item_tv);
                        ImageView imageView = (ImageView) v.findViewById(R.id.main_item_iv);
                        if (textView != null){
                            textView.setText(mainItem.getText());                            
                        }
                        if(imageView != null){
                                imageView.setBackgroundResource(mainItem.getImage());
                        }
                }
                return v;
        }
}
}
