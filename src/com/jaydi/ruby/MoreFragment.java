package com.jaydi.ruby;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class MoreFragment extends MainFragment {
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_main_more, container, false);
		setMenuButtons();
		return view;
	}

	private void setMenuButtons() {
		view.findViewById(R.id.button_more_mycoupons).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MyCouponsActivity.class);
				getActivity().startActivity(intent);
			}

		});

	}

	@Override
	public void loadContents(long rubyzoneId) {
	}

}
