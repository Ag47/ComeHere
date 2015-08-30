package io.codeguy.comehere;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Clusterpoint {
    public static final String search_url = "https://api-us.clusterpoint.com/100403/android/_search.json";
    private static String jsonResponse = null;
    private static JSONObject jsonObject = null;
    private static JSONArray jsonArray = null;
    public final String url = "https://api-us.clusterpoint.com/100403/android.json";
    public final String insert_url = url;
    public final String update_url = url;
    public final String replace_url = "https://api-us.clusterpoint.com/100403/android/_replace.json";
    public final String partial_replace_url = "https://api-us.clusterpoint.com/100403/android/_partial_replace.json";
    public final String delete_url = url;
    public final String lookup_url = "https://api-us.clusterpoint.com/100403/android/_lookup.json";
    public final String retrieve_url = "https://api-us.clusterpoint.com/100403/android/_retrieve.json";
    public final String status_url = url;

    public static JSONArray search_all(Context context) {

        jsonObject = new JSONObject();
        try {
            jsonObject.put("query", "*");

        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, search_url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        jsonResponse = response.toString();
                        try {
                            jsonArray = response.getJSONArray("documents");
                        } catch (Exception e) {
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
                // Add BASIC AUTH HEADER
                Map<String, String> newHeaders = new HashMap<String, String>();
                newHeaders.putAll(headers);
                newHeaders.put("Authorization", "Basic c2VjcmV0c2lsdmVyQHltYWlsLmNvbTpydDQ3cnQ0Nw==");
                return newHeaders;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
        return jsonArray;
    }


}
