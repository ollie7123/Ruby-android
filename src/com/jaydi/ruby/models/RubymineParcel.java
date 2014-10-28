package com.jaydi.ruby.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.appspot.ruby_mine.rubymine.model.Rubymine;
import com.appspot.ruby_mine.rubymine.model.Text;

public class RubymineParcel implements Parcelable {
	private long id;
	private long rubyzoneId;
	private String name;
	private String contents;
	private int type;
	private int ruby;
	private int value;

	public RubymineParcel(Rubymine rubymine) {
		super();
		id = rubymine.getId();
		rubyzoneId = rubymine.getRubyzoneId();
		name = rubymine.getName();
		contents = rubymine.getContents().getValue();
		type = rubymine.getType();
		ruby = rubymine.getRuby();
		value = rubymine.getValue();
	}

	public Rubymine toRubymine() {
		Rubymine rubymine = new Rubymine();
		rubymine.setId(id);
		rubymine.setRubyzoneId(rubyzoneId);
		rubymine.setName(name);
		Text textContents = new Text();
		textContents.setValue(contents);
		rubymine.setContents(textContents);
		rubymine.setType(type);
		rubymine.setRuby(ruby);
		rubymine.setValue(value);
		return rubymine;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getRubyzoneId() {
		return rubyzoneId;
	}

	public void setRubyzoneId(long rubyzoneId) {
		this.rubyzoneId = rubyzoneId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRuby() {
		return ruby;
	}

	public void setRuby(int ruby) {
		this.ruby = ruby;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeLong(rubyzoneId);
		dest.writeString(name);
		dest.writeString(contents);
		dest.writeInt(type);
		dest.writeInt(ruby);
		dest.writeInt(value);
	}

	public RubymineParcel(Parcel source) {
		super();
		id = source.readLong();
		rubyzoneId = source.readLong();
		name = source.readString();
		contents = source.readString();
		type = source.readInt();
		ruby = source.readInt();
		value = source.readInt();
	}

	public static final Parcelable.Creator<RubymineParcel> CREATOR = new Parcelable.Creator<RubymineParcel>() {

		@Override
		public RubymineParcel createFromParcel(Parcel source) {
			return new RubymineParcel(source);
		}

		@Override
		public RubymineParcel[] newArray(int size) {
			return new RubymineParcel[size];
		}

	};

}
