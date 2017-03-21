package com.micro_gis.microgistracker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

/**
 * Created by oleg on 23.06.16.
 */
public class WebAppInterface {
    Context mContext;
    WebView webView;
    DBHelper dbHelper;

    WebAppInterface(Context c) {
        mContext = c;

    }

    @JavascriptInterface
    public void getMarker(final String lotlng) {
//        Toast.makeText(mContext, "tost", Toast.LENGTH_SHORT).show();
        dbHelper = new DBHelper(mContext);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues cv = new ContentValues();
        boolean isSave;
        Intent intent = new Intent(mContext, AddPlaceActivity.class);
        intent.putExtra("latlng",lotlng);
        mContext.startActivity(intent);
//        final String[] latlong = new String[1];
//        final String[] name = new String[1];
//                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
//                LayoutInflater inflater = LayoutInflater.from(mContext);
//                final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
//                dialogBuilder.setView(dialogView);
//                webView = (WebView)((Activity) mContext).findViewById(R.id.webview);
//                final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
//                final TextView coordinates = (TextView) dialogView.findViewById(R.id.coordinates);
//                coordinates.setText(lotlng);
//                dialogBuilder.setTitle(mContext.getString(R.string.save_marker));
//                dialogBuilder.setMessage(mContext.getString(R.string.enter_name_of_place));
//        dialogBuilder.setPositiveButton(mContext.getString(R.string.done), new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                       try{
//                        name[0] =  name[0]+String.valueOf(edt.getText());
//                        latlong[0] = lotlng;
//                        final String str = new String(name[0]);
//                        final Marker marker = new Marker();
//                        marker.setName(name[0]);
//                        marker.setLatlng(latlong[0]);
//                        Gson gson = new Gson();
//                        cv.put("latlng",latlong[0]);
//                        cv.put("data", gson.toJson(marker).getBytes());
//                        db.insert("markers", null, cv);
//
//                        ((Activity) mContext).runOnUiThread(new Runnable() {
//                            @TargetApi(Build.VERSION_CODES.KITKAT)
//                            @Override
//                            public void run() {
//                                webView.loadUrl("javascript: \n" +
//                                        "var RedIcon = L.Icon.Default.extend({\n" +
//                                        "            options: {\n" +
//                                        "            \t    iconUrl: 'file:///android_res/drawable/flag_green.png',\n" +
//                                        "            \t     shadowSize: [0, 0]" +
//                                        "}" +
//                                        "         });\n" +
//                                        "         var redIcon = new RedIcon();\n" +
//                                        "\n" +
//                                        "  var popup = L.popup()\n" +
//                                        "    .setContent('"+name[0]+"');        var place = L.marker("+latlong[0]+", {icon: redIcon}).bindPopup(popup).addTo(map);");
//
//
//
//
//                                webView.loadUrl("javascript:map.removeLayer(mitka);");
//                            }
//                        });
//                    }
//           catch(Exception e) {
//           } finally {
//                           db.close();
//                       }
//                    }
//                });
//                dialogBuilder.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        ((Activity) mContext).runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                webView.loadUrl("javascript:map.removeLayer(mitka);");            }
//        });
//
//                    }
//                });
//                AlertDialog b = dialogBuilder.create();
//                b.show();
//

    }

}
