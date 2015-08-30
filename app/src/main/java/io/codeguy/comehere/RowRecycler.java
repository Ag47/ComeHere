package io.codeguy.comehere;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.codeguy.comehere.DataObject.pending;

/**
 * Created by KaiHin on 7/8/2015.
 */
public class RowRecycler extends Activity {
    private static String TAG = MainActivity.class.getSimpleName();
    JSONArray comeHereDB = null;
    private String urlJsonObj = "http://10.0.2.2/android/readAllpending.php";
    private ProgressDialog pDialog;
    private String jsonResponse;
    private TextView txtResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public List<pending> getData() {
        final List<pending> data = new ArrayList<>();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, (String) null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    comeHereDB = response.getJSONArray("come_here");
                    for (int i = 0; i < comeHereDB.length(); i++) {
                        pending currentPending = new pending();
                        JSONObject currentJsonObject = comeHereDB.getJSONObject(i);
                        String id = currentJsonObject.getString("pending_id");
                        String name = currentJsonObject.getString("pending_name");
                        String appkey = currentJsonObject.getString("app_registered_key");
                        String expired = currentJsonObject.getString("time_expired");
                        currentPending.setId(id);
                        currentPending.setName(name);
                        currentPending.setInstalledKey(appkey);
                        currentPending.setTimeExpired(expired);
                        data.add(currentPending);

//                        jsonResponse += "Name: " + id + "\n\n";
//                        jsonResponse += "Email: " + name + "\n\n";
//                        jsonResponse += "Home: " + appkey + "\n\n";
//                        jsonResponse += "Mobile: " + expired + "\n\n\n";
                    }
//                    txtResponse.setText(jsonResponse);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
        return data;
    }

}
