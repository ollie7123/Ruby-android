package com.jaydi.ruby;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appspot.ruby_mine.rubymine.model.Rubymine;
import com.google.gson.Gson;
import com.jaydi.ruby.connection.network.NetworkInter;
import com.jaydi.ruby.utils.ResourceUtils;

public class RubymineFragment extends Fragment {
	private Rubymine rubymine;

	public void setRubymine(Rubymine rubymine) {
		this.rubymine = rubymine;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_rubymine, container, false);
		LinearLayout wrapper = (LinearLayout) view.findViewById(R.id.linear_rubymine_wrapper);

		String jsonContents = rubymine.getContents();
		List<String> contents = new Gson().fromJson(jsonContents, ArrayList.class);

		if (contents != null)
			for (int i = 0; i < contents.size(); i++)
				if (i % 2 == 0)
					wrapper.addView(getImageView(contents.get(i)));
				else
					wrapper.addView(getTextView(contents.get(i)));

		return view;
	}

	private ImageView getImageView(String imageKey) {
		ImageView imageView = new ImageView(getActivity());
		imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ResourceUtils.convertDpToPixel(360)));
		imageView.setScaleType(ScaleType.CENTER_CROP);
		NetworkInter.getImage(imageView, ResourceUtils.getImageUrlFromKey(imageKey), 720, 720);
		
		return imageView;
	}

	private TextView getTextView(String text) {
		int sideMargin = ResourceUtils.convertDpToPixel(30);
		int verMargin = ResourceUtils.convertDpToPixel(10);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(sideMargin, verMargin, sideMargin, verMargin);

		TextView textView = new TextView(getActivity());
		textView.setLayoutParams(params);
		textView.setGravity(Gravity.CENTER);
		textView.setText(text);
		
		return textView;
	}
}
