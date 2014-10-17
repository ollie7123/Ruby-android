package com.jaydi.ruby.connection;

public class BaseModel {
	public static final int NO_DATA = 1;
	public static final int DUP_EMAIL = 101;
	public static final int DUP_PHONE = 102;
	public static final int DUP_NAME = 103;
	
	private int resultCode;

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

}
