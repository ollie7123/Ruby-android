package com.jaydi.ruby.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.appspot.ruby_mine.rubymine.model.Ruby;

public class RubyParcel implements Parcelable {
	private long id;
	private long giverId;
	private long planterId;
	private int value;

	public RubyParcel(Ruby ruby) {
		id = ruby.getId();
		giverId = ruby.getGiverId();
		planterId = ruby.getPlanterId();
		value = ruby.getValue();
	}

	public Ruby toRuby() {
		Ruby ruby = new Ruby();
		ruby.setId(id);
		ruby.setGiverId(giverId);
		ruby.setPlanterId(planterId);
		ruby.setValue(value);
		return ruby;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeLong(giverId);
		dest.writeLong(planterId);
		dest.writeInt(value);
	}

	public RubyParcel(Parcel source) {
		super();
		id = source.readLong();
		giverId = source.readLong();
		planterId = source.readLong();
		value = source.readInt();
	}

	public static final Parcelable.Creator<RubyParcel> CREATOR = new Parcelable.Creator<RubyParcel>() {

		@Override
		public RubyParcel createFromParcel(Parcel source) {
			return new RubyParcel(source);
		}

		@Override
		public RubyParcel[] newArray(int size) {
			return new RubyParcel[size];
		}

	};

}
