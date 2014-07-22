package com.nexters.pack.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;
import com.nexters.pack.R;

public class StationActivity extends BaseSherlockActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		FadingActionBarHelper helper = new FadingActionBarHelper()
	        .actionBarBackground(R.color.window_actionbar_background)
	        .headerLayout(R.layout.header)
	        .contentLayout(R.layout.activity_station)
	        .headerOverlayLayout(R.layout.header_overlay);
	    setContentView(helper.createView(this));
	    helper.initActionBar(this);
	    
	    ListView listView = (ListView) findViewById(android.R.id.list);
        ArrayList<String> items = loadItems(R.raw.nyc_sites);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
	}
	/**
     * @return A list of Strings read from the specified resource
     */
    private ArrayList<String> loadItems(int rawResourceId) {
        try {
            ArrayList<String> countries = new ArrayList<String>();
            InputStream inputStream = getResources().openRawResource(rawResourceId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                countries.add(line);
            }
            reader.close();
            return countries;
        } catch (IOException e) {
            return null;
        }
    }

}
