package com.jaydi.ruby;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appspot.ruby_mine.rubymine.model.User;
import com.jaydi.ruby.connection.network.NetworkInter;
import com.jaydi.ruby.user.LocalUser;
import com.jaydi.ruby.utils.ResourceUtils;

public class MyPageFragment extends MainFragment {
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_main_my_page, container, false);
		return view;
	}

	@Override
	public void loadContents(long rubyzoneId) {
		User user = LocalUser.getUser();

		ImageView imageImage = (ImageView) view.findViewById(R.id.image_my_page_image);
		NetworkInter.getImage(null, imageImage, ResourceUtils.getImageUrlFromKey(user.getImageKey()), 200, 200);

		TextView textName = (TextView) view.findViewById(R.id.text_my_page_name);
		textName.setText(user.getName());

		TextView textLevel = (TextView) view.findViewById(R.id.text_my_page_level);
		textLevel.setText("Level. " + user.getLevel());
	}

}
