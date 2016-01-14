package io.codeguy.comehere.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.codeguy.comehere.Adapter.MyRecyclerAdapter;
import io.codeguy.comehere.Network.AppController;
import io.codeguy.comehere.DataObject.pending;
import io.codeguy.comehere.R;


/**
 * Created by KaiHin on 7/13/2015.
 */
public class FragmentPending extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<HashMap<String, String>> Item_List;
    ProgressDialog PD;
    JSONArray comeHereDB = null;
    ArrayList<pending> data = new ArrayList<>();
    private ImageButton mImgBtn;
    private RecyclerView mRecyclerView;
    private ItemTouchHelper mItemTouchHelper;
    private String mParam1;
    private String mParam2;
    //    private String urlJsonObj = "http://10.0.2.2/android/readAllpending.php";
    private String urlJsonObj = "http://104.155.195.239/oska/php/comehere/readAllpending.php";
    private SwipeRefreshLayout pendingSwipeRefreshLayout;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout mToolbarContainer;
    private int mToolbarHeight;

    public FragmentPending() {

    }

    public static FragmentPending newInstance(String param1, String param2) {
        FragmentPending fragment = new FragmentPending();
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

        View layout = inflater.inflate(R.layout.fragment_pending, container, false);
        initRecyclerView(layout);
        return layout;
    }


    private void initRecyclerView(View layout) {
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.pending_recycper_view);
        pendingSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.pending_swipe_refresh);

        data = new ArrayList<>();
        mAdapter = new MyRecyclerAdapter(this.getActivity(), data);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        Log.v("pending", " after the new recyclerAdapter");
        pendingSwipeRefreshLayout.setOnRefreshListener(this);
        pendingSwipeRefreshLayout.post(new Runnable() {

                                           @Override
                                           public void run() {
                                               pendingSwipeRefreshLayout.setRefreshing(true);

                                               fetchPendingItems();
                                               Log.v("pending", "in the refresh runnable");
                                           }
                                       }
        );




    }

    @Override
    public void onRefresh() {
        if (data != null) {
            data.clear();
        }
        fetchPendingItems();
    }

    private void fetchPendingItems() {

        // showing refresh animation before making http call
        pendingSwipeRefreshLayout.setRefreshing(true);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                urlJsonObj, (String) null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("oscar", response.toString());
                ArrayList result = new ArrayList<pending>();
                try {
                    comeHereDB = response.getJSONArray("come_here");
                    Log.v("oscar", "before looping");
                    for (int i = 0; i < comeHereDB.length(); i++) {
                        pending currentPendingItem = new pending();
                        JSONObject currentJsonObject = comeHereDB.getJSONObject(i);
                        String id = currentJsonObject.getString("pending_id");
                        String name = currentJsonObject.getString("pending_name");
                        String appkey = currentJsonObject.getString("app_registered_key");
                        String expired = currentJsonObject.getString("time_expired");
//                        String iconThum = currentJsonObject.getString("test_shopper_thum_icon");
//                        if(iconThum == "" || iconThum == null)
//                            iconThum = "";
//                        else
//                            iconThum = iconThum.replace("\\/","/");
                        Log.v("oscar", "pending name: " + name);
//                        currentPendingItem.setIcon_thum(iconThum);
                        currentPendingItem.setName(name);
                        currentPendingItem.setId(id);
                        currentPendingItem.setTimeExpired(expired);
                        currentPendingItem.setInstalledKey(appkey);
//                        pending p = new pending(name, id , appkey, expired);
                        data.add(i, currentPendingItem);
//                        data.add(i, p);

//                    result.add
                        Log.v("oscar", "the array data is " + data.get(i).toString());
                        Log.v("oscar", "adding...");

                    }
                    mAdapter.notifyDataSetChanged();
                    pendingSwipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("oscar", "onError Response: " + error.toString());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
        Log.v("pending", "the final data size is " + this.data.size());
    }


    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLoingClick(View view, int position);
    }


    /**
     * Created by KaiHin on 7/10/2015.
     */

}
