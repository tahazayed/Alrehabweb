package com.taha.alrehab.Helpers;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by taha on 2/9/16.
 */
public class ConnectionHelper {

    public static boolean IsOnline() {
        try {
            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.alrehablife.com.com").openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(3000); //choose your own timeframe
            urlc.setReadTimeout(4000); //choose your own timeframe
            urlc.connect();
            return (urlc.getResponseCode() == 200);
        } catch (Exception e) {
            return (false);  //connectivity exists, but no internet.
        }
    }
}
