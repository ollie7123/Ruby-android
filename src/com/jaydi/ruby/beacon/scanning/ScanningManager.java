package com.jaydi.ruby.beacon.scanning;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class ScanningManager {
	private static final String PREF_SCANNING_MANAGER = "prefScanningManager";
	private static final String KEY_PRE_STATE = "bluetoothPreState";

	// beacon scanning listener
	private static ScanningListener scanningListener;

	public static void turnOnScanning(Context context) {
		turnOnBluetooth(context);
		initScanningListener(context);
	}

	private static void turnOnBluetooth(Context context) {
		// turn on bluetooth and mark original state
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (!adapter.isEnabled()) {
			adapter.enable();
			savePreState(context, false);
		} else
			savePreState(context, true);
	}

	private static void initScanningListener(Context context) {
		if (scanningListener == null) {
			scanningListener = new ScanningListener();
			scanningListener.init(context.getApplicationContext());
		}
	}

	public static void turnOffScanning(Context context) {
		if (!getPreState(context))
			turnOffBluetooth();
		termScanningListener();
	}

	private static void turnOffBluetooth() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter.isEnabled())
			adapter.disable();
	}

	private static void termScanningListener() {
		if (scanningListener != null) {
			scanningListener.term();
			scanningListener = null;
		}
	}

	// for crash protection
	public static void forceFlush(Context context) {
		termScanningListener();

		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter.startDiscovery()) {
			Intent intent = new Intent(context, ScanningCommandReceiver.class);
			intent.setAction(ScanningCommandReceiver.ACTION_STOP_FLUSHING);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 10942, intent, PendingIntent.FLAG_UPDATE_CURRENT);

			AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000 * 5), pendingIntent);
		}
	}

	// for crash protection
	public static void finishFlush(Context context) {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter.isDiscovering())
			adapter.cancelDiscovery();
		if (!adapter.isEnabled())
			adapter.enable();

		initScanningListener(context);
	}

	// for crash recovery
	public static void startRecovery(Context context) {
		termScanningListener();

		Intent intent = new Intent(context, ScanningCommandReceiver.class);
		intent.setAction(ScanningCommandReceiver.ACTION_RESTART_DELAYED);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 10900, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000 * 5), pendingIntent);
	}

	// for crash recovery
	public static void restartDelayed(Context context) {
		// first turn off bluetooth
		turnOffBluetooth();

		// restart scanning 3 seconds later
		Intent intent = new Intent(context, ScanningCommandReceiver.class);
		intent.setAction(ScanningCommandReceiver.ACTION_START_SCANNING);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 11780, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000 * 5), pendingIntent);
	}

	private static boolean getPreState(Context context) {
		SharedPreferences pref = getPref(context);
		return pref.getBoolean(KEY_PRE_STATE, false);
	}

	private static void savePreState(Context context, boolean state) {
		Log.i("SM", "pre state: " + state);
		SharedPreferences pref = getPref(context);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(KEY_PRE_STATE, state);
		editor.commit();
	}

	private static SharedPreferences getPref(Context context) {
		return context.getSharedPreferences(PREF_SCANNING_MANAGER, Context.MODE_PRIVATE);
	}

}
