package io.codeguy.comehere;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import io.codeguy.comehere.Network.AppController;


public class AddToPendingDetailActivity extends ActionBarActivity {

    //    String insertURL = "http://10.0.2.2/android/add_to_pending.php";
    String insertURL = "http://androiddebugoska.host22.com/add_to_pending.php";
    String spotName, spotTime, spotType;
    ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        PD = new ProgressDialog(this);
        PD.setMessage("Loading.....");
        PD.setCancelable(false);

        setContentView(R.layout.activity_pending_detail);

        Button btnAddToPending = (Button) findViewById(R.id.btnAddPending);
        final EditText editName = (EditText) findViewById(R.id.editName);
        final EditText editType = (EditText) findViewById(R.id.editType);
        final EditText edittime = (EditText) findViewById(R.id.editTime);

        // may be there are application key that need to upload to the database.

        btnAddToPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Snackbar.make(v, "Added into the pending queue", Snackbar.LENGTH_LONG).show();
                spotName = editName.getText().toString();
                spotTime = edittime.getText().toString();
                spotType = editType.getText().toString();
                Log.v("720", "spotName :" + spotName);

                StringRequest postRequest = new StringRequest(Request.Method.POST, insertURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                PD.dismiss();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        PD.dismiss();
                        Toast.makeText(getApplicationContext(),
                                "failed to insert", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("spot_item_name", spotName);
                        params.put("spot_item_time", spotTime);
                        params.put("spot_item_type", spotType);

                        Log.v("pending", "in the hash map" + spotName);
                        return params;
                    }
                };

                // Adding request to request queues
                AppController.getInstance().addToRequestQueue(postRequest);
            }
        });
    }

    public void insertTo_tbl_pending(View v) {


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pending_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
