package com.nexters.pack.activity;

import java.util.Stack;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.nexters.pack.R;
import com.nexters.pack.adapter.MenuListAdapter;
import com.nexters.pack.fragment.BaseSherlockFragment;
import com.nexters.pack.fragment.MainFragment;

public class MainActivity extends BaseSherlockFragmentActivity implements SearchView.OnQueryTextListener, SearchView.OnSuggestionListener{
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private MenuListAdapter menuAdapter;
	private ActionBarDrawerToggle drawerToggle;
	private android.support.v4.app.FragmentManager fm;
	private Stack<BaseSherlockFragment> fragmentStack = new Stack<BaseSherlockFragment>();
	
	String[] title;
	String[] subtitle;
	int[] icon;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.listview_drawer);
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		
		// Generate title
		title = new String[] { "Title Fragment 1", "Title Fragment 2",
				"Title Fragment 3" };

		// Generate subtitle
		subtitle = new String[] { "Subtitle Fragment 1", "Subtitle Fragment 2",
				"Subtitle Fragment 3" };

		// Generate icon
		icon = new int[] { R.drawable.action_about, R.drawable.action_settings,
				R.drawable.collections_cloud };
				
		menuAdapter = new MenuListAdapter(MainActivity.this, title, subtitle,
				icon);
		drawerList.setAdapter(menuAdapter);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				// TODO Auto-generated method stub
				super.onDrawerClosed(view);
			}

			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
				// Set the title on the action when drawer open
				//getSupportActionBar().setTitle(mDrawerTitle);
				super.onDrawerOpened(drawerView);
			}
		};
		drawerLayout.setDrawerListener(drawerToggle);

		
		fm = getSupportFragmentManager();
		
		addFragment(new MainFragment());
	}
	
	public void replaceFragment(BaseSherlockFragment fragment) {
		fragmentStack.pop();
		fm.popBackStackImmediate();
		addFragment(fragment);
	}
	
	public void addFragment(BaseSherlockFragment fragment) {
		for(int i = 0; i < fm.getBackStackEntryCount(); i++) {
			String name = fm.getBackStackEntryAt(i).getName();
			if(fragment.getName().equals(name)) {
				int end = fragmentStack.size() - i - 1;
				for(int j = 0; j < end; j++) {
					fragmentStack.pop();
				}
				fm.popBackStack(name, 0);
				
				return;
			}
		}
		fragmentStack.add(fragment);
		 
		fm.beginTransaction()
		.addToBackStack(fragment.getName())
		.replace(R.id.content_frame, fragment)
		.commit();
	}
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		drawerToggle.onConfigurationChanged(newConfig);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getItemId() == android.R.id.home) {

			if (drawerLayout.isDrawerOpen(drawerList)) {
				drawerLayout.closeDrawer(drawerList);
			} else {
				drawerLayout.openDrawer(drawerList);
			}
		}
		showShortToast("Click : " + item.getTitle());
		
		return super.onOptionsItemSelected(item);
	}
	
	private SuggestionsAdapter suggestionsAdapter;
	private static final String[] COLUMNS = {
        BaseColumns._ID,
        SearchManager.SUGGEST_COLUMN_TEXT_1,
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//Create the search view
        SearchView searchView = new SearchView(getSupportActionBar().getThemedContext());
        searchView.setQueryHint("Search for countries");
        searchView.setOnQueryTextListener(this);
        searchView.setOnSuggestionListener(this);

        if (suggestionsAdapter == null) {
            MatrixCursor cursor = new MatrixCursor(COLUMNS);
            cursor.addRow(new String[]{"1", "'Murica"});
            cursor.addRow(new String[]{"2", "Canada"});
            cursor.addRow(new String[]{"3", "Denmark"});
            suggestionsAdapter = new SuggestionsAdapter(getSupportActionBar().getThemedContext(), cursor);
        }

        searchView.setSuggestionsAdapter(suggestionsAdapter);
        
        menu.add("Search")
                .setIcon(R.drawable.search_icon)
                .setActionView(searchView)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                //.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        menu.add("Setting")
        		.setIcon(R.drawable.set_up_icon)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }
	
	private class SuggestionsAdapter extends CursorAdapter {

        public SuggestionsAdapter(Context context, Cursor c) {
            super(context, c, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView tv = (TextView) view;
            final int textIndex = cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1);
            tv.setText(cursor.getString(textIndex));
        }
    }

	@Override
	public boolean onSuggestionSelect(int position) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSuggestionClick(int position) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}
}
