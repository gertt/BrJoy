package com.browsejoy.games.app.tv;

import android.content.Context;
import android.util.Log;

import com.browsejoy.games.app.data.prefs.SaveData;
import com.browsejoy.games.app.utils.Utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class VastTagPopulator {

    final static String TAG = "VastTagPopulator";

    SaveData saveData;

    Context context;

    public VastTagPopulator(SaveData saveData, Context context) {
        this.saveData = saveData;
        this.context = context;
    }

    private String getUserIP() {

        return Utils.getIPAddress(true);
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
            return Utils.getIPAddress(true);

        } catch (Exception e) {
            Log.e(TAG,e.toString() );
            return "127.0.0.1";
        }

    }

    public String enrichWithDeviceData(String urlWithGetParams) {

        String replaced = urlWithGetParams;

        replaced = insertValueInUrlQuery(replaced, "&adid=", getGoogleAdId());
        replaced = insertValueInUrlQuery(replaced, "&bundleid=", "com.browsejoy.games");
        replaced = insertValueInUrlQuery(replaced, "&appname=","browsejoy");
        replaced = insertValueInUrlQuery(replaced, "&cb=",java.util.UUID.randomUUID().toString());
        replaced = insertValueInUrlQuery(replaced, "&dnt=","0");
        // preload
        replaced = insertValueInUrlQuery(replaced, "&pl=","0");
        // json response type
        replaced = insertValueInUrlQuery(replaced, "&key=","3");
       // replaced = insertValueInUrlQuery(replaced, "&ip=",getUserIP());
        //replaced = insertValueInUrlQuery(replaced, "&plc=","1047125");
        replaced = insertValueInUrlQuery(replaced, "&siteurl=","browsejoy.co");

        return urlWithGetParams;
    }

    protected String insertValueInUrlQuery(String sourceString, String key, String value) {

        int keyPosition = sourceString.indexOf(key);
        if(keyPosition != -1) {
            int insertAfterKeyPosition = keyPosition + key.length();
            return sourceString.substring(0, insertAfterKeyPosition) + value + sourceString.substring(insertAfterKeyPosition, sourceString.length());
        }

        return sourceString;
    }

    protected String getGoogleAdId() {
        return saveData.getStoredValue("adId");
    }
}
