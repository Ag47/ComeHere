package io.codeguy.comehere;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import io.codeguy.comehere.Network.AppController;

/**
 * Created by KaiHin on 9/2/2015.
 */
public class SearchProductDetail extends AppCompatActivity {

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
    private NetworkImageView icon, productImage;
    private FloatingActionButton fabInImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_item);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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

        name = (TextView) findViewById(R.id.displayItemName);
        price = (TextView) findViewById(R.id.displayProductPrice);
        shopperName = (TextView) findViewById(R.id.displayShopperName);
        productImage = (NetworkImageView) findViewById(R.id.product_image);
        fabInImg = (FloatingActionButton) findViewById(R.id.fab_in_img);

        Bundle detailBundle = getIntent().getExtras();
        String bundleName = detailBundle.getString("searchProductName");
        String bundlePrice = "HKD $" + detailBundle.getString("searchPRoductPrice");
        String bundleShopperName = detailBundle.getString("searchProductShopperName");
        String bundleShopperLocation = detailBundle.getString("searchProductLocation");
        String bundleProductImage = detailBundle.getString("searchProductImage");
        Log.v("DetailItem", "bundleName :" + bundleName);

        productInfo = new String[]{bundlePrice, bundleShopperName, bundleShopperLocation};
        imageId = new Integer[]{R.drawable.price_x72, R.drawable.shop_x72, R.drawable.location_x72};
        name.setText(bundleName);
        price.setText(bundlePrice);
        shopperName.setText(bundleShopperName);
//        icon.setImageUrl(bundleIcon, imageLoader);
        productImage.setImageUrl(bundleProductImage, imageLoader);
        ProductInfoAdapter productInfoAdapter = new ProductInfoAdapter(SearchProductDetail.this, productInfo, imageId, tags);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(productInfoAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2) {
                    Snackbar.make(view, "you clicked ", Snackbar.LENGTH_SHORT).show();

                    double dest_lat = 22.330423;
                    double dest_long = 114.161890;
                    String uri = "http://maps.google.com/maps?daddr=" + dest_lat + "," + dest_long;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
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
