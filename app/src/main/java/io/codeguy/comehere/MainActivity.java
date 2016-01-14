package io.codeguy.comehere;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

import io.codeguy.comehere.DataObject.Product;
import io.codeguy.comehere.DataObject.pending;
import io.codeguy.comehere.DataObject.response;
import io.codeguy.comehere.Fragment.FragmentSharedHomePage1;

public class MainActivity extends AppCompatActivity {

    private static final String PREFERENCES_FILE = "mymaterialapp_settings";
    public static String MKEY;
    private final String PREFS_NAME = "MyPrefsFile";
    public ArrayList<pending> data = new ArrayList<>();
    public ArrayList<response> dataResponse = new ArrayList<>();
    public ArrayList<Product> dataSpotProduct = new ArrayList<>();
    NavigationView mNavigationView;
    private Toolbar toolbar;
    /**
     * this is setting up the navigation bar
     */
    private DrawerLayout mDrawerLayout;
    public static int mCurrentSelectedPosition;

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        responseNum = (TextView) findViewById(R.id.response_num);
        if (android.os.Build.VERSION.SDK_INT >= 21)
            setupTransitionAnimation();
        else
            setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tabanim_toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        setUpNavDrawer();


        // MKey Login
        mkeyLogin();


        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            CoordinatorLayout mCoordinator = (CoordinatorLayout) findViewById(R.id.tabanim_maincontent);

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        mCurrentSelectedPosition = 0;
                        return true;
                    case R.id.navigation_spot:
                        mCurrentSelectedPosition = 1;
                        startActivity(new Intent(MainActivity.this, SpotActivity.class));
                        return true;
                    case R.id.navigation_seek:
                        mCurrentSelectedPosition = 2;
                        startActivity(new Intent(MainActivity.this, SeekActivity.class));
                        return true;
                    case R.id.navigation_request:
                        mCurrentSelectedPosition = 3;
                        startActivity(new Intent(MainActivity.this, RequestActivity.class));
                        return true;
                    case R.id.navigation_promotion:
                        mCurrentSelectedPosition = 4;
                        startActivity(new Intent(MainActivity.this, PromotionActivity.class));
                        return true;
                    case R.id.navigation_vendor:
                        mCurrentSelectedPosition = 5;
                        startActivity(new Intent(MainActivity.this, VendorActivity.class));
                        return true;
                    case R.id.nearby:
                        mCurrentSelectedPosition = 6;
                        startActivity(new Intent(MainActivity.this, NearByActivity.class));
                    default:
                        return true;
                }
            }
        });

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


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawer, menu);

        //Set icon for the menu button
        Drawable icon = getResources().getDrawable(R.drawable.ic_vender);
        menu.getItem(0).setIcon(icon);
        menu.getItem(1).setIcon(icon);
        menu.getItem(2).setIcon(icon);
        menu.getItem(3).setIcon(icon);


        return true;
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


    private void setupTransitionAnimation() {
        setContentView(R.layout.activity_main_v21up);
        setupWindowAnimations();
        setupLayout();
    }


    private void setupWindowAnimations() {
        // We are not interested in defining a new Enter Transition. Instead we change default transition duration
        if (android.os.Build.VERSION.SDK_INT >= 21)
            getWindow().getEnterTransition().setDuration(getResources().getInteger(R.integer.anim_duration_long));
    }
    private void setupLayout() {
        if (android.os.Build.VERSION.SDK_INT >= 21) {

            Fade fadeIn = new Fade();
            fadeIn.setDuration(getResources().getInteger(R.integer.anim_duration_long));
//            Slide slideTransition = new Slide(Gravity.LEFT);
//            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
//             Create fragment and define some of it transitions
            FragmentSharedHomePage1 sharedElementFragment1 = FragmentSharedHomePage1.newInstance();
            sharedElementFragment1.setReenterTransition(fadeIn);
            sharedElementFragment1.setExitTransition(fadeIn);
            sharedElementFragment1.setSharedElementEnterTransition(new ChangeBounds());
            Log.v("transition", "1");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.shared_content, sharedElementFragment1)
                    .commit();
        }
    }




}