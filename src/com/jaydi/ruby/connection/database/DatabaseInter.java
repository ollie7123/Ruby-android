package com.jaydi.ruby.connection.database;

import java.io.IOException;

import android.content.Context;
import android.os.Handler;

import com.jaydi.ruby.models.RubyZone;
import com.jaydi.ruby.threading.ThreadManager;
import com.jaydi.ruby.threading.Work;

public class DatabaseInter {

	public static <T> void addRubyZone(final Context context, final RubyZone rubyZone) {
		ThreadManager.execute(new Work<T>() {

			@Override
			public T work() throws IOException {
				DatabaseHandler dbHandler = new DatabaseHandler(context);
				dbHandler.addSpot(rubyZone);
				return null;
			}

		}, null);
	}

	// public static <T> void getLink(final Context context, Handler handler, final String url) {
	// ThreadManager.execute(new Work<T>() {
	//
	// @SuppressWarnings("unchecked")
	// @Override
	// public T work() throws IOException {
	// DatabaseHandler dbHandler = new DatabaseHandler(context);
	// return (T) dbHandler.getLink(url);
	// }
	//
	// }, handler);
	// }

	public static <T> void getRubyZones(final Context context, Handler handler) {
		ThreadManager.execute(new Work<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T work() throws IOException {
				DatabaseHandler dbHandler = new DatabaseHandler(context);
				return (T) dbHandler.getSpots();
			}

		}, handler);
	}

	public static <T> void deleteRubyZone(final Context context, Handler handler, final long id) {
		ThreadManager.execute(new Work<T>() {

			@Override
			public T work() throws IOException {
				DatabaseHandler dbHandler = new DatabaseHandler(context);
				dbHandler.deleteSpot(id);
				return null;
			}

		}, handler);
	}

}
