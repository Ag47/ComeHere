package io.codeguy.comehere;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import io.codeguy.comehere.DataObject.Product;
import io.codeguy.comehere.Fragment.FragmentFindResult;
import io.codeguy.comehere.Fragment.FragmentWish;

/**
 * Created by KaiHin on 8/25/2015.
 */
public class FindResultAndAddActivity extends AppCompatActivity {

    private static String inputSearch;
    private static int sizeArrayList;
    private static ArrayList<Product> resultSearchProduct;
    public ViewPagerAdapter mViewPagerAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_add_activity);

        Bundle getResult = getIntent().getExtras();
        inputSearch = getResult.getString("search");
        resultSearchProduct = (ArrayList<Product>) getResult.getSerializable("listResult");
        sizeArrayList = resultSearchProduct.size();

        setUpToolbar();
        setUpViewPager();
    }

    private void setUpViewPager() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.tabanim_viewpager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabanim_tabs);
        viewPager.setAdapter(mViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tabanim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Find: " + inputSearch);
    }


    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private static final String[] TITLES = new String[]
                {"Result( " + sizeArrayList + " )", "Add To Wish"};
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

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
                    f = new FragmentFindResult().newInstance("", "", resultSearchProduct);
                    break;
                case 1:
                    f = new FragmentWish().newInstance("", "");
                    break;
                default:
                    break;
            }
            return f;
        }

    }

}
