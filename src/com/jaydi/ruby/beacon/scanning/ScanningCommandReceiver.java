package com.jaydi.ruby.beacon.scanning;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScanningCommandReceiver extends BroadcastReceiver {
	public static final String ACTION_STOP_DISCOVERING = "com.jaydi.ruby.intent.action.STOP_DISCOVERING";
	public static final String ACTION_RESTART_SCANNING = "com.jaydi.ruby.intent.action.RESTART_SCANNING";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACTION_STOP_DISCOVERING)) {
			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			if (adapter.isDiscovering())
				adapter.cancelDiscovery();
		} else if (intent.getAction().equals(ACTION_RESTART_SCANNING))
			ScanningManager.turnOnScanning(context);
	}

}