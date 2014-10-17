package com.jaydi.ruby.threading;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;

import com.jaydi.ruby.application.Cache;
import com.jaydi.ruby.utils.BitmapUtils;

public class ImageLoadWork extends Work<Bitmap> {
	private String url;
	private int width = BitmapUtils.longSide;
	private int height = BitmapUtils.longSide;

	public ImageLoadWork(String url, int width, int height) {
		super();
		this.url = url;
		if (width != 0)
			this.width = width;
		if (height != 0)
			this.height = height;
	}

	@Override
	public Bitmap work() throws IOException {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet request;
		HttpResponse response;
		try {
			request = new HttpGet(url);
			response = client.execute(request);
		} catch (IllegalArgumentException e) {
			return null;
		} catch (IllegalStateException e) {
			return null;
		}

		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("response not ok: " + response.getStatusLine().getStatusCode());
			return null;
		}

		HttpEntity entity = response.getEntity();
		if (entity == null) {
			System.out.println("entity null");
			return null;
		}

		InputStream inputStream = null;

		try {
			// getting contents from the stream
			inputStream = entity.getContent();
			Bitmap bitmap = BitmapUtils.decodeStreamScaledDown(inputStream, width, height);
			Cache.addBitmapItem(url, bitmap);

			return bitmap;
		} catch (Exception e) {
			System.out.println("exception on input stream");
			return null;
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			entity.consumeContent();
		}
	}
}
