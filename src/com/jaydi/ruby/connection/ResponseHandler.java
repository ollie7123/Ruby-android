package com.jaydi.ruby.connection;

import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

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

	@SuppressWarnings("unchecked")
	@Override
	public void handleMessage(Message m) {
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
		onResponse((T) m.obj);
	}

	protected abstract void onResponse(T res);
}
