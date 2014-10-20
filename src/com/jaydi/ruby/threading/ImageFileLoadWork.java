package com.jaydi.ruby.threading;

import java.io.IOException;

import android.graphics.Bitmap;

import com.jaydi.ruby.utils.BitmapUtils;

public class ImageFileLoadWork extends Work<Bitmap> {
	private String path;
	private int width;
	private int height;

	public ImageFileLoadWork(String path, int width, int height) {
		super();
		this.path = path;
		this.width = width;
		this.height = height;
	}

	@Override
	public Bitmap work() throws IOException {
		Bitmap bitmap = BitmapUtils.decodeFileScaledDown(path, width, height);
		return BitmapUtils.fixOrientation(path, bitmap);
	}
}
