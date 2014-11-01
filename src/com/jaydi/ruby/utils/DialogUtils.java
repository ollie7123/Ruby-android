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
		progressDlg.setCancelable(false);
		progressDlg.show();
		return progressDlg;
	}

	public static void showNeedRubyDialog(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Need more ruby");
		builder.setPositiveButton("Close", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		});
		builder.show();
	}

	public static void showBuyCouponDialog(Context context, OnClickListener buyCouponListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Are you sure?");
		builder.setPositiveButton("Yes", buyCouponListener);
		builder.setNegativeButton("No", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		});
		builder.show();
	}

	public static void showUseCouponDialog(Context context, OnClickListener useCouponListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Are you sure?");
		builder.setPositiveButton("Yes", useCouponListener);
		builder.setNegativeButton("No", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		});
		builder.show();
	}

	public static void showDeletePairDialog(Context context, OnClickListener deletePairListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Are you sure?");
		builder.setPositiveButton("Yes", deletePairListener);
		builder.setNegativeButton("No", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		});
		builder.show();
	}
}
