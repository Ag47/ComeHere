package io.codeguy.comehere;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PendingTab extends Fragment {

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
    public static JSONObject responseObject = null;
    String search_url = String.format("https://api-us.clusterpoint.com/100403/ComeHere/_search.json");
    JSONObject jsonObject = null;
    // contacts JSONArray
    JSONArray documents = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> documentsList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.tab_pending, container, false);

        Log.i("MKEYtab", MainActivity.MKEY);
        jsonObject = new JSONObject();
        try {
            jsonObject.put("query", "<key>" + MainActivity.MKEY + "</key><state>0</state>");
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
                            setListView(v);
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

        return v;
    }

    public void setListView(View rootView) {
        documentsList = new ArrayList<HashMap<String, String>>();
        ListView listView = (ListView) rootView.findViewById(R.id.listview);
        int[] colors = {0xFF9CD1DD, 0xFF0872BA, 0xFF9CD1DD};
        listView.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        listView.setDividerHeight(5);
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
                String type = currentJsonObject.getString(TAG_TYPE);
                String image_url = currentJsonObject.getString(TAG_IMAGE_URL);
                String location = currentJsonObject.getString(TAG_LOCATION);
                String longitude = currentJsonObject.getString(TAG_LONGITUDE);
                String latitude = currentJsonObject.getString(TAG_LATITUDE);
                String price = "$" + currentJsonObject.getString(TAG_PRICE);
                String holder = currentJsonObject.getString(TAG_HOLDER);
                String key = currentJsonObject.getString(TAG_KEY);
                String state = currentJsonObject.getString(TAG_STATE);

                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value
                map.put(TAG_ID, id);
                map.put(TAG_NAME, name);
                map.put(TAG_TYPE, type);
                map.put(TAG_IMAGE_URL, image_url);
                map.put(TAG_LOCATION, location);
                map.put(TAG_LONGITUDE, longitude);
                map.put(TAG_LATITUDE, latitude);
                map.put(TAG_PRICE, price);
                map.put(TAG_HOLDER, holder);
                map.put(TAG_KEY, key);
                map.put(TAG_STATE, state);

                // adding HashList to ArrayList
                documentsList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(getActivity(), documentsList,
                R.layout.row_request, new String[]{TAG_NAME, TAG_TYPE, TAG_LOCATION, TAG_PRICE, TAG_HOLDER},
                new int[]{R.id.name, R.id.type, R.id.location, R.id.price, R.id.holder});

        listView.setAdapter(adapter);
    }

}
