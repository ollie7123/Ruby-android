package com.jaydi.ruby;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appspot.ruby_mine.rubymine.model.Coupon;
import com.jaydi.ruby.connection.ResponseHandler;
import com.jaydi.ruby.connection.network.NetworkInter;
import com.jaydi.ruby.models.CouponParcel;
import com.jaydi.ruby.utils.DialogUtils;
import com.jaydi.ruby.utils.ResourceUtils;

public class CouponActivity extends SubCategoryActivity {
	public static final String EXTRA_COUPON = "com.jaydi.ruby.extras.COUPON";

	private Coupon coupon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coupon);

		if (savedInstanceState == null) {
			CouponParcel couponParcel = getIntent().getParcelableExtra(EXTRA_COUPON);
			coupon = couponParcel.toCoupon();
		}

		showCoupon();
	}

	private void showCoupon() {
		ImageView imageImage = (ImageView) findViewById(R.id.image_coupon_image);
		NetworkInter.getImage(null, imageImage, ResourceUtils.getImageUrlFromKey(coupon.getImageKey()), 270, 360);

		TextView textName = (TextView) findViewById(R.id.text_coupon_name);
		textName.setText(coupon.getName() + " @" + coupon.getRubymineName());
	}

	public void useCoupon(View view) {
		DialogUtils.showUseCouponDialog(this, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				deleteCoupon();
			}

		});
	}

	private void deleteCoupon() {
		NetworkInter.deleteCoupon(new ResponseHandler<Void>(DialogUtils.showWaitingDialog(this)) {

			@Override
			protected void onResponse(Void res) {
				Intent returnIntent = new Intent();
				returnIntent.putExtra(MyCouponsActivity.EXTRA_USED_COUPON_ID , coupon.getId());
				setResult(RESULT_OK, returnIntent);
				finish();
			}

		}, coupon.getId());
	}

}
