package com.jaydi.ruby.utils;

import com.jaydi.ruby.R;
import com.jaydi.ruby.connection.BaseModel;

public class ErrorResponse {

	public static void showErrorResponse(int resultCode) {
		ToastUtils.show(getErrorResponseResourceId(resultCode));
	}

	private static int getErrorResponseResourceId(int resultCode) {
		switch (resultCode) {
		case BaseModel.NO_DATA:
			return R.string.app_name;
		case BaseModel.DUP_EMAIL:
			return R.string.app_name;
		case BaseModel.DUP_PHONE:
			return R.string.app_name;
		case BaseModel.DUP_NAME:
			return R.string.app_name;
		default:
			return R.string.app_name;
		}
	}

}
