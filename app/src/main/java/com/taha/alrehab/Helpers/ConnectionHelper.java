package com.taha.alrehab.Helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;


public class ConnectionHelper extends AsyncTask<Context, Void, String> {
    @Override
    protected String doInBackground(Context... params) {
        try {
            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://android.alrehablife.com").openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(3000); //choose your own timeframe
            urlc.setReadTimeout(4000); //choose your own timeframe
            urlc.connect();
            if (urlc.getResponseCode() != 200) {
                Toast.makeText(params[0], "no internet", Toast.LENGTH_LONG).show();
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3500); // As I am using LENGTH_LONG in Toast
                            System.exit(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
            return "Executed";

        } catch (Exception e) {
        }
        return "Executed";
        }
}

