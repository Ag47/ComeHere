package io.codeguy.comehere.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.codeguy.comehere.Adapter.ResponseRecyclerApapter;
import io.codeguy.comehere.Network.AppController;
import io.codeguy.comehere.DataObject.response;
import io.codeguy.comehere.MainActivity;
import io.codeguy.comehere.R;


/**
 * Created by KaiHin on 7/13/2015.
 */
public class FragmentResponse extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static FrameLayout fl;
    ArrayList<HashMap<String, String>> Item_List;
    ProgressDialog PD;
    JSONArray comeHereDB = null;
//        private String urlJsonObj = "http://10.0.2.2/android/readAllResponse.php";
    View layout;
    ArrayList<response> responseData = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout responseSwipeRefreshLayout;
    private String mParam1;
    private String mParam2;
    private String urlReadResponse = "http://androiddebugoska.host22.com/readAllResponse.php";
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    public FragmentResponse() {

    }

    public static FragmentResponse newInstance(String param1, String param2, FrameLayout frameLayout) {
        fl = frameLayout;
        FragmentResponse fragment = new FragmentResponse();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        Log.v("oscar", "in the newInstance");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_response, container, false);
        initRecyclerView(layout);
        return layout;
    }


    private void initRecyclerView(View layout) {

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.response_recycler_view);
        responseSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.pending_swipe_refresh);

        responseData = new ArrayList<>();
        mAdapter = new ResponseRecyclerApapter(this.getActivity(), responseData);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new GridLayoutManager(this.getActivity() , 2);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        Log.v("pending", " after the new recyclerAdapter");
        responseSwipeRefreshLayout.setOnRefreshListener(this);
        responseSwipeRefreshLayout.post(new Runnable() {

                                            @Override
                                            public void run() {
                                                responseSwipeRefreshLayout.setRefreshing(true);

                                                fetchResponseItems();
                                                Log.v("pending", "in the refresh runnable");
                                            }
                                        }
        );
    }

    @Override
    public void onRefresh() {
        if (responseData != null) {
            responseData.clear();
        }
        fetchResponseItems();

    }

    private void fetchResponseItems() {
        Log.v("oscar", "int the ReadDataFromDb");
        responseSwipeRefreshLayout.setRefreshing(true);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlReadResponse, (String) null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("oscar", response.toString());
                ArrayList result = new ArrayList<io.codeguy.comehere.DataObject.response>();
                try {
                    comeHereDB = response.getJSONArray("come_here");
                    Log.v("oscar", "before looping");
                    for (int i = 0; i < comeHereDB.length(); i++) {
                        io.codeguy.comehere.DataObject.response currentResponseItem = new response();
                        JSONObject currentJsonObject = comeHereDB.getJSONObject(i);
                        String id = currentJsonObject.getString("response_id");
                        String name = currentJsonObject.getString("p_name");
                        String appkey = currentJsonObject.getString("app_registered_key");
                        String expired = currentJsonObject.getString("time_expired");
                        String iconThum = currentJsonObject.getString("shopper_icon");
                        String rProductPrice = currentJsonObject.getString("p_price");
                        String rProductDiscount = currentJsonObject.getString("p_discount");
                        String rProductImage = currentJsonObject.getString("p_image");
                        String shopperName = currentJsonObject.getString("v_shop_name");
                        String shopperLocation = currentJsonObject.getString("v_addr");
                        if (iconThum == "" || iconThum == null)
                            iconThum = "";
                        else
                            iconThum = iconThum.replace("\\/", "/");


                        if (shopperName == "" || shopperName == null)
                            shopperName = "";
                        else
                            shopperName = shopperName.replace("\\/", "/");


                        currentResponseItem.setIcon_thum(iconThum);
                        currentResponseItem.setName(name);
                        currentResponseItem.setId(id);
                        currentResponseItem.setTimeExpired(expired);
                        currentResponseItem.setInstalledKey(appkey);
                        currentResponseItem.setShopperName(shopperName);
                        currentResponseItem.setResponseProductPrice(rProductPrice);
                        currentResponseItem.setResponseProductImage(rProductImage);
                        currentResponseItem.setResponseProductDiscount(rProductDiscount);
                        currentResponseItem.setShopperLocation(shopperLocation);
                        Log.v("response ", "in the onResponse, the id is " + id);
                        responseData.add(i, currentResponseItem);
//                    result.add
                        Log.v("oscar", "the array responseData is " + responseData.get(i).toString());
                        Log.v("oscar", "adding...");


                    }
                    mAdapter.notifyDataSetChanged();
                    MainActivity ma = new MainActivity();
                    ma.dataResponse = responseData;
                    responseSwipeRefreshLayout.setRefreshing(false);
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

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }


    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLoingClick(View view, int position);
    }


    /**
     * Created by KaiHin on 7/10/2015.
     */

}
