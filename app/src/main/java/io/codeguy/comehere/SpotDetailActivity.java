package io.codeguy.comehere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SpotDetailActivity extends FragmentActivity implements View.OnClickListener, OnMapReadyCallback{
    private Button mQueen;
    private Button mHidden;
    private SpotDetailOuterLayout mOuterLayout;
    private RelativeLayout mMainLayout;

    public GoogleMap googleMap;
    private static final String TAG_doc = "documents";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_TYPE = "type";
    private static final String TAG_IMAGE_URL = "image_url";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_LONGITUDE = "longitude";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_PRICE = "price";
    private static final String TAG_HOLDER = "holder";
    private static final String TAG_KEY = "key";
    private static final String TAG_STATE = "state";

//    public static String[] extraId;

    public static String targetName;

    String search_url = String.format("https://api-us.clusterpoint.com/100403/ComeHere/_search.json");
    JSONObject jsonObject = null;

    public static JSONObject responseObject = null;

    // contacts JSONArray
    JSONArray documents = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> documentsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_detail);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);


        mOuterLayout = (SpotDetailOuterLayout) findViewById(R.id.outer_layout);
        mMainLayout = (RelativeLayout) findViewById(R.id.main_layout);
//        mHidden = (Button) findViewById(R.id.hidden_button);
//        mHidden.setOnClickListener(this);
        mQueen = (Button) findViewById(R.id.queen_button);
        mQueen.setOnClickListener(this);
        mMainLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mOuterLayout.isMoving()) {
                    v.setTop(oldTop);
                    v.setBottom(oldBottom);
                    v.setLeft(oldLeft);
                    v.setRight(oldRight);
                }
            }
        });
        Intent intent = getIntent();
        String id = intent.getStringExtra("ExtraIds");
        Log.i("get new id", id);
        jsonObject = new JSONObject();
        try {
//            jsonObject.put("query", "<key>" + MainActivity.MKEY + "</key><state>1</state><id>" + id + "</id>");
            jsonObject.put("query", "<id>" + id + "</id>");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO Optimize coding
//        Clusterpoint cp = new Clusterpoint();
//        String response = cp.search(getActivity(), 0);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, search_url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            responseObject = response;
                            setView();
                        } catch
                                (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                // Add Basic Authentication header
                Map<String, String> newHeaders = new HashMap<String, String>();
                newHeaders.putAll(headers);
//                String authorizationString = "Basic " + Base64.encodeToString(
//                        ("secretsilver@ymail.com" + ":" + "rt47rt47").getBytes(),
//                        Base64.NO_WRAP);
//                newHeaders.put("Authorization", authorizationString);
                newHeaders.put("Authorization", "Basic c2VjcmV0c2lsdmVyQHltYWlsLmNvbTpydDQ3cnQ0Nw==");

                return newHeaders;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        Toast t = Toast.makeText(this, b.getText() + " clicked", Toast.LENGTH_SHORT);
        t.show();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
    }

    public void setView() {
        documentsList = new ArrayList<HashMap<String, String>>();
        try {
            // Getting array of documents
            documents = responseObject.getJSONArray("documents");

            // Looping through All documents
            for (int i = 0; i < documents.length(); i++) {
                JSONObject currentJsonObject = documents.getJSONObject(i);

                Log.i("here", "here");

                // Storing each json item in variable
                String id = currentJsonObject.getString(TAG_ID);
                String name = currentJsonObject.getString(TAG_NAME);
                targetName = name;
                String type = currentJsonObject.getString(TAG_TYPE);
                String image_url = currentJsonObject.getString(TAG_IMAGE_URL);
                String location = currentJsonObject.getString(TAG_LOCATION);
                String longitude = currentJsonObject.getString(TAG_LONGITUDE);
                String latitude = currentJsonObject.getString(TAG_LATITUDE);
                double mlong = Double.valueOf(longitude);
                double mlongitude = Double.valueOf(latitude);
                String price = "$" + currentJsonObject.getString(TAG_PRICE);
                String holder = currentJsonObject.getString(TAG_HOLDER);
                String key = currentJsonObject.getString(TAG_KEY);
                String state = currentJsonObject.getString(TAG_STATE);

                TextView tv = (TextView) findViewById(R.id.name);
                tv.setText(name);
                tv = (TextView) findViewById(R.id.type);
                tv.setText(type);
                tv = (TextView) findViewById(R.id.price);
                tv.setText(price);
                tv = (TextView) findViewById(R.id.vendor);
                tv.setText(holder);
                tv = (TextView) findViewById(R.id.address);
                tv.setText(location);
                Log.i("himap2", "");
                moveMap(mlong, mlongitude);
                Log.i("himap3", "");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void moveMap(double longitude, double latitude) {
        LatLng target = new LatLng(longitude, latitude);

        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(target, 17));
        Log.i("himap", "");
        googleMap.addMarker(new MarkerOptions()
                .title(targetName)
                .snippet("Reach there and buy it")
                .position(target));
    }


}
