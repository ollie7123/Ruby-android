package com.jaydi.ruby;

import java.util.Date;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appspot.ruby_mine.rubymine.model.Coupon;
import com.appspot.ruby_mine.rubymine.model.Gem;
import com.appspot.ruby_mine.rubymine.model.User;
import com.jaydi.ruby.connection.ResponseHandler;
import com.jaydi.ruby.connection.network.NetworkInter;
import com.jaydi.ruby.models.GemParcel;
import com.jaydi.ruby.user.LocalUser;
import com.jaydi.ruby.utils.DialogUtils;
import com.jaydi.ruby.utils.ResourceUtils;

public class GemActivity extends SubCategoryActivity {
	public static final String EXTRA_GEM = "com.jaydi.ruby.extras.GEM";

	private Gem gem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gem);

		if (savedInstanceState == null) {
			GemParcel gemParcel = getIntent().getParcelableExtra(EXTRA_GEM);
			gem = gemParcel.toGem();
		}

		showGem();
	}

	private void showGem() {
		ImageView imageImage = (ImageView) findViewById(R.id.image_gem_image);
		NetworkInter.getImage(null, imageImage, ResourceUtils.getImageUrlFromKey(gem.getImageKey()), 270, 360);

		TextView textName = (TextView) findViewById(R.id.text_gem_name);
		textName.setText(gem.getName() + " @" + gem.getRubymineName());
	}

	public void buyGem(View view) {
		if (LocalUser.getUser().getRuby() >= gem.getValue())
			DialogUtils.showBuyCouponDialog(this, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					redeemCoupon();
				}

			});
		else
			DialogUtils.showNeedRubyDialog(this);
	}

	private void redeemCoupon() {
		Coupon coupon = new Coupon();
		coupon.setUserId(LocalUser.getUser().getId());
		coupon.setGemId(gem.getId());
		coupon.setName(gem.getName());
		coupon.setRubymineName(gem.getRubymineName());
		coupon.setImageKey(gem.getImageKey());
		coupon.setDesc(gem.getDesc());
		coupon.setExpirationDate(getExpirationDate());

		NetworkInter.redeemCoupon(new ResponseHandler<User>(DialogUtils.showWaitingDialog(this)) {

			@Override
			protected void onResponse(User res) {
				if (res == null)
					return;

				LocalUser.setUser(res);
				Intent intent = new Intent(GemActivity.this, MyCouponsActivity.class);
				startActivity(intent);
				finish();
			}

		}, coupon);
	}

	private long getExpirationDate() {
		long et = new Date().getTime() + 1000 * 60 * 60 * 24 * 30;
		return et;
	}

}
