package com.taha.alrehab.JSON;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.taha.alrehab.BusinessEntities.AlrehabNotification;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlrehabNotificationsJSONHandler extends AsyncTask<String, String, List<AlrehabNotification>> {

    public static final String COLUMN_ID = "Id";
    public static final String COLUMN_TITLE = "Title";
    public static final String COLUMN_PUBLISHDATE = "PublishDate";
    public static final String COLUMN_IMAGEURL = "ImageUrl";
    public static final String COLUMN_IMAGETHUMBURL = "ImageThumbUrl";
    public static final String COLUMN_TYPE = "Type";
    private final AlrehabNotificationsJSONHandlerClient mClient;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public AlrehabNotificationsJSONHandler(AlrehabNotificationsJSONHandlerClient client) {
        mClient = client;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected List<AlrehabNotification> doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        List<AlrehabNotification> AlrehabNotificationList = new ArrayList<>();
        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            String finalJson = buffer.toString();

            //JSONObject parentObject = new JSONObject(finalJson);
            // JSONArray parentArray = parentObject.getJSONArray("stories");
            JSONArray parentArray = new JSONArray(finalJson);


            Gson gson = new Gson();
            for (int i = 0; i < parentArray.length(); i++) {

                JSONObject finalObject = parentArray.getJSONObject(i);
                try {
                    int _id = finalObject.getInt(COLUMN_ID);
                    String _title = finalObject.getString(COLUMN_TITLE);
                    Date _publishdate = dateFormat.parse(finalObject.getString(COLUMN_PUBLISHDATE));
                    String _imageUrl = finalObject.getString(COLUMN_IMAGEURL);
                    String _imageThumbUrl = finalObject.getString(COLUMN_IMAGETHUMBURL);
                    int _type = finalObject.getInt(COLUMN_TYPE);
                    _imageUrl = _imageUrl.replace("../", "http://test.alrehablife.com/");
                    _imageThumbUrl = _imageThumbUrl.replace("../", "http://test.alrehablife.com/");
                    AlrehabNotificationList.add(new AlrehabNotification(_id,
                            _title,
                            _publishdate,
                            _imageUrl,
                            _imageThumbUrl,
                            _type));


                } catch (Exception ex) {
                    String error = ex.getMessage();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return AlrehabNotificationList;
    }

    @Override
    protected void onPostExecute(List<AlrehabNotification> result) {

        mClient.onAlrehabNotificationsJSONHandlerClientResult(result);


    }

    public interface AlrehabNotificationsJSONHandlerClient {
        void onAlrehabNotificationsJSONHandlerClientResult(List<AlrehabNotification> result);
    }
}