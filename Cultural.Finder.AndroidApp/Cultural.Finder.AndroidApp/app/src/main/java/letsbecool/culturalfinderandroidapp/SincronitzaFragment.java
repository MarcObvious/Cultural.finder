package letsbecool.culturalfinderandroidapp;

import android.app.Activity;

import android.content.Intent;
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


public class SincronitzaFragment extends Fragment implements View.OnClickListener {
    private final static String LOG_TAG = "Sincronitza";
/*   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sincronitza);

        final Button loadButton = (Button) findViewById(R.id.button1);
        loadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Sincronitza.this,
                        NetworkingAndroidHttpClientJSONActivity.class));
            }
        });
    }*/

    private ViewGroup mSincronitzaView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSincronitzaView = (ViewGroup)inflater.inflate(R.layout.sincronitza, container, false);
        Button sinc = (Button) mSincronitzaView.findViewById(R.id.button1);
        sinc .setOnClickListener(this);
        return mSincronitzaView;


    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        Toast.makeText(getActivity().getApplicationContext(), LOG_TAG+"S'ha començat l'activitat", Toast.LENGTH_LONG).show();
        Log.i(LOG_TAG, " S'ha començat l'activitat");
        startActivity(new Intent(getActivity(),
                NetworkingAndroidHttpClientJSONActivity.class));


       /* switch (view.getId()) {
            case R.id.Button_home:
                saveLocation();
                drawLocation(dest_loc);
                Toast.makeText(getActivity().getApplicationContext(), "GO HOME",
                        Toast.LENGTH_LONG).show();
                break;
            case R.id.Button_search:
                saveLocation();
                drawLocation(dest_loc);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                Toast.makeText(getActivity().getApplicationContext(), "SEARCH",
                        Toast.LENGTH_LONG).show();
                break;
            default:
                Log.i(LOG_TAG, "Unknown: " + view.getId());
                break;
        }*/
    }
}
