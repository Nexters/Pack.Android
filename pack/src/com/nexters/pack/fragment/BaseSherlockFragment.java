package com.nexters.pack.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.nexters.pack.activity.BaseSherlockFragmentActivity;

public class BaseSherlockFragment extends SherlockFragment {
	protected ViewGroup rootView = null;
	public LayoutInflater inflater;
	public BaseSherlockFragmentActivity activity;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        inflater = LayoutInflater.from(activity); 
        this.activity = (BaseSherlockFragmentActivity) activity;
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		if(rootView == null) {
			rootView = (ViewGroup) getView(container);
		}
		return rootView;
	}
	
	public View getView(ViewGroup container) {
		return container;
	}
	
	public void showLoading() {
		activity.showLoading();
	}

	public void hideLoading() {
		activity.hideLoading();
	}

	public String getName() {
		StringBuilder nameBuilder = new StringBuilder(getClass().getName());
		Bundle arguments = getArguments();
		if(arguments != null) {
			for(String k : arguments.keySet()) {
				nameBuilder.append(arguments.get(k).toString());
			}
		}
		return nameBuilder.toString();
	}
	public String getTitle() {
		return "";
	}
	
	public boolean onBackPressed() {
		return false;
	}
}
