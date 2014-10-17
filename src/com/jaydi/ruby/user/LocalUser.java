package com.jaydi.ruby.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.appspot.ruby_mine.rubymine.model.User;
import com.jaydi.ruby.application.RubyApplication;

public class LocalUser {
	private static final String PREF_USER = "prefUser";
	private static final String PROPERTY_ID = "propertyId";
	private static final String PROPERTY_NAME = "propertyName";
	private static final String PROPERTY_LEVEL = "propertyLevel";
	private static final String PROPERTY_RUBY = "propertyRuby";

	private static User user;

	public static void setUser(User user) {
		LocalUser.user = user;
		saveUser(user);
	}

	public static User getUser() {
		if (user == null)
			user = loadUser();
		return user;
	}

	private static void saveUser(User user) {
		SharedPreferences.Editor editor = getPref().edit();
		editor.putLong(PROPERTY_ID, user.getId());
		editor.putString(PROPERTY_NAME, user.getName());
		editor.putInt(PROPERTY_LEVEL, user.getLevel());
		editor.putInt(PROPERTY_RUBY, user.getRuby());
		editor.commit();
	}

	private static User loadUser() {
		User user = new User();
		SharedPreferences pref = getPref();
		user.setId(pref.getLong(PROPERTY_ID, 0));
		user.setName(pref.getString(PROPERTY_NAME, ""));
		user.setLevel(pref.getInt(PROPERTY_LEVEL, 0));
		user.setRuby(pref.getInt(PROPERTY_RUBY, 0));
		return user;
	}

	public static boolean getReady() {
		return !getUser().getId().equals(0l);
	}

	private static SharedPreferences getPref() {
		return RubyApplication.getInstance().getSharedPreferences(PREF_USER, Context.MODE_PRIVATE);
	}

}
