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
                        mCurrentSelectedPosition = 0;
                        return true;
                    case R.id.navigation_spot:
                        mCurrentSelectedPosition = 1;
                        startActivity(new Intent(SeekActivity.this, io.codeguy.comehere.menu_item.SpotActivity.class));
                        return true;
                    case R.id.navigation_seek:
                        mCurrentSelectedPosition = 2;
                        startActivity(new Intent(SeekActivity.this, SeekActivity.class));
                        return true;
                    case R.id.navigation_request:
                        mCurrentSelectedPosition = 3;
                        startActivity(new Intent(SeekActivity.this, RequestActivity.class));
                        return true;
                    case R.id.navigation_promotion:
                        mCurrentSelectedPosition = 4;
                        startActivity(new Intent(SeekActivity.this, PromotionActivity.class));
                        return true;
                    case R.id.navigation_vendor:
                        mCurrentSelectedPosition = 5;
//                        startActivity(new Intent(SeekActivity.this, SpotAddPendingActivity.class));
                        return true;
                    default:
                        return true;
                }
            }
        });

    }
}