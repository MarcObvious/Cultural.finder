package letsbecool.culturalfinderandroidapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.ListActivity;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class NetworkingAndroidHttpClientJSONActivity extends Activity {
    private final static String LOG_TAG = "NetworkingAndroidHttpClientJSONActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "Sembla ser que tot va be");
        new HttpGetTask().execute();
    }

    private class HttpGetTask extends AsyncTask<Void, Void, List<Event>> {

        private static final String USER_NAME = ""; //NO HO FEM SERVIR DE MOMENT

        private static final String URL = "http://192.168.1.33:15000/Events/getString";
               // + USER_NAME;

        AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

        @Override
        protected List<Event> doInBackground(Void... params) {
            HttpGet request = new HttpGet(URL);
            JSONResponseHandler responseHandler = new JSONResponseHandler();
            try {
                return mClient.execute(request, responseHandler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

      //  @Override
        protected void onPostExecute(List<String> result) {
            if (null != mClient)
                mClient.close();
            /*setListAdapter(new ArrayAdapter<String>(
                    NetworkingAndroidHttpClientJSONActivity.this,
                    R.layout.list_item, result));*/
        }
    }

    private class JSONResponseHandler implements ResponseHandler<List<Event>> {


        @Override
        public List<Event> handleResponse(HttpResponse response)
                throws ClientProtocolException, IOException {
            List<Event> result = new ArrayList<Event>();
          /*  String JSONResponse = new BasicResponseHandler()
                    .handleResponse(response);

            Toast.makeText(getApplicationContext(), "aha", Toast.LENGTH_SHORT).show();

            try {
               // result = (ArrayList<Event>())JSONResponse.getBytes();

               // List<Event> events = targetGetall.request(MediaType.APPLICATION_JSON_TYPE).get(  new GenericType<List<Event>>(){} );

               // Get top-level JSON Object - a Map
                JSONObject responseObject = (JSONObject) new JSONTokener(
                        JSONResponse).nextValue();

                JSONArray tots = responseObject.getJSONArray("/getEvents");

                // Extract value of "earthquakes" key -- a List
                JSONArray earthquakes = responseObject
                        .getJSONArray(EARTHQUAKE_TAG);

                // Iterate over earthquakes list
               *//* for (int idx = 0; idx < earthquakes.length(); idx++) {

                    // Get single earthquake data - a Map
                    JSONObject earthquake = (JSONObject) earthquakes.get(idx);

                    // Summarize earthquake data as a string and add it to
                    // result
                    *//**//*result.add(MAGNITUDE_TAG + ":"
                            + earthquake.get(MAGNITUDE_TAG) + ","
                            + LATITUDE_TAG + ":"
                            + earthquake.getString(LATITUDE_TAG) + ","
                            + LONGITUDE_TAG + ":"
                            + earthquake.get(LONGITUDE_TAG));*//**//*
                }*//*
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
            return null;
        }
    }
}