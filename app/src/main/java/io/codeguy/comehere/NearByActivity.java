package io.codeguy.comehere;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import io.codeguy.comehere.Adapter.BeaconListAdapter;

/**
 * Created by KaiHin on 10/6/2015.
 */
public class NearByActivity extends AppCompatActivity {

//    private static final String "NearBy" = ListBeaconsActivity.class.getSimpleName();

    protected Toolbar toolbar;
    private JSONArray comeHereDB = null;

    public static final String EXTRAS_TARGET_ACTIVITY = "extrasTargetActivity";
    public static final String EXTRAS_BEACON = "extrasBeacon";

    private static final int REQUEST_ENABLE_BT = 1234;
    private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);

    private BeaconManager beaconManager;
    private BeaconListAdapter adapter;

    String shopperType, shopperName, shopperAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.beacon_main);
        // Configure device list.
        adapter = new BeaconListAdapter(this);
        ListView list = (ListView) findViewById(R.id.device_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(createOnItemClickListener());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(getTitle());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Configure BeaconManager.
        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
                // Note that results are not delivered on UI thread.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Note that beacons reported here are already sorted by estimated
                        // distance between device and beacon.
                        Log.v("13/10", "is running");
                        toolbar.setSubtitle("Found beacons: " + beacons.size());
                        adapter.replaceWith(beacons);
                        for(Beacon getBeacon : beacons){
                            Log.v("8/10","the major is" +getBeacon.getMajor());
                            if(getBeacon.getMajor() == 12159)
                            {
                                Log.v("8/10","say hello");
                            }

                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        beaconManager.disconnect();

        super.onDestroy();
    }

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

    @Override
    protected void onStop() {
        beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGION);
        super.onStop();
    }

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
                toolbar.setSubtitle("Bluetooth not enabled");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void connectToService() {
        toolbar.setSubtitle("Searching...");
        adapter.replaceWith(Collections.<Beacon>emptyList());
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);
            }
        });
    }

    private AdapterView.OnItemClickListener createOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent beaconDetail = new Intent(NearByActivity.this, BeaconDetailActivity.class);
                beaconDetail.putExtra("extraBeacon", adapter.getItem(position));
//                fetchShopDetail(String.valueOf(adapter.getItem(position).getMajor()));
                Log.v("9/10", "inside on click listener");
//                Log.v("getFromDB", shopperType + " " + shopperAddr + " " + shopperName);
                startActivity(beaconDetail);
            }
        };
    }


}