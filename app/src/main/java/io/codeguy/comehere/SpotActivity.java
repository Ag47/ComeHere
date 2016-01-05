package io.codeguy.comehere;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import io.codeguy.comehere.Adapter.SpotPendingAdapter;
import io.codeguy.comehere.DataObject.Product;
import io.codeguy.comehere.Network.AppController;

/**
 * Created by KaiHin on 8/23/2015.
 */
public class SpotActivity extends AppCompatActivity {
    int i = 1;
    private ArrayList<Product> searchProduct = new ArrayList<>();
    private RecyclerView instantRecycler;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private CoordinatorLayout mCoordinatorLayout;
    private JSONArray comeHereDB = null;
    private EditText inputSearch;
    private TextView searchContent;
    private Toolbar toolbar;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edtSeach;
    private LinearLayout searchContentRow;
    private String searchContentString;
    NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instant_search);

        searchContentRow = (LinearLayout) findViewById(R.id.search_content_row);
        searchContent = (TextView) findViewById(R.id.search_content);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Spot");
        instantRecycler = (RecyclerView) findViewById(R.id.instant_search_recycler);

        mLayoutManager = new LinearLayoutManager(this);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        instantRecycler.setLayoutManager(layoutManager);

        searchContentRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "you want to find " + searchContentString, Snackbar.LENGTH_LONG).show();
                Intent i = new Intent(SpotActivity.this, FindResultAndAddActivity.class);
                Bundle resultBundle = new Bundle();
                resultBundle.putSerializable("listResult", searchProduct);
                resultBundle.putString("search", searchContentString);
                i.putExtras(resultBundle);
                startActivity(i);
            }
        });


        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        setUpNavDrawer();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
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
                    default:
                        return true;
                }
            }
        });
    }

    private void instantSearch(CharSequence inputSearch) {
        searchProduct.clear();
        searchProduct = new ArrayList<>();
        Log.v("instant", "count i " + i);
        fetchInstantSearch(inputSearch);
        mAdapter = new SpotPendingAdapter(this, searchProduct);
        instantRecycler.setAdapter(mAdapter);
        instantRecycler.setLayoutManager(mLayoutManager);
        instantRecycler.setHasFixedSize(true);
    }


    private void fetchInstantSearch(CharSequence inputSearch) {
        final String inputSearchString = inputSearch.toString();
        String urlSearchProductJson = "http://androiddebugoska.host22.com/products_user_spot.php?p_name=" + inputSearchString;
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
                        String shopperAddr = currentJsonObject.getString("v_addr");
                        if (imageURL == "" || imageURL == null)
                            imageURL = "";
                        else
                            imageURL = imageURL.replace("\\/", "/");
                        currentProductItem.setpVendorAddr(shopperAddr);
                        currentProductItem.setpName(pName);
                        currentProductItem.setPid(pid);
                        currentProductItem.setImaageURL(imageURL);
                        currentProductItem.setpDiscount(pDiscount);
                        currentProductItem.setpPrice(pPrice);
                        currentProductItem.setpTypeId(pTypeId);
                        currentProductItem.setpTypeName(pTypeName);
                        currentProductItem.setpVendorId(pVendorId);
                        currentProductItem.setpVendorName(pVendorName);

                        searchProduct.add(i, currentProductItem);
//                    result.add
                        Log.v("spot", " in the onResponse, the price is :" + pPrice);
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
        }) {
            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("p_name", inputSearchString);
                return super.getParams();
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void hotProduct() {
        Log.v("instant", "init hot product");
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                handleMenuSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void handleMenuSearch() {
        ActionBar action = getSupportActionBar(); //get the actionbar

        if (isSearchOpened) { //test if the search is open

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

            //hides the keyboard
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);

            //add the search icon in the action bar
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search_white_24dp));

            isSearchOpened = false;
            searchContentString = "";
            searchContentRow.setVisibility(View.GONE);
        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.search_bar);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

            edtSeach = (EditText) action.getCustomView().findViewById(R.id.edtSearch); //the text editor

            edtSeach.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (searchProduct != null) {
//                        searchProduct.clear();
                        searchProduct = new ArrayList<>();
                        Log.v("instant", "count i " + i);
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.v("instant", "search bar :" + s.toString());
                    Log.v("instant", "start :" + start);
                    Log.v("instant", "before" + before);
//                    if (searchProduct != null) {
                        searchProduct.clear();
                        searchProduct = new ArrayList<>();
//                    }
                    searchContentRow.setVisibility(View.VISIBLE);
                    searchContentString = s.toString();
                    searchContent.setText(s.toString());
                    if (before == 1 && start == 0) {
                        searchContentRow.setVisibility(View.GONE);
                    }
                    instantSearch(s);
                }

                @Override
                public void afterTextChanged(Editable s) {
//                    if (searchProduct != null) {
//                        searchProduct.clear();
//                        searchProduct = new ArrayList<>();
//                    }
//                    searchContentRow.setVisibility(View.VISIBLE);
//                    searchContentString = s.toString();
//                    searchContent.setText(s.toString());
//                    if (before == 1 && start == 0) {
//                        searchContentRow.setVisibility(View.GONE);
//                    }
                    instantSearch(s);
//                    s.toString();
                }

            });

            edtSeach.requestFocus();

            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);

            //add the close icon
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_close_search));
            isSearchOpened = true;
        }
    }

    private void setUpNavDrawer() {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
