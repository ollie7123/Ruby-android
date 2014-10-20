package com.jaydi.ruby.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;

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

	public static Bitmap decodeFileScaledDown(String path, int reqWidth, int reqHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(path, options);
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

	public static Bitmap fixOrientation(String imagePath, Bitmap result) {
		int orientation = 1;

		try {
			ExifInterface exif = new ExifInterface(imagePath);
			orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
		} catch (IOException e) {
			e.printStackTrace();
		}

		switch (orientation) {
		case ExifInterface.ORIENTATION_ROTATE_90:
			result = rotate(result, 90);
			break;
		case ExifInterface.ORIENTATION_ROTATE_180:
			result = rotate(result, 180);
			break;
		case ExifInterface.ORIENTATION_ROTATE_270:
			result = rotate(result, 270);
			break;
		}

		return result;
	}

	private static Bitmap rotate(Bitmap result, int rotation) {
		Matrix matrix = new Matrix();
		matrix.setRotate(rotation, (float) result.getWidth() / 2, (float) result.getHeight() / 2);

		return Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), matrix, true);
	}
}
