package io.codeguy.comehere;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
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
import java.util.Map;

import io.codeguy.comehere.Adapter.SpotProductRecyclerAdapter;
import io.codeguy.comehere.DataObject.Product;


public class SpotActivity extends AppCompatActivity {


    Toolbar toolbar;
    int fabMargin;
    int toolbarHeight;
    FrameLayout fab;
    ImageButton fabbtn;
    View fabShadow;
    boolean fadeToolbar = false;
    LinearLayout toolbarContainer;

    ArrayList<HashMap<String, String>> Item_List;
    JSONArray comeHereDB = null;
    ArrayList<Product> data = new ArrayList<>();
    CoordinatorLayout mCoordinatorLayout;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String fromSpotBundle = "card";
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_place);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tabanim_toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout mDrawerLayout;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        toolbar = (Toolbar) findViewById(R.id.tabanim_toolbar);

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
            CoordinatorLayout mCoordinator = (CoordinatorLayout) findViewById(R.id.tabanim_maincontent);

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        MainActivity.mCurrentSelectedPosition = 0;
                        startActivity(new Intent(SpotActivity.this, MainActivity.class));
                        return true;
                    case R.id.navigation_spot:
                        MainActivity.mCurrentSelectedPosition = 1;
                        return true;
                    case R.id.navigation_seek:
                        MainActivity.mCurrentSelectedPosition = 2;
                        startActivity(new Intent(SpotActivity.this, SeekActivity.class));
                        return true;
                    case R.id.navigation_request:
                        MainActivity.mCurrentSelectedPosition = 3;
                        startActivity(new Intent(SpotActivity.this, RequestActivity.class));
                        return true;
                    case R.id.navigation_promotion:
                        MainActivity.mCurrentSelectedPosition = 4;
                        startActivity(new Intent(SpotActivity.this, PromotionActivity.class));
                        return true;
                    case R.id.navigation_vendor:
                        MainActivity.mCurrentSelectedPosition = 5;
                        startActivity(new Intent(SpotActivity.this, VendorActivity.class));
                        return true;
                    case R.id.nearby:
                        MainActivity.mCurrentSelectedPosition = 6;
                        startActivity(new Intent(SpotActivity.this, NearByActivity.class));
                        return true;
                    default:
                        return true;
                }
            }
        });

        Log.v("oscar", "in the RecyclerViewActivity on create view");

        Item_List = new ArrayList<HashMap<String, String>>();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SpotProductRecyclerAdapter(this, ReadDataFromDB());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_spot, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {


    }


    private ArrayList<Product> ReadDataFromDB() {

        Log.v("spot", "on ReadDataFromDB");

        Log.v("oscar", "int the ReadDataFromDb");
        String urlSearchProductJson = "http://androiddebugoska.host22.com/products_user_spot.php?p_name=" + fromSpotBundle;
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
                params.put("p_name", fromSpotBundle);
                Log.v("spot", "in the getParams, spot :" + fromSpotBundle);
                return super.getParams();
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
        Log.v("spot", "out the volley");
        Log.v("spot", "Data size in the onResponse: " + this.data.size());
        return this.data;
    }
}
