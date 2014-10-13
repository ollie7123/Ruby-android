package com.jaydi.ruby.beacon.scanning;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.altbeacon.beacon.Beacon;

import android.content.Context;
import android.util.Log;

public class ScanningLog {

	public static void logBeacon(Context context, Beacon beacon) {
		String msg = beacon.getId2().toString() + " " + beacon.getId3().toString() + " " + new Date().getTime();
		Log.i("BL", msg);
		logFile(context, msg, ScanningConstants.TRACK_FILE);
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
