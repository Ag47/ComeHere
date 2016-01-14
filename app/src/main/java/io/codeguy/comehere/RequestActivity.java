package io.codeguy.comehere;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.codeguy.comehere.DataObject.Product;
import io.codeguy.comehere.DataObject.pending;
import io.codeguy.comehere.DataObject.response;
import io.codeguy.comehere.Fragment.FragmentPending;
import io.codeguy.comehere.Fragment.FragmentResponse;
import io.codeguy.comehere.Fragment.NavSpot;


/**
 * Another implementation of ViewPagerTabActivity.
 * This uses TouchInterceptionFrameLayout to move Fragments.
 * <p/>
 * SlidingTabLayout and SlidingTabStrip are from google/iosched:
 * https://github.com/google/iosched
 */
public class RequestActivity extends AppCompatActivity {
    public ArrayList<pending> data = new ArrayList<>();
    public ArrayList<response> dataResponse = new ArrayList<>();
    public ArrayList<Product> dataSpotProduct = new ArrayList<>();
    private View mToolbarView;
    private ViewPager mPager;
    //    private NavigationAdapter mPagerAdapter;
    private int mSlop;
    private boolean mScrolled;
    public ViewPagerAdapter mViewPagerAdapter;
    private Toolbar toolbar;
    private NavSpot navSop;
    public static FrameLayout fl;
    /**
     * this is setting up the navigation bar
     */
    private DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    private static final String PREFERENCES_FILE = "mymaterialapp_settings";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    FragmentManager fm;
    FragmentTransaction transaction;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private int mCurrentSelectedPosition;

    /**
     * ---------------------------------------
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        setUpToolbar();
//        ArrayList<pending> temp = ReadDataFromDB();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);

        fl = (FrameLayout) findViewById(R.id.mFrameLayout);

        mUserLearnedDrawer = Boolean.valueOf(readSharedSetting(this, PREF_USER_LEARNED_DRAWER, "false"));

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
        final NavSpot navSpot = NavSpot.newInstance("", "");
        setUpNavDrawer();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            CoordinatorLayout mCoordinator = (CoordinatorLayout) findViewById(R.id.tabanim_maincontent);

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                menuItem.setChecked(true);
                Fragment f = new Fragment();
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        mCurrentSelectedPosition = 0;
                        startActivity(new Intent(RequestActivity.this, MainActivity.class));
                        return true;
                    case R.id.navigation_spot:
                        mCurrentSelectedPosition = 1;
                        startActivity(new Intent(RequestActivity.this, SpotActivity.class));
                        return true;
                    case R.id.navigation_seek:
                        mCurrentSelectedPosition = 2;
                        startActivity(new Intent(RequestActivity.this, SeekActivity.class));
                        return true;
                    case R.id.navigation_request:
                        mCurrentSelectedPosition = 3;
                        return true;
                    case R.id.navigation_promotion:
                        mCurrentSelectedPosition = 4;
                        startActivity(new Intent(RequestActivity.this, PromotionActivity.class));
                        return true;
                    case R.id.navigation_vendor:
                        mCurrentSelectedPosition = 5;
                        startActivity(new Intent(RequestActivity.this, VendorActivity.class));
                        return true;
                    case R.id.nearby:
                        MainActivity.mCurrentSelectedPosition = 6;
                        startActivity(new Intent(RequestActivity.this, NearByActivity.class));
                        return true;
                    default:
                        return true;
                }
            }
        });


        ViewPager viewPager = (ViewPager) findViewById(R.id.tabanim_viewpager);
//        setupViewPager(viewPager);

        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabanim_tabs);
        viewPager.setAdapter(mViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    private NavSpot getFragment() {
        NavSpot f = (NavSpot) getSupportFragmentManager().findFragmentByTag("SET_A_TAG");
        if (f == null) {
            f = new NavSpot();
        }
        return f;
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tabanim_toolbar);
        setSupportActionBar(toolbar);
    }

    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        private static final String[] TITLES = new String[]
                {"Pending", "Response"};
//


//        @Override
//        public Fragment getItem(int position) {
//            return mFragmentList.get(position);
//        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = new Fragment();
            switch (position) {
                case 0:
                    f = new FragmentPending().newInstance("http://104.155.195.239/oska/php/comehere/readAllpending.php", "");
                    break;
                case 1:
                    f = new FragmentResponse().newInstance("", "", fl);

            }
            return f;
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

        if (!mUserLearnedDrawer) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            mUserLearnedDrawer = true;
            saveSharedSetting(this, PREF_USER_LEARNED_DRAWER, "true");
        }

    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION, 0);
        Menu menu = mNavigationView.getMenu();
        menu.getItem(mCurrentSelectedPosition).setChecked(true);
    }


}

