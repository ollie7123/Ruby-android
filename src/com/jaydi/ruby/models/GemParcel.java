package com.jaydi.ruby.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.appspot.ruby_mine.rubymine.model.Gem;

public class GemParcel implements Parcelable {
	private long id;
	private long rubymineId;
	private String name;
	private String rubymineName;
	private String imageKey;
	private String desc;
	private int type;
	private int value;
	private int ea;

	public GemParcel(Gem gem) {
		id = gem.getId();
		rubymineId = gem.getRubymineId();
		name = gem.getName();
		rubymineName = gem.getRubymineName();
		imageKey = gem.getImageKey();
		desc = gem.getDesc();
		type = gem.getType();
		value = gem.getValue();
		ea = gem.getEa();
	}

	public Gem toGem() {
		Gem gem = new Gem();
		gem.setId(id);
		gem.setRubymineId(rubymineId);
		gem.setName(name);
		gem.setRubymineName(rubymineName);
		gem.setImageKey(imageKey);
		gem.setDesc(desc);
		gem.setType(type);
		gem.setValue(value);
		gem.setEa(ea);
		return gem;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getRubymineId() {
		return rubymineId;
	}

	public void setRubymineId(long rubymineId) {
		this.rubymineId = rubymineId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRubymineName() {
		return rubymineName;
	}

	public void setRubymineName(String rubymineName) {
		this.rubymineName = rubymineName;
	}

	public String getImageKey() {
		return imageKey;
	}

	public void setImageKey(String imageKey) {
		this.imageKey = imageKey;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getEa() {
		return ea;
	}

	public void setEa(int ea) {
		this.ea = ea;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeLong(rubymineId);
		dest.writeString(name);
		dest.writeString(rubymineName);
		dest.writeString(imageKey);
		dest.writeString(desc);
		dest.writeInt(type);
		dest.writeInt(value);
		dest.writeInt(ea);
	}

	public GemParcel(Parcel source) {
		super();
		id = source.readLong();
		rubymineId = source.readLong();
		name = source.readString();
		rubymineName = source.readString();
		imageKey = source.readString();
		desc = source.readString();
		type = source.readInt();
		value = source.readInt();
		ea = source.readInt();
	}

	public static final Parcelable.Creator<GemParcel> CREATOR = new Parcelable.Creator<GemParcel>() {

		@Override
		public GemParcel createFromParcel(Parcel source) {
			return new GemParcel(source);
		}

		@Override
		public GemParcel[] newArray(int size) {
			return new GemParcel[size];
		}

	};

}
