package com.jaydi.ruby;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.appspot.ruby_mine.rubymine.model.Coupon;
import com.appspot.ruby_mine.rubymine.model.CouponCol;
import com.jaydi.ruby.adapters.CouponAdapter;
import com.jaydi.ruby.connection.ResponseHandler;
import com.jaydi.ruby.connection.network.NetworkInter;
import com.jaydi.ruby.models.CouponParcel;
import com.jaydi.ruby.user.LocalUser;

public class MyCouponsActivity extends SubCategoryActivity {
	public static final String EXTRA_USED_COUPON_ID = "com.jaydi.ruby.extras.USED_COUPON_ID";

	private static final int REQUEST_USE_COUPON = 101;

	private List<Coupon> coupons;
	private ListView listCoupons;
	private CouponAdapter couponAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_coupons);

		coupons = new ArrayList<Coupon>();
		listCoupons = (ListView) findViewById(R.id.list_my_coupons_coupons);
		couponAdapter = new CouponAdapter(this, coupons);
		listCoupons.setAdapter(couponAdapter);
		listCoupons.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				goToCoupon(position);
			}

		});

		getCoupons();
	}

	private void getCoupons() {
		NetworkInter.getCoupons(new ResponseHandler<CouponCol>() {

			@Override
			protected void onResponse(CouponCol res) {
				hideProgress();
				if (res == null)
					return;

				coupons.clear();
				if (res.getCoupons() != null)
					coupons.addAll(res.getCoupons());
				refresh();
			}

		}, LocalUser.getUser().getId());
	}

	private void hideProgress() {
		findViewById(R.id.progressbar_my_coupons_loading).setVisibility(View.GONE);
	}

	private void refresh() {
		couponAdapter.notifyDataSetChanged();
	}

	private void goToCoupon(int position) {
		Coupon coupon = (Coupon) couponAdapter.getItem(position);
		Intent intent = new Intent(this, CouponActivity.class);
		intent.putExtra(CouponActivity.EXTRA_COUPON, new CouponParcel(coupon));
		startActivityForResult(intent, REQUEST_USE_COUPON);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_USE_COUPON) {
			long couponId = data.getLongExtra(EXTRA_USED_COUPON_ID, 0);
			removeCoupon(couponId);
		}
	}

	private void removeCoupon(long couponId) {
		List<Coupon> results = new ArrayList<Coupon>();
		for (Coupon coupon : coupons)
			if (!coupon.getId().equals(couponId))
				results.add(coupon);

		coupons.clear();
		coupons.addAll(results);
		refresh();
	}

}
