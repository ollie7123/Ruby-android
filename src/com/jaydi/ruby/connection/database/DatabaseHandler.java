package com.jaydi.ruby.connection.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jaydi.ruby.models.RubyZone;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "SpotDB";

	private static final String TABLE_SPOTS = "spots";
	private static final String COL_ID = "id";
	private static final String COL_LAT = "lat";
	private static final String COL_LNG = "lng";
	private static final String COL_RANGE = "range";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String createMessagePinTable = "CREATE TABLE " + TABLE_SPOTS + " (" + COL_ID + " INTEGER PRIMARY KEY, " + COL_LAT + " REAL, " + COL_LNG
				+ " REAL, " + COL_RANGE + " INTEGER)";
		db.execSQL(createMessagePinTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPOTS);
			onCreate(db);
		}
	}

	public void addSpot(RubyZone spot) {
		if (spot == null)
			return;

		deleteSpot(spot.getId());

		ContentValues values = new ContentValues();
		values.put(COL_ID, spot.getId());
		values.put(COL_LAT, spot.getLat());
		values.put(COL_LNG, spot.getLng());
		values.put(COL_RANGE, spot.getRange());

		SQLiteDatabase db = getWritableDatabase();
		db.insert(TABLE_SPOTS, null, values);
		db.close();
	}

	// public Link getLink(String url) {
	// Link link = null;
	//
	// String[] cols = new String[] { COL_URL, COL_TITLE };
	// String selection = COL_URL + " = ?";
	// String[] args = new String[] { url };
	//
	// SQLiteDatabase db = getReadableDatabase();
	// Cursor cursor = db.query(TABLE_LINKS, cols, selection, args, null, null, null);
	// if (cursor == null)
	// return link;
	// if (cursor.isAfterLast())
	// return link;
	//
	// if (cursor.moveToFirst())
	// do {
	// link = new Link();
	// link.setUrl(cursor.getString(0));
	// link.setTitle(cursor.getString(1));
	// } while (cursor.moveToNext());
	//
	// cursor.close();
	// db.close();
	//
	// return link;
	// }

	public List<RubyZone> getSpots() {
		List<RubyZone> spots = new ArrayList<RubyZone>();

		String[] cols = new String[] { COL_ID, COL_LAT, COL_LNG, COL_RANGE };

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_SPOTS, cols, null, null, null, null, null);
		if (cursor == null)
			return spots;
		if (cursor.isAfterLast())
			return spots;

		if (cursor.moveToFirst())
			do {
				RubyZone spot = new RubyZone();
				spot.setId(cursor.getLong(0));
				spot.setLat(cursor.getDouble(1));
				spot.setLng(cursor.getDouble(2));
				spot.setRange(cursor.getInt(3));
				spots.add(spot);
			} while (cursor.moveToNext());

		cursor.close();
		db.close();

		return spots;
	}

	public void deleteSpot(long id) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_SPOTS, COL_ID + " = ?", new String[] { String.valueOf(id) });
		db.close();
	}

}
