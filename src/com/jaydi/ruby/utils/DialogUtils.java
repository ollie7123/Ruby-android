package com.jaydi.ruby.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.jaydi.ruby.R;

public class DialogUtils {
	public static void networkAlert(final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Network Error");
		builder.setPositiveButton("Close", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				((Activity) context).finish();
			}

		});

		builder.show();
	}

	public static Dialog showWaitingDialog(Context context) {
		ProgressDialog progressDlg = new ProgressDialog(context);
		progressDlg.setMessage(ResourceUtils.getString(R.string.waiting_message));
		progressDlg.setCancelable(true);
		progressDlg.show();
		return progressDlg;
	}
}
