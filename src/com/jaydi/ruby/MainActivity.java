package com.jaydi.ruby;

import android.os.Bundle;

import com.jaydi.ruby.application.RubyApplication;
import com.jaydi.ruby.beacon.scanning.ScanningManager;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initBeaconScanning();
	}

	private void initBeaconScanning() {
		ScanningManager.turnOnScanning(RubyApplication.getInstance());
	}

}
