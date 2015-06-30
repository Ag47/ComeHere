package io.codeguy.comehere.menu_item;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.codeguy.comehere.MainActivity;
import io.codeguy.comehere.MySingleton;
import io.codeguy.comehere.R;

public class VendorFragment extends Fragment {
    private SwipeRefreshLayout swipeContainer;

    String search_url = String.format("https://api-us.clusterpoint.com/100403/ComeHere/_search.json");
    String replace_url = String.format("https://api-us.clusterpoint.com/100403/ComeHere/_partial_replace.json");

    private static final String TAG_doc = "documents";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";

    JSONObject jsonObject = null;
    public static JSONObject responseObject = null;

    // contacts JSONArray
    JSONArray documents = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> documentsList;

    public VendorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_vendor, container, false);

        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {

                // Your code to refresh the list here.

                // Make sure you call swipeContainer.setRefreshing(false)

                // once the network request has completed successfully.

//                fetchTimelineAsync(0);

            }

        });

        // Configure the refreshing colors

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,

                android.R.color.holo_green_light,

                android.R.color.holo_orange_light,

                android.R.color.holo_red_light);


        jsonObject = new JSONObject();
        try {
            jsonObject.put("query", "<state>0</state>");
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
        int[] colors = {0xFFAED7DA, 0xFF1DAFB2, 0xFFAED7DA};
        lv.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        lv.setDividerHeight(5);        try {
            // Getting array of documents
            documents = responseObject.getJSONArray("documents");

            // Looping through All documents
            for (int i = 0; i < documents.length(); i++) {
                JSONObject currentJsonObject = documents.getJSONObject(i);

                // Storing each json item in variable
                String id = currentJsonObject.getString(TAG_ID);
                MainActivity.extraId[i] = id;
                String name = currentJsonObject.getString(TAG_NAME);

                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value
                map.put(TAG_ID, id);
                map.put(TAG_NAME, name);

                // adding HashList to ArrayList
                documentsList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(getActivity(), documentsList,
                R.layout.row, new String[]{TAG_NAME}, new int[]{R.id.name});
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage("Accept Request?");
                builder1.setCancelable(true);
//                Log.i("ididididid", MainActivity.extraId[(int) id]);
                final String idYeah = MainActivity.extraId[(int) id];
                builder1.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                JSONObject jsonObject1 = new JSONObject();
                                try {
                                    Log.i("idYeah", idYeah);
                                    jsonObject.put("query", "<id>" + idYeah + "</id><state>1</state>");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, replace_url, jsonObject1,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Log.i("replace sucess", "success");
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
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });
    }

    public void fetchTimelineAsync(int page) {

//        client.getHomeTimeline(0, new JsonHttpResponseHandler() {
//
//            public void onSuccess(JSONArray json) {
//
//                // Remember to CLEAR OUT old items before appending in the new ones
//
//                adapter.clear();
//
//                // ...the data has come back, add new items to your adapter...
//
//                adapter.addAll();
//
//                // Now we call setRefreshing(false) to signal refresh has finished
//
//                swipeContainer.setRefreshing(false);
//
//            }
//
//
//
//            public void onFailure(Throwable e) {
//
//                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
//
//            }
//
//        });

    }
}