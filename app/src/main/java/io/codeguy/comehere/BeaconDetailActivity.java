package io.codeguy.comehere;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.estimote.sdk.Beacon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.codeguy.comehere.DataObject.Product;

/**
 * Created by KaiHin on 10/9/2015.
 */

public class BeaconDetailActivity extends AppCompatActivity {
    private JSONArray comeHereDB = null;
    Beacon getBeacon;
    String shopperType;
    String shopperName;
    String shopperAddr;
    TextView beaconShopperName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.beacon_detail_activity);
        Log.v("9/10","inside beacon detail activity");
        getBeacon = getIntent().getParcelableExtra("extraBeacon");
        String beaconMajor = String.valueOf(getBeacon.getMajor());
//        fetchShopDetail(beaconMajor);
        Log.v("9/10", "major" + getBeacon.getMajor());
//        beaconShopperName = (TextView) findViewById(R.id.beacon_shopper_name);
//        beaconShopperName.setText(shopperName);
    }

//    private void fetchShopDetail(final String beaconMajor) {
//
//
//        String urlSearchProductJson = "http://androiddebugoska.host22.com/fetching_shop_detail.php?beacon_major=" + beaconMajor;
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
//                urlSearchProductJson, (String) null, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("oscar", response.toString());
//                try {
//                    comeHereDB = response.getJSONArray("come_here");
//                    Log.v("oscar", "before looping");
//                    for (int i = 0; i < comeHereDB.length(); i++) {
//                        JSONObject currentJsonObject = comeHereDB.getJSONObject(i);
//                        shopperType = currentJsonObject.getString("v_type");
//                        shopperName = currentJsonObject.getString("v_shop_name");
//                        shopperAddr = currentJsonObject.getString("v_addr");
//                        Log.v("BeaconDetailActivity","the shopper name is " + shopperName);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.v("oscar", "onError Response: " + error.toString());
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("p_name", beaconMajor);
//                return super.getParams();
//            }
//        };
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq);
//    }
}
