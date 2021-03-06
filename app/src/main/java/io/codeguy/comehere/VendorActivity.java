package io.codeguy.comehere;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

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

import io.codeguy.comehere.Adapter.VendorAdapter;
import io.codeguy.comehere.DataObject.pending;
import io.codeguy.comehere.Network.AppController;

/**
 * Created by SILVER on 3/9/15.
 */

public class VendorActivity extends AppCompatActivity {
    JSONArray comeHereDB = null;
    ArrayList<pending> data = new ArrayList<>();
    private RecyclerView mRecyclerView;
    //    private String urlJsonObj = "http://10.0.2.2/android/readAllpending.php";
    private String urlJsonObj = "http://104.155.195.239/oska/php/comehere/readAllpending.phpq";
    private SwipeRefreshLayout pendingSwipeRefreshLayout;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);
        initRecyclerView();

        Toolbar toolbar = (Toolbar) findViewById(R.id.tabanim_toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        if (toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_drawer);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        MainActivity.mCurrentSelectedPosition = 0;
                        startActivity(new Intent(VendorActivity.this, MainActivity.class));
                        return true;
                    case R.id.navigation_spot:
                        MainActivity.mCurrentSelectedPosition = 1;
                        startActivity(new Intent(VendorActivity.this, SpotActivity.class));
                        return true;
                    case R.id.navigation_seek:
                        MainActivity.mCurrentSelectedPosition = 2;
                        startActivity(new Intent(VendorActivity.this, SeekActivity.class));
                        return true;
                    case R.id.navigation_request:
                        MainActivity.mCurrentSelectedPosition = 3;
                        startActivity(new Intent(VendorActivity.this, RequestActivity.class));
                        return true;
                    case R.id.navigation_promotion:
                        MainActivity.mCurrentSelectedPosition = 4;
                        startActivity(new Intent(VendorActivity.this, PromotionActivity.class));
                        return true;
                    case R.id.navigation_vendor:
                        MainActivity.mCurrentSelectedPosition = 5;
                        startActivity(new Intent(VendorActivity.this, VendorActivity.class));
                        return true;
                    case R.id.nearby:
                        MainActivity.mCurrentSelectedPosition = 6;
                        startActivity(new Intent(VendorActivity.this, NearByActivity.class));
                        return true;
                    default:
                        return true;
                }
            }
        });

    }


    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.pending_recycper_view);
        pendingSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.pending_swipe_refresh);

        data = new ArrayList<>();

        fetchPendingItems();
        mAdapter = new VendorAdapter(data);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);



        // init swipe to dismiss logic
        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int getPosition = viewHolder.getAdapterPosition();
                Log.v("vendor", "the row id is " + data.get(getPosition).getId());
//                deletePendingItem(Integer.toString(getPosition));
                data.remove(getPosition);
                mAdapter.notifyDataSetChanged();


            }
        });
        swipeToDismissTouchHelper.attachToRecyclerView(mRecyclerView);

    }


    private void fetchPendingItems() {

        // showing refresh animation before making http call
//        pendingSwipeRefreshLayout.setRefreshing(true);

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

                        currentPendingItem.setName(name);
                        currentPendingItem.setId(id);
                        currentPendingItem.setTimeExpired(expired);
                        currentPendingItem.setInstalledKey(appkey);
                        data.add(i, currentPendingItem);

                    }
                    mAdapter.notifyDataSetChanged();
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



    /**
     * Created by KaiHin on 7/10/2015.
     */

    private void deletePendingItem(final String id) {
        Log.v("vendor", "deleted id is " + id);
        String deleteURL = "http://104.155.195.239/oska/php/comehere/delete_pending_item.php?pending_id=" + id;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                deleteURL, (String) null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
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
                params.put("p_name", id);
                Log.v("spot", "in the getParams, spot :" + id);
                return super.getParams();
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
}


