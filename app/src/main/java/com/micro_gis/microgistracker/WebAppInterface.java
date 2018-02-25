package com.micro_gis.microgistracker;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.webkit.JavascriptInterface;

import com.micro_gis.microgistracker.activities.AddMarkerActivity;

/**
 * Created by oleg on 23.06.16.
 */
public class WebAppInterface {
    Context mContext;
    DBHelper dbHelper;

    public WebAppInterface(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void getMarker(final String lotlng) {
        dbHelper = new DBHelper(mContext);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues cv = new ContentValues();
        boolean isSave;
        Intent intent = new Intent(mContext, AddMarkerActivity.class);
        intent.putExtra("latlng",lotlng);
        mContext.startActivity(intent);
    }

}
