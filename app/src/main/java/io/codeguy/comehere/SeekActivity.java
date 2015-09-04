package io.codeguy.comehere;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class SeekActivity extends AppCompatActivity {

    NavigationView mNavigationView;
    private int mCurrentSelectedPosition;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_seek);

        MainActivity.mCurrentSelectedPosition = 2;

        toolbar = (Toolbar) findViewById(R.id.tabanim_toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
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

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            CoordinatorLayout mCoordinator = (CoordinatorLayout) findViewById(R.id.tabanim_maincontent);

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        MainActivity.mCurrentSelectedPosition = 0;
                        startActivity(new Intent(SeekActivity.this, MainActivity.class));
                        return true;
                    case R.id.navigation_spot:
                        MainActivity.mCurrentSelectedPosition = 1;
                        startActivity(new Intent(SeekActivity.this, io.codeguy.comehere.menu_item.SpotActivity.class));
                        return true;
                    case R.id.navigation_seek:
                        MainActivity.mCurrentSelectedPosition = 2;
                        startActivity(new Intent(SeekActivity.this, SeekActivity.class));
                        return true;
                    case R.id.navigation_request:
                        MainActivity.mCurrentSelectedPosition = 3;
                        startActivity(new Intent(SeekActivity.this, RequestActivity.class));
                        return true;
                    case R.id.navigation_promotion:
                        MainActivity.mCurrentSelectedPosition = 4;
                        startActivity(new Intent(SeekActivity.this, PromotionActivity.class));
                        return true;
                    case R.id.navigation_vendor:
                        MainActivity.mCurrentSelectedPosition = 5;
                        startActivity(new Intent(SeekActivity.this, VendorActivity.class));
                        return true;
                    default:
                        return true;
                }
            }
        });

    }

    public void cable(View view) {
        startActivity(new Intent(getApplicationContext(), SeekResultActivity.class).putExtra("type", "cable"));
    }

    public void smartphone(View view) {
        startActivity(new Intent(getApplicationContext(), SeekResultActivity.class).putExtra("type", "smartphone"));
    }

    public void handtools(View view) {
        startActivity(new Intent(getApplicationContext(), SeekResultActivity.class).putExtra("type", "handtools"));
    }

    public void lightbulb(View view) {
        startActivity(new Intent(getApplicationContext(), SeekResultActivity.class).putExtra("type", "lightbulb"));
    }

    public void watch(View view) {
        startActivity(new Intent(getApplicationContext(), SeekResultActivity.class).putExtra("type", "watch"));
    }

    public void tv(View view) {
        startActivity(new Intent(getApplicationContext(), SeekResultActivity.class).putExtra("type", "tv"));
    }

    public void tnd(View view) {
        startActivity(new Intent(getApplicationContext(), SeekResultActivity.class).putExtra("type", "tnd"));
    }

    public void msp(View view) {
        startActivity(new Intent(getApplicationContext(), SeekResultActivity.class).putExtra("type", "msp"));
    }

    public void tvwall(View view) {
        startActivity(new Intent(getApplicationContext(), SeekResultActivity.class).putExtra("type", "tvwall"));
    }
}