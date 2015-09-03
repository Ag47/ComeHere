package io.codeguy.comehere;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

import io.codeguy.comehere.Adapter.ResultSearchAdapter;
import io.codeguy.comehere.DataObject.Product;

/**
 * Created by KaiHin on 8/25/2015.
 */
public class SeekResultActivity extends AppCompatActivity {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static ArrayList<Product> searchProduct;
    private String mParam1;
    private String mParam2;
    private RecyclerView resultRecyclerVIew;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private FloatingActionButton mapFab;
    private String intentExtraType;

    JSONArray comeHereDB = null;
    ArrayList<Product> data = new ArrayList<>();


    public static SeekResultActivity newInstance(String param1, String param2, ArrayList<Product> getSearchProduct) {
        SeekResultActivity fragment = new SeekResultActivity();
        Log.v("result", "the array list size is in newinstance" + getSearchProduct.size());
        searchProduct = getSearchProduct;
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek_result);
        intentExtraType = getIntent().getStringExtra("type");
        Log.i("intentExtra", intentExtraType);
        ReadDataFromDB(intentExtraType);
        initRecyclerView();

    }

    private void initRecyclerView() {
        resultRecyclerVIew = (RecyclerView) findViewById(R.id.result_recycler_view);
        mapFab = (FloatingActionButton) findViewById(R.id.fab_map);
        mAdapter = new ResultSearchAdapter(this, searchProduct);
        mLayoutManager = new LinearLayoutManager(this);
        resultRecyclerVIew.setLayoutManager(mLayoutManager);
        resultRecyclerVIew.setHasFixedSize(true);
        resultRecyclerVIew.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();

//        mapFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getActivity().getApplicationContext(), MapProductActivity.class);
//                Bundle toMap = new Bundle();
//                toMap.putSerializable("listResult", searchProduct);
//                i.putExtras(toMap);
//                startActivity(i);
//            }
//        });
    }

    private void ReadDataFromDB(final String type) {

        Log.v("spot", "on ReadDataFromDB");

        Log.v("oscar", "int the ReadDataFromDb");
        String urlSearchProductJson = "http://androiddebugoska.host22.com/products_user_seek.php?type=" + type;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                urlSearchProductJson, (String) null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("oscar", response.toString());
                ArrayList result = new ArrayList<Product>();
                try {
                    comeHereDB = response.getJSONArray("come_here");
                    Log.v("oscar", "before looping");
                    for (int i = 0; i < comeHereDB.length(); i++) {
                        Product currentProductItem = new Product();
                        JSONObject currentJsonObject = comeHereDB.getJSONObject(i);
                        String pid = currentJsonObject.getString("p_id");
                        String pName = currentJsonObject.getString("p_name");
                        String pPrice = currentJsonObject.getString("p_price");
                        String imageURL = currentJsonObject.getString("p_image");
                        String pDiscount = currentJsonObject.getString("p_discount");
                        String pTypeId = currentJsonObject.getString("type_id");
                        String pTypeName = currentJsonObject.getString("type_name");
                        String pVendorId = currentJsonObject.getString("v_id");
                        String pVendorName = currentJsonObject.getString("v_shop_name");
                        String latitude = currentJsonObject.getString("latitude");
                        String longitude = currentJsonObject.getString("longitude");
                        if (imageURL == "" || imageURL == null)
                            imageURL = "";
                        else
                            imageURL = imageURL.replace("\\/", "/");

                        currentProductItem.setpName(pName);
                        currentProductItem.setPid(pid);
                        currentProductItem.setImaageURL(imageURL);
                        currentProductItem.setpDiscount(pDiscount);
                        currentProductItem.setpPrice(pPrice);
                        currentProductItem.setpTypeId(pTypeId);
                        currentProductItem.setpTypeName(pTypeName);
                        currentProductItem.setpVendorId(pVendorId);
                        currentProductItem.setpVendorName(pVendorName);
                        currentProductItem.setLatitude(latitude);
                        currentProductItem.setLatitude(latitude);

                        data.add(i, currentProductItem);
//                    result.add
                        Log.v("spot", " in the onResponse, the price is :" + pPrice);
                    }
                    mAdapter.notifyDataSetChanged();
                    MainActivity ma = new MainActivity();
                    ma.dataSpotProduct = data;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("oscar", "onError Response: " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("type", type);
//                Log.v("spot", "in the getParams, spot :" + fromSpotBundle);
                return super.getParams();
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
        Log.v("spot", "out the volley");
        Log.v("spot", "Data size in the onResponse: " + this.data.size());
        searchProduct = data;
    }
}
