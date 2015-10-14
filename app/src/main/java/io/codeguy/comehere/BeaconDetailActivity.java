package io.codeguy.comehere;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.codeguy.comehere.Adapter.BeaconListAdapter;
import io.codeguy.comehere.Adapter.FocusTodayInfoAdapter;
import io.codeguy.comehere.DataObject.Product;
import io.codeguy.comehere.DataObject.response;

/**
 * Created by KaiHin on 10/9/2015.
 */

public class BeaconDetailActivity extends AppCompatActivity {
    private JSONArray comeHereDB = null;
    Beacon getBeacon;
    String beaconMajor;
    String shopperType;
    String shopperName;
    String shopperAddr;
    TextView beaconShopperName, beaconShopperType;
    Toolbar toolbar;
    CircleImageView beaconShopperIcon;
    String[] focusTitle;
    Integer[] focusImage;
    ListView list;
    Button btnCheckArrived;
    public boolean enableChecking;


    private static final int REQUEST_ENABLE_BT = 1234;
    private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);

    private BeaconManager beaconManager;
    private BeaconListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.beacon_detail_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        focusTitle = new String[]{"Wholesale Pocket Watches", "Hamilton Electric Watch", "Pre-1930 Collectible Antique Clocks"};
        focusImage = new Integer[]{R.drawable.clock1, R.drawable.clock2, R.drawable.clock3};
        getBeacon = getIntent().getParcelableExtra("extraBeacon");
        beaconMajor = String.valueOf(getBeacon.getMajor());
        beaconShopperName = (TextView) findViewById(R.id.beacon_shopper_name);
        beaconShopperType = (TextView) findViewById(R.id.beacon_shopper_type);
        beaconShopperIcon = (CircleImageView) findViewById(R.id.beacon_shopper_image);
        list = (ListView) findViewById(R.id.list_focus);
        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
                // Note that results are not delivered on UI thread.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.v("13/10","is beacon running");
                        for (Beacon getSpecficBeacon : beacons) {
                            Log.v("13/10", "get beacon: " + getSpecficBeacon.getMajor() + " my beacon :" + beaconMajor);
                            if (String.valueOf(getSpecficBeacon.getMajor()).equals(beaconMajor)&& enableChecking) {
                                Log.v("13/10", "say hello");
                                Log.v("13/10", "in the checking arrived");
                                Log.v("13/10", "the boolean is" + enableChecking);
                                if(Utils.computeAccuracy(getSpecficBeacon) > 0.25) {
                                    btnCheckArrived.setText("No, Remaining "
                                            + String.valueOf(Utils.computeAccuracy(getSpecficBeacon)).substring(0, 4)
                                            + "m");
                                }
                                else {
                                    btnCheckArrived.setText("Good, you arrived");
                                    btnCheckArrived.setBackgroundColor(getResources().getColor(R.color.shopper_type));
                                    enableChecking = false;
                                }
                            }

                        }
                    }

                });
            }
        });
        btnCheckArrived = (Button) findViewById(R.id.btn_arrived);
        btnCheckArrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("13/10", "in the on button click");
                enableChecking = true;
                btnCheckArrived.setBackgroundColor(getResources().getColor(R.color.white_pressed));
            }
        });
    }


    JSONObject result;

    private void fetchShopDetail(final String beaconMajor, final VolleyCallback callback) {


        String urlSearchProductJson = "http://androiddebugoska.host22.com/fetching_shop_detail.php?beacon_major=" + beaconMajor;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                urlSearchProductJson, (String) null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("oscar", response.toString());
                result = response;
                callback.onSuccess(result);
            }
//        StringRequest sr = new StringRequest(Request.Method.POST, urlSearchProductJson, new Response.Listener<String>()){
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("oscar", "onError Response: " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("beacon_major", beaconMajor);
                return super.getParams();
            }


//            protected Map<String, String> getParamsFromVolley(){
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("shopperName", shopperName);
//                params.put("shopperAddr",shopperAddr);
//                params.put("shopperType",shopperType);
//                return checkParams(params);
//            };
//
//
//            private Map<String, String> checkParams(Map<String, String> map){
//                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
//                while (it.hasNext()) {
//                    Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
//                    if(pairs.getValue()==null){
//                        map.put(pairs.getKey(), "");
//                    }
//                }
//                return map;
//            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    //---------------------------------------------------------------------//
    // the usage of onResume is get the volley data and update in onResume
    //--------------------------------------------------------------------//

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("10/10", "in the onResume");
        fetchShopDetail(beaconMajor, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.v("10/10", "result" + result);
                beaconShopperName.setText("hihi");
                try {
                    comeHereDB = result.getJSONArray("come_here");
                    for (int i = 0; i < comeHereDB.length(); i++) {
                        JSONObject currentJsonObject = comeHereDB.getJSONObject(i);
                        shopperType = currentJsonObject.getString("v_type");
                        shopperName = currentJsonObject.getString("v_shop_name");
                        shopperAddr = currentJsonObject.getString("v_addr");
                        Log.v("BeaconDetailActivity", "the shopper name is " + shopperName);
                        shopperType = shopperType.replace(',', '#');

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (shopperName.equals("Nam Pont Bldg"))
                    beaconShopperIcon.setImageResource(R.drawable.nam_pont_bldg_shopper);
                else if (shopperName.equals("Ming Chu Bldg"))
                    beaconShopperIcon.setImageResource(R.drawable.ming_chu_bldg_shopper);
                else
                    beaconShopperIcon.setImageResource(R.drawable.audio_ltd);
                beaconShopperName.setText(shopperName);

                beaconShopperType.setText("#" + shopperType);

                FocusTodayInfoAdapter focusTodayInfoAdapter = new FocusTodayInfoAdapter(BeaconDetailActivity.this, focusTitle, focusImage);
                list.setAdapter(focusTodayInfoAdapter);
                connectToService();
            }
        });
    }

    public interface VolleyCallback {
        void onSuccess(JSONObject result);
    }


//    @Override
//    protected void onDestroy() {
//        beaconManager.disconnect();
//
//        super.onDestroy();
//    }

//    @Override
//    protected void onDestroy() {
//        beaconManager.disconnect();
//
//        super.onDestroy();
//    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if device supports Bluetooth Low Energy.
        if (!beaconManager.hasBluetooth()) {
            Toast.makeText(this, "Device does not have Bluetooth Low Energy", Toast.LENGTH_LONG).show();
            return;
        }

        // If Bluetooth is not enabled, let user enable it.
        if (!beaconManager.isBluetoothEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            connectToService();
        }
    }

//    @Override
//    protected void onStop() {
//        beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGION);
//        super.onStop();
//    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        if (!beaconManager.hasBluetooth()) {
//            Toast.makeText(this, "Device does not have Bluetooth Low Energy", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        // If Bluetooth is not enabled, let user enable it.
//        if (!beaconManager.isBluetoothEnabled()) {
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//        } else {
//            connectToService();
//        }
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        // Check if device supports Bluetooth Low Energy.
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                connectToService();
            } else {
                Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void connectToService() {
//        adapter.replaceWith(Collections.<Beacon>emptyList());
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);
            }
        });
    }


}

