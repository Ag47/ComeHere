package io.codeguy.comehere;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.clusterpoint.api.CPSConnection;
import com.clusterpoint.api.request.CPSInsertRequest;
import com.clusterpoint.api.response.CPSModifyResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import io.codeguy.comehere.adapter.NavDrawerListAdapter;
import io.codeguy.comehere.menu_item.HomeFragment;
import io.codeguy.comehere.menu_item.PromotionFragment;
import io.codeguy.comehere.menu_item.RequestFragment;
import io.codeguy.comehere.menu_item.SeekFragment;
import io.codeguy.comehere.menu_item.SpotFragment;
import io.codeguy.comehere.menu_item.VendorFragment;
import io.codeguy.comehere.model.NavDrawerItem;


public class MainActivity extends Activity {

    String url = String.format("https://api-us.clusterpoint.com/100403/android.json");
    String search_url = String.format("https://api-us.clusterpoint.com/100403/android/_search.json");

    public static String[] extraId = new String[100];

    public static String fragmentExtra;
    public static String MKEY;

    private static final String TAG_doc = "documents";
    private static final String TAG_ID = "id";
    private static final String TAG_COL1 = "col1";
    private static final String TAG_COL2 = "col2";

    JSONObject jsonObject = null;
    public static JSONObject responseObject = null;

    // contacts JSONArray
    JSONArray documents = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> documentsList;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    private final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final FragmentManager fragmentManager = getFragmentManager();
//        if (savedInstanceState == null) {
//
//            fragmentManager.beginTransaction().add(R.id.fragment_container, new InitFragment()).commit();
//            Thread timer = new Thread() {
//                public void run() {
//                    try {
//                        sleep(2000);
//
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } finally {
//                        fragmentManager.beginTransaction().add(R.id.fragment_container, new HomeFragment()).commit();
//                    }
//
//                }
//            };
//            timer.start();
//
//        }
        setContentView(R.layout.activity_main);
        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
//        mDrawerList.setBackground(getResources().getDrawable(R.color.theme_blue));
        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
        // Pages
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // What's hot, We  will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));

//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));


        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
//                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
//                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }


//        MapFragment mapFragment = (MapFragment) getFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
        this.getActionBar().setBackgroundDrawable(this.getResources().getDrawable(R.drawable.actionbar));


        // MKey Login
        mkeyLogin();


    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            Log.i("position", Integer.toString(position));
            displayView(position);
        }
    }

    private void mkeyLogin() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        if (settings.getBoolean("first_time", true)) {
            //the app is being launched for first time, do something
            Random tmpRand = new Random();
            String deviceId = String.valueOf(Math.abs(tmpRand.nextLong()) % 1000);
            MKEY = deviceId;
            editor.putString("MKEY", deviceId);
            editor.putBoolean("first_time", false);
            editor.commit();
            Log.i("settingbool", "firsttime");
            Log.i("MEKY", MKEY);
        } else {
            MKEY = settings.getString("MKEY", "N/A");
            Log.i("MEKY", MKEY);
            Log.i("settingbool", "secondtime");
        }
    }

    private void initNavigationBar(Bundle savedInstanceState) {

    }


    /* *
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new SpotFragment();
                break;
            case 2:
                fragment = new SeekFragment();
                break;
            case 3:
                Intent intent = new Intent(this, RequestFragment.class);
                startActivity(intent);
                break;
            case 4:
                fragment = new PromotionFragment();
                break;
            case 5:
                fragment = new VendorFragment();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);

            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    /**
     * unused method ** DO NOT DELETE
     */

    // Clusterpoint connection example but not working
    public void clusterpointJavaConnection() {
        try {
            CPSConnection conn = new CPSConnection("tcp://cloud-us-0.clusterpoint.com:9007", "comehere", "secretsilver@ymail.com", "rt47rt47", "100403", "document", "//document/id");

            // Inserting test documents.
            List<String> docs = new ArrayList<String>();
            docs.add("<document><id>id1</id><title>Test document 1</title><body>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam a nisl magna</body></document>");
            docs.add("<document><id>id2</id><title>Test document 2</title><body>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam a nisl magna</body></document>");
            docs.add("<document><id>id3</id><title>Test document 3</title><body>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam a nisl magna</body></document>");
            docs.add("<document><id>id5</id><title>Test document 4</title><body>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam a nisl magna</body></document>");

            //Create Insert request
            CPSInsertRequest insert_req = new CPSInsertRequest();
            //Add documents to request
            insert_req.setStringDocuments(docs);
            //Send request
            CPSModifyResponse insert_resp = (CPSModifyResponse) conn.sendRequest(insert_req);
            //Print out inserted document ids
            Log.i("Inserted ids: ", Arrays.toString(insert_resp.getModifiedIds()));

            //Close connection
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enableStrictMode() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

}