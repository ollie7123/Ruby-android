package com.jaydi.ruby.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class BitmapUtils {
	public static int longSide = 720;
	public static int middleSide = 540;
	public static int shortSide = 360;

	public static Bitmap decodeStreamScaledDown(InputStream stream, int reqWidth, int reqHeight) {
		byte[] data = new byte[0];
		try {
			data = IOUtils.toByteArray(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, options);

		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeByteArray(data, 0, data.length, options);
	}

	private static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
		final int width = options.outWidth;
		final int height = options.outHeight;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			inSampleSize = (heightRatio < widthRatio) ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
}
