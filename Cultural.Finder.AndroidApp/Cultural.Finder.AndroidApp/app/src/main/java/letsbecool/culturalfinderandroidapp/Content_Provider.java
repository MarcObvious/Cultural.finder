/*
package letsbecool.culturalfinderandroidapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.List;


public class Content_Provider extends ContentProvider {
    public Content_Provider() {
        HttpClient Client = new DefaultHttpClient();
        String URL = "http://localhost:15000/Events/getEvents";

        HttpGet httpget = new HttpGet(URL);
        ResponseHandler<List<Event> > responseHandler = new BasicResponseHandler();

        try {
            Client.execute(httpget, responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }

       Client.

        List<Event> events = targetGetall.request(MediaType.APPLICATION_JSON_TYPE).get(  new GenericType<List<Event>>(){} );

        for (Event item : events)
            System.out.println(item);

    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
*/
