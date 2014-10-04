package com.jaydi.ruby.location.tracking;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

public class TrackingLog {

	public static void logMsg(Context context, String msg) {
		Log.i("TL-M", msg);
	}

	public static void logLocation(Context context, String msg) {
		Log.i("TL-L", msg);
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
