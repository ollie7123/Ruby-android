package com.jaydi.ruby.location.tracking;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import android.content.Context;
import android.location.Location;
import android.util.Log;

public class TrackingLog {

	public static void logMsg(Context context, String msg) {
		Log.i("TL", msg);
	}

	public static void logLocation(Context context, Location location) {
		String msg = Double.toString(location.getLatitude()) + " " + Double.toString(location.getLongitude()) + " " + new Date().getTime();
		Log.i("TL", msg);
		logFile(context, msg, TrackingConstants.TRACK_FILE);
	}

	public static void logFile(Context context, String msg, String fileName) {
		File file = new File(fileName);

		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
			bw.append(msg);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
