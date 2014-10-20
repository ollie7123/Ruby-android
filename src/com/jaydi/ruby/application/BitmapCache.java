package com.jaydi.ruby.application;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class BitmapCache {
	public static final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	public static final int cacheSize = maxMemory / 5;
	public static LruCache<String, Bitmap> bitmapCache = new LruCache<String, Bitmap>(cacheSize) {

		@Override
		protected int sizeOf(String key, Bitmap bitmap) {
			return bitmap.getByteCount() / 1024;
		}

	};

	public static void addBitmapItem(String key, Bitmap bitmap) {
		if (getBitmapItem(key) == null)
			bitmapCache.put(key, bitmap);
	}

	public static Bitmap getBitmapItem(String key) {
		if (key != null && key.trim().length() != 0)
			return bitmapCache.get(key);
		else
			return null;
	}
	
}