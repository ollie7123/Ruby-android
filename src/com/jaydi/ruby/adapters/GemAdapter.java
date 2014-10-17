package com.jaydi.ruby.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appspot.ruby_mine.rubymine.model.Gem;
import com.jaydi.ruby.R;
import com.jaydi.ruby.connection.network.NetworkInter;
import com.jaydi.ruby.utils.ResourceUtils;

public class GemAdapter extends BaseAdapter {
	private Context context;
	private List<Gem> gems;

	public GemAdapter(Context context, List<Gem> gems) {
		super();
		this.context = context;
		this.gems = gems;
	}

	@Override
	public int getCount() {
		return gems.size();
	}

	@Override
	public Object getItem(int position) {
		return gems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return gems.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Gem gem = (Gem) getItem(position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.adapted_gem_layout, parent, false);

		ImageView image = (ImageView) view.findViewById(R.id.image_adapted_gem_image);
		NetworkInter.getImage(image, ResourceUtils.getImageUrlFromKey(gem.getImageKey()), 270, 360);

		TextView name = (TextView) view.findViewById(R.id.text_adapted_gem_name);
		name.setText(gem.getRubymineName() + "\n" + gem.getName());

		TextView value = (TextView) view.findViewById(R.id.text_adapted_gem_value);
		value.setText("" + gem.getValue());

		return view;
	}

}
