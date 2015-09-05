package io.codeguy.comehere.menu_item;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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

import io.codeguy.comehere.MainActivity;
import io.codeguy.comehere.MySingleton;
import io.codeguy.comehere.R;
import io.codeguy.comehere.SpotDetailActivity;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG_doc = "documents";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    public static JSONObject responseObject = null;
    private static View rootView;
    String search_url = String.format("https://api-us.clusterpoint.com/100403/ComeHere/_search.json");
    JSONObject jsonObject = null;
    // contacts JSONArray
    JSONArray documents = null;
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> documentsList;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
//        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        jsonObject = new JSONObject();
        try {
            jsonObject.put("query", "<key>" + MainActivity.MKEY + "</key><state>1</state>");
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
//                        mTxtDisplay.setText("Response: " + response.toString());
                        try {
                            responseObject = response;
                            Log.i("jsonObject", jsonObject.toString());
                            setListView(rootView);
                        } catch
                                (Exception e) {
                            e.printStackTrace();
                        }
                        Log.i("here", "here2");
                        try {
                            Log.i("res", response.getJSONArray(TAG_doc).toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.i("response", response.toString());
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
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);

        MainActivity.fragmentExtra = "homehome";

        return rootView;
    }

    public void setListView(View rootView) {
        documentsList = new ArrayList<HashMap<String, String>>();
        ListView lv = (ListView) rootView.findViewById(R.id.listview);
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


                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value
                map.put(TAG_ID, id);
                MainActivity.extraId[i] = id;
                map.put(TAG_NAME, name);


                // adding HashList to ArrayList
                documentsList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(getActivity(), documentsList,
                R.layout.row, new String[]{TAG_ID, TAG_NAME}, new int[]{R.id.id, R.id.name});
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), SpotDetailActivity.class);
                Log.i("extra", Long.toString(id));
                intent.putExtra("ExtraIds", MainActivity.extraId[(int) id]);
                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng apliu = new LatLng(22.3291536, 114.1633124);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(apliu, 10));
// Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomIn());

// Zoom out to zoom level 10, animating with a duration of 2 seconds.
        map.animateCamera(CameraUpdateFactory.zoomTo(19), 3000, null);
        map.addMarker(new MarkerOptions()
                .title("Apliu")
                .snippet("Best Flea Market in HK")
                .position(apliu));
    }


}