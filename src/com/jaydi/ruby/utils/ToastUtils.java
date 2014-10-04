package com.jaydi.ruby.utils;

import android.widget.Toast;

import com.jaydi.ruby.application.RubyApplication;

public class ToastUtils {
	public static void show(String msg) {
		Toast.makeText(RubyApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
	}
}
