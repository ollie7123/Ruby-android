package com.jaydi.ruby.threading;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;

import com.jaydi.ruby.application.BitmapCache;
import com.jaydi.ruby.utils.BitmapUtils;

public class ImageDownloadWork extends Work<Bitmap> {
	private String url;

	public ImageDownloadWork(String url) {
		super();
		this.url = url;
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
			Bitmap bitmap = BitmapUtils.decodeStreamScaledDown(inputStream, BitmapUtils.longSide, BitmapUtils.longSide);
			BitmapCache.addBitmapItem(url, bitmap);
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
