package com.jaydi.ruby.connection;

import android.graphics.Bitmap;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.jaydi.ruby.R;
import com.jaydi.ruby.application.RubyApplication;

public class BitmapResponseHandler extends ResponseHandler<Bitmap> {
	private Handler secondHandler;
	private ImageView imageView;

	public BitmapResponseHandler(Handler secondHandler, ImageView imageView) {
		super();
		this.secondHandler = secondHandler;
		this.imageView = imageView;
	}

	@Override
	protected void onResponse(Bitmap res) {
		if (secondHandler != null)
			secondHandler.sendEmptyMessage(0);
		if (imageView == null || res == null)
			return;

		Animation fadeIn = AnimationUtils.loadAnimation(RubyApplication.getInstance(), R.anim.fade_in);
		imageView.startAnimation(fadeIn);
		imageView.setImageBitmap(res);
	}

}
