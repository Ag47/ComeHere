/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.codeguy.comehere;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import io.codeguy.comehere.Adapter.ProductInfoAdapter;
import io.codeguy.comehere.DataObject.response;

public class DetailItem extends AppCompatActivity {
    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
    public ArrayList<response> dataTOtest;
    CollapsingToolbarLayout collapsingToolbar;
    int mutedColor = R.attr.colorPrimary;
    ListView list;
    String[] productInfo;
    Integer[] imageId;
    String[] tags = {"Price :", "Holder :", "Location :"};
    TextView test;
    private TextView name, id, key, time, price, discount, shopperName, image;
    private View mImageView;
    private View mOverlayView;
    private TextView mTitleView;
    private View mFab;
    private int mActionBarSize;
    private int mFlexibleSpaceShowFabOffset;
    private int mFlexibleSpaceImageHeight;
    private int mFabMargin;
    private boolean mFabIsShown;
    private NetworkImageView icon, productImage;
    private FloatingActionButton fabInImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_item);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.black_trans80));
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("");

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.shopper1);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                mutedColor = palette.getMutedColor(R.attr.colorPrimary);
                collapsingToolbar.setContentScrimColor(mutedColor);
            }
        });


        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

//        icon = (NetworkImageView) findViewById(R.id.displayIcon);
//        test = (TextView) findViewById(R.id.test);
        id = (TextView) findViewById(R.id.displayItemId);
        name = (TextView) findViewById(R.id.displayItemName);
        key = (TextView) findViewById(R.id.displayItemKey);
        time = (TextView) findViewById(R.id.displayItemTime);
        price = (TextView) findViewById(R.id.displayProductPrice);
        image = (TextView) findViewById(R.id.displayProductImage);
        discount = (TextView) findViewById(R.id.displayProductDiscount);
        shopperName = (TextView) findViewById(R.id.displayShopperName);
        productImage = (NetworkImageView) findViewById(R.id.product_image);
        fabInImg = (FloatingActionButton) findViewById(R.id.fab_in_img);

        Bundle detailBundle = getIntent().getExtras();
        String bundleID = detailBundle.getString("clickedItemID");
        String bundleName = detailBundle.getString("clickedItemName");
        String bundleKey = detailBundle.getString("clickedItemKey");
        String bundleTime = detailBundle.getString("clickedItemTime");
        String bundleIcon = detailBundle.getString("clickedProfileIcon");
        String bundlePrice = "HKD $" + detailBundle.getString("clickedProductPrice");
        String bundleProductImage = detailBundle.getString("clickedProductImage");
        String bundleDiscount = detailBundle.getString("clickedProductDiscount");
        String bundleShopperName = detailBundle.getString("clickedShopperName");
        String bundleShopperLocation = detailBundle.getString("clickedShopperLocation");
        Log.v("DetailItem", "bundleName :" + bundleName);

        productInfo = new String[]{bundlePrice, bundleShopperName, bundleShopperLocation};
        imageId = new Integer[]{R.drawable.price_x72, R.drawable.shop_x72, R.drawable.location_x72};
        id.setText(bundleID);
        name.setText(bundleName);
        key.setText(bundleKey);
        time.setText(bundleTime);
        image.setText(bundleProductImage);
        price.setText(bundlePrice);
        discount.setText(bundleDiscount);
        shopperName.setText(bundleShopperName);
//        icon.setImageUrl(bundleIcon, imageLoader);
        productImage.setImageUrl(bundleProductImage, imageLoader);
        ProductInfoAdapter productInfoAdapter = new ProductInfoAdapter(DetailItem.this, productInfo, imageId, tags);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(productInfoAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2) {
                    Snackbar.make(view ,"you clicked ",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        fabInImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "You added a ComeHere Rate", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

}
