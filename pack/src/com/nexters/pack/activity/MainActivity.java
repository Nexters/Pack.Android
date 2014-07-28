package com.nexters.pack.activity;

import java.util.Stack;

import android.os.Bundle;

import com.nexters.pack.R;
import com.nexters.pack.fragment.BaseSherlockFragment;
import com.nexters.pack.fragment.MainFragment;

public class MainActivity extends BaseSherlockFragmentActivity{
	
	private android.support.v4.app.FragmentManager fm;
	private Stack<BaseSherlockFragment> fragmentStack = new Stack<BaseSherlockFragment>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fm = getSupportFragmentManager();

		
		setContentView(R.layout.activity_main);
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
}
