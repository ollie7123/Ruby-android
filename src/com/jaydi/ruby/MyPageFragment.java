package com.jaydi.ruby;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyPageFragment extends MainFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_my_page, container, false);
		
		return view;
	}

	@Override
	public void loadContents(long rubyzoneId) {
		// TODO Auto-generated method stub

	}

}
