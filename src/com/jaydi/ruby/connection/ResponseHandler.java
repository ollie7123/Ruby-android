package com.jaydi.ruby.connection;

import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.api.client.json.GenericJson;
import com.google.gson.Gson;
import com.jaydi.ruby.utils.ErrorResponse;

public abstract class ResponseHandler<T> extends Handler {
	private Dialog dialog;

	public ResponseHandler() {
		super();
	}

	public ResponseHandler(Dialog dialog) {
		super();
		this.dialog = dialog;
	}

	public ResponseHandler(Looper looper) {
		super(looper);
	}

	public ResponseHandler(Looper looper, Dialog dialog) {
		super(looper);
		this.dialog = dialog;
	}

	@Override
	public void handleMessage(Message m) {
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
		processResponse(m.obj);
	}

	@SuppressWarnings("unchecked")
	private void processResponse(Object res) {
		errorCheck(res);
		onResponse((T) res);
	}

	private void errorCheck(Object res) {
		try {
			String json = ((GenericJson) res).toString();
			BaseModel model = new Gson().fromJson(json, BaseModel.class);

			int resultCode = model.getResultCode();
			if (resultCode != 0)
				ErrorResponse.showErrorResponse(resultCode);

		} catch (ClassCastException e) {
		} catch (NullPointerException e) {
		}
	}

	protected abstract void onResponse(T res);
}
