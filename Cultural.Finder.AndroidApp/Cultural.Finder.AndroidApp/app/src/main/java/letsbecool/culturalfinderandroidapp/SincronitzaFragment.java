package letsbecool.culturalfinderandroidapp;

import android.app.Activity;

import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class SincronitzaFragment extends Fragment implements View.OnClickListener {
    private final static String LOG_TAG = "Sincronitza";

    private ViewGroup mSincronitzaView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSincronitzaView = (ViewGroup)inflater.inflate(R.layout.sincronitza, container, false);
        Button sinc = (Button) mSincronitzaView.findViewById(R.id.button1);
        sinc .setOnClickListener(this);
        return mSincronitzaView;


    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        Toast.makeText(getActivity(), LOG_TAG+"S'ha començat l'activitat", Toast.LENGTH_LONG).show();
        Log.i(LOG_TAG, " S'ha començat l'activitat");
        new HttpGetTask().execute();


    }
    private class HttpGetTask extends AsyncTask<Void, Void, String> {

        private static final String USER_NAME = ""; //NO HO FEM SERVIR DE MOMENT

        private static final String URL = "http://192.168.1.33:15000/Events/getString";
        // + USER_NAME;

        AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

        @Override
        protected String doInBackground(Void... params) {
            HttpGet request = new HttpGet(URL);
            JSONResponseHandler responseHandler = new JSONResponseHandler();
            Log.i(LOG_TAG, "Ara?? Abans o despres?");
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
        protected void onPostExecute(String result) {
            if (null != mClient)
                mClient.close();
            if (result != null) Log.i(LOG_TAG, "Sembla ser que alguna cosa surt? -> " + result);
            /*SetListAdapter(new String(
                    NetworkingAndroidHttpClientJSONActivity.this,
                    R.layout.list_item, result));*/
        }
    }

    private class JSONResponseHandler implements ResponseHandler<String> {


        @Override
        public String handleResponse(HttpResponse response)
                throws ClientProtocolException, IOException {
            //List<Event> result = new ArrayList<Event>();

            String JSONResponse = new BasicResponseHandler()
                    .handleResponse(response);
          //  String a = (String) JSONResponse.getBytes().toString();

            String jsonString = null;
            try {
                jsonString = new String(JSONResponse.getBytes(), "UTF-8");
            }
            catch (UnsupportedEncodingException e) {
                Log.i(LOG_TAG, "NOPE");
            }
         /*//   Log.i(LOG_TAG, "1 " + JSONResponse.getBytes());
            Log.i(LOG_TAG, "Status line " + response.getStatusLine());
            Log.i(LOG_TAG, "3 " + response.getEntity().getContent().toString());
            Log.i(LOG_TAG, "4 " + response.getAllHeaders());
            String result = response.getEntity().toString();*/

            return jsonString;
        }


        /*public Object convertToUseForm(Object key, byte[] bytes) {
            String jsonString = null;
            try {
                jsonString = new String(bytes, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                //#debug
                L.e("Unable to instantiate a UTF-8 string from bytes.", "", e);
            }

            JSONObject response = null;
            try {
                response = new JSONObject(jsonString);
            } catch (JSONException e) {
                //#debug
                L.e("Unable to instantiate a JSONObject from response.", "", e);
            }

            return response;
        }*/
    }
}
