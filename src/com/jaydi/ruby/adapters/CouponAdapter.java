package com.jaydi.ruby.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appspot.ruby_mine.rubymine.model.Coupon;
import com.jaydi.ruby.R;
import com.jaydi.ruby.connection.network.NetworkInter;
import com.jaydi.ruby.utils.ResourceUtils;

public class CouponAdapter extends BaseAdapter {
	private Context context;
	private List<Coupon> coupons;

	public CouponAdapter(Context context, List<Coupon> coupons) {
		super();
		this.context = context;
		this.coupons = coupons;
	}

	@Override
	public int getCount() {
		return coupons.size();
	}

	@Override
	public Object getItem(int position) {
		return coupons.get(position);
	}

	@Override
	public long getItemId(int position) {
		return coupons.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Coupon coupon = coupons.get(position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.adapted_coupon_layout, parent, false);

		ImageView imageImage = (ImageView) view.findViewById(R.id.image_adapted_coupon_image);
		NetworkInter.getImage(null, imageImage, ResourceUtils.getImageUrlFromKey(coupon.getImageKey()), 130, 130);

		TextView textName = (TextView) view.findViewById(R.id.text_adapted_coupon_name);
		textName.setText(coupon.getName() + " @" + coupon.getRubymineName());

		return view;
	}

}
