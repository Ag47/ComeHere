package io.codeguy.comehere;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.codeguy.comehere.Adapter.PromotionGalleryAdapter;
import io.codeguy.comehere.Adapter.PromotionInfoAdapter;
import io.codeguy.comehere.DataObject.PromoImage;
import io.codeguy.comehere.DataObject.PromotionInfo;
import io.codeguy.comehere.DataObject.PromotionMarkerInfo;
import io.codeguy.comehere.GoogleMap.MainMapFragment;
import io.codeguy.comehere.GoogleMap.MultiDrawable;
import io.codeguy.comehere.Utils.DividerItemDecoration;

public class PromotionActivity extends ActionBarActivity implements OnMapReadyCallback, ClusterManager.OnClusterClickListener<PromotionMarkerInfo>, ClusterManager.OnClusterItemClickListener<PromotionMarkerInfo> {
    private static final String TAG = "DemoActivity";
    public static String clickedPinpointName;
    public static String location;
    JSONArray comeHereDB = null;
    ArrayList<PromotionInfo> dataPromotion = new ArrayList<>();
    ArrayList<PromoImage> dataPromoImage = new ArrayList<>();
    FloatingActionsMenu fabMenu;
    ClusterManager mClusterManager;
    private SlidingUpPanelLayout mLayout;
    private MainMapFragment mapFragment;
    private HashMap<Marker, PromotionMarkerInfo> promotionMarkerMap;
    private TextView textInDrag, promotionShopperName;
    private ImageView shoppeerIcon;
    private LinearLayout dragView;
    private boolean dragFromStart, isSetDragBarColor;
    private RecyclerView.Adapter promotionRecyclerAdapter;
    private RecyclerView.Adapter promotionGalleryAdapter;
    //    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView promotionInfoRecyclerView;
    private RecyclerView promotionGalleryRecyclerView;
    private ImageView expendIcon;
    private TextView locationView;
    private String imageStringArray[];
    private NetworkImageView firstImageToDisplay;
    private ImageLoader imageLoader;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promotion);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.promoStatusBar));
        }

        // set up toolbar and navdrawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.tabanim_toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout mDrawerLayout;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        toolbar = (Toolbar) findViewById(R.id.tabanim_toolbar);
        mProgressBar = (ProgressBar) findViewById(R.id.circle_laoder);
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
                        return true;
                    case R.id.navigation_spot:
                        MainActivity.mCurrentSelectedPosition = 1;
                        startActivity(new Intent(PromotionActivity.this, io.codeguy.comehere.menu_item.SpotActivity.class));
                        return true;
                    case R.id.navigation_seek:
                        MainActivity.mCurrentSelectedPosition = 2;
                        startActivity(new Intent(PromotionActivity.this, SeekActivity.class));
                        return true;
                    case R.id.navigation_request:
                        MainActivity.mCurrentSelectedPosition = 3;
                        startActivity(new Intent(PromotionActivity.this, RequestActivity.class));
                        return true;
                    case R.id.navigation_promotion:
                        MainActivity.mCurrentSelectedPosition = 4;
                        return true;
                    case R.id.navigation_vendor:
                        MainActivity.mCurrentSelectedPosition = 5;
                        startActivity(new Intent(PromotionActivity.this, VendorActivity.class));
                        return true;
                    case R.id.nearby:
                        MainActivity.mCurrentSelectedPosition = 6;
                        startActivity(new Intent(PromotionActivity.this, NearByActivity.class));
                        return true;
                    default:
                        return true;
                }
            }
        });

        dragFromStart = true;
        isSetDragBarColor = false;

        fabMenu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        fabMenu.setVisibility(View.GONE);
        locationView = (TextView) findViewById(R.id.location);
        promotionInfoRecyclerView = (RecyclerView) findViewById(R.id.promotion_info_view);
        promotionGalleryRecyclerView = (RecyclerView) findViewById(R.id.promo_gallery_recyclerview);
        expendIcon = (ImageView) findViewById(R.id.more_or_less);
        dragView = (LinearLayout) findViewById(R.id.drag_view);
        textInDrag = (TextView) findViewById(R.id.firstInitial);
        promotionShopperName = (TextView) findViewById(R.id.shopper_name);
        mapFragment = new MainMapFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.map, mapFragment);
        setSupportActionBar((Toolbar) findViewById(R.id.tabanim_toolbar));

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
//        swipe up onstart
//        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);

        mLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {


            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset + " boolean " + dragFromStart);

//                    dragView.setBackxgroundColor(Color.parseColor("#1A237E"));


                Log.v(TAG, "in silde the slideOffset");
                if (dragFromStart) {

                }
//                if(slideOffset == 0)
//                {
//                    dragView.setBackgroundColor(Color.parseColor("#ffffff"));
//                    dragFromStart = true;
//                }
                if (!dragFromStart && slideOffset < 0.5) {
                    isSetDragBarColor = true;
                    fabMenu.setVisibility(View.GONE);
                    Log.v("drag", "is set Drag bar color? true");

                }
                if (dragFromStart && slideOffset > 0) {
                    isSetDragBarColor = false;
                    Log.v("drag", "is set Drag bar color? false");
                }
                if (dragFromStart && slideOffset > 0.7)
                    fabMenu.setVisibility(View.VISIBLE);

            }

            @Override
            public void onPanelExpanded(final View panel) {
                Log.i(TAG, "onPanelExpanded");
                Integer colorFrom = getResources().getColor(R.color.dragColorFromStart);
                Integer colorTo = getResources().getColor(R.color.dragedColorFromStart);

                Integer colorFromShopperName = getResources().getColor(R.color.dragColorshopperNameFromStart);
                Integer colorToShopperName = getResources().getColor(R.color.dragedColorshopperNameFromStart);

                ValueAnimator dragingColorShopperAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFromShopperName, colorToShopperName);
                ValueAnimator dragingColorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                dragingColorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (!isSetDragBarColor) {
                            dragView.setBackgroundColor((Integer) animation.getAnimatedValue());
                        }

                    }
                });
                dragingColorShopperAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (!isSetDragBarColor) {
                            promotionShopperName.setTextColor((Integer) animation.getAnimatedValue());
                        }
                    }
                });
                dragingColorAnimation.start();
                dragingColorShopperAnimation.start();
                fadeOutExpendIcon();
                dragFromStart = false;

            }

            @Override
            public void onPanelCollapsed(View panel) {
                Log.i(TAG, "onPanelCollapsed");


                Integer colorFrom = getResources().getColor(R.color.dragColorFromEnd);
                Integer colorTo = getResources().getColor(R.color.dragedColorFromEnd);

                Integer colorFromShopperName = getResources().getColor(R.color.dragColorshopperNameFromEnd);
                Integer colorToShopperName = getResources().getColor(R.color.dragedColorshopperNameFromEnd);

                ValueAnimator dragingColorShopperAnimationFromEnd = ValueAnimator.ofObject(new ArgbEvaluator(), colorFromShopperName, colorToShopperName);
                ValueAnimator dragingColorAnimationFromEnd = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                dragingColorAnimationFromEnd.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (isSetDragBarColor) {
                            dragView.setBackgroundColor((Integer) animation.getAnimatedValue());
                        }

                    }
                });
                dragingColorShopperAnimationFromEnd.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (isSetDragBarColor) {
                            promotionShopperName.setTextColor((Integer) animation.getAnimatedValue());
                        }
                    }
                });
                dragingColorAnimationFromEnd.start();
                dragingColorShopperAnimationFromEnd.start();
                fadeInExpendIcon();
                dragFromStart = true;
            }

            @Override
            public void onPanelAnchored(View panel) {
                Log.i(TAG, "onPanelAnchored");
            }

            @Override
            public void onPanelHidden(View panel) {
                Log.i(TAG, "onPanelHidden");
            }
        });

        ft.commit();
        mapFragment.getMapAsync(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
//        may be can solve the debug that add in onResume

        Log.v("promotion", "under the finViewById");
        setUpPromotionPinPoint();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Add a marker in Sydney, Australia, and move the camera.

        LatLng ApliuStreet = new LatLng(22.328964, 114.163547);
        CameraPosition movingApliuStreet = new CameraPosition.Builder().target(ApliuStreet).zoom(16).build();

        MarkerOptions marker = new MarkerOptions().position(ApliuStreet).title("ApliuStreet");

        map.animateCamera(CameraUpdateFactory.newCameraPosition(movingApliuStreet));

    }

    private void fadeOutExpendIcon() {

        if (dragFromStart == true) {
            Animation fadeOut = new AlphaAnimation(1, 0);
            fadeOut.setInterpolator(new AccelerateInterpolator());
            fadeOut.setDuration(250);

            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    expendIcon.setVisibility(View.GONE);
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }
            });

            expendIcon.startAnimation(fadeOut);
        }
    }

    public void fadeInExpendIcon() {
        if (dragFromStart == false) {
            Animation fadeIn = new AlphaAnimation(0, 1);
            fadeIn.setInterpolator(new AccelerateInterpolator());
            fadeIn.setDuration(250);

            fadeIn.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    expendIcon.setVisibility(View.VISIBLE);
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }
            });

            expendIcon.startAnimation(fadeIn);
        }
    }

    private void setUpPromotionPinPoint() {

        if (mapFragment.getMap() != null) {
            Log.v("cluster", "in the setUpPromotionPinPoint, map not null");
        }
        mClusterManager = new ClusterManager<PromotionMarkerInfo>(this, mapFragment.getMap());
        mClusterManager.setRenderer(new PersonRenderer());
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mapFragment.getMap().setOnCameraChangeListener(mClusterManager);
        mapFragment.getMap().setOnMarkerClickListener(mClusterManager);

        PromotionMarkerInfo MingChuBldg = new PromotionMarkerInfo("Ming Chu Bldg", "15:00-18:00", new LatLng(22.330187, 114.161844), "G/F & 1/F, Ming Chu Building, Hong Kong Ming Chu Bldg"
                , R.drawable.ming_chu_bldg_shopper);
        PromotionMarkerInfo NamPontBldg = new PromotionMarkerInfo("Nam Pont Bldg", "16:00-17:00", new LatLng(22.329083, 114.163559), "128-136 Nam Cheong St Tong Mi, Apliu Street, Hong Kong"
                , R.drawable.nam_pont_bldg_shopper);

//        Marker firstMarker = mapFragment.placeMarker(MingChuBldg);
//        Marker secondMarker = mapFragment.placeMarker(NamPontBldg);
        mapFragment.placeMarker(MingChuBldg, mClusterManager);
        mapFragment.placeMarker(NamPontBldg, mClusterManager);

        mClusterManager.cluster();
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_toggle: {
                if (mLayout != null) {
                    if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN) {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                        item.setTitle(R.string.action_show);
                    } else {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        item.setTitle(R.string.action_hide);
                    }
                }
                return true;
            }
            case R.id.action_anchor: {
                if (mLayout != null) {
                    if (mLayout.getAnchorPoint() == 1.0f) {
                        mLayout.setAnchorPoint(0.7f);
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                        item.setTitle(R.string.action_anchor_disable);
                    } else {
                        mLayout.setAnchorPoint(1.0f);
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        item.setTitle(R.string.action_anchor_enable);
                    }
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    private void fetchPromotionDataFromDB() {

        Log.v("promo", "the clickedPinpointName " + clickedPinpointName);
        //work
//        String urlFetchPinpointData = "http://androiddebugoska.host22.com/fetch_all_data_in_pinpoint.php?v_shop_name=" + clickedPinpointName;
        String urlFetchPinpointData = "http://androiddebugoska.host22.com/fetch_now_promotion_data.php?v_shop_name=" + clickedPinpointName;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                urlFetchPinpointData, (String) null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
//                    mProgressBar.setVisibility(View.INVISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    mProgressBar.setLayoutParams(new TableLayout.LayoutParams(0, 0, 1f));
                    comeHereDB = response.getJSONArray("come_here");
                    Log.v("promo", "inside the fetch data function");
                    for (int i = 0; i < comeHereDB.length(); i++) {
                        PromotionInfo currentPromotionInfo = new PromotionInfo();
                        JSONObject currentJsonObject = comeHereDB.getJSONObject(i);
                        String promoID = currentJsonObject.getString("promotion_id");
                        String promoTitle = currentJsonObject.getString("title");
                        String promoContent = currentJsonObject.getString("content");
                        String promoTime = currentJsonObject.getString("time");
                        String shopperName = currentJsonObject.getString("v_shop_name");
                        String shopperAddr = currentJsonObject.getString("v_addr");
                        String shopperIcon = currentJsonObject.getString("shopper_icon");
                        if (shopperIcon == "" || shopperIcon == null)
                            shopperIcon = "";
                        else
                            shopperIcon = shopperIcon.replace("\\/", "/");

                        currentPromotionInfo.setTitle(promoTitle);
                        currentPromotionInfo.setContent(promoContent);
                        currentPromotionInfo.setPromotionID(promoID);
                        currentPromotionInfo.setShopperIcon(shopperIcon);
                        currentPromotionInfo.setTime(promoTime);
                        currentPromotionInfo.setvAddr(shopperAddr);
                        currentPromotionInfo.setvShopNName(shopperName);


                        dataPromotion.add(i, currentPromotionInfo);
//                    result.add
                        Log.v("promo", "the promotion title is :" + promoTitle);
                        Log.v("promo", "the promotion content is :" + promoContent);

                    }
                    promotionRecyclerAdapter.notifyDataSetChanged();
//                    mAdapter.notifyDataSetChanged();

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
                params.put("v_shop_name", "Nam Pont Bldg");
                Log.v("spot", "in the getParams, spot :" + clickedPinpointName);
                return super.getParams();
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
        Log.v("spot", "out the volley");
        Log.v("spot", "Data size in the onResponse: " + this.dataPromotion.size());
    }

    private void fetchPromotionImage() {
        String urlFetchPinpointData = "http://androiddebugoska.host22.com/fetch_image.php?v_shop_name=" + clickedPinpointName;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                urlFetchPinpointData, (String) null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    comeHereDB = response.getJSONArray("come_here");
                    Log.v("promo", "inside the fetch data function");
                    for (int i = 0; i < comeHereDB.length(); i++) {
                        PromoImage currentPromoImage = new PromoImage();
                        JSONObject currentJsonObject = comeHereDB.getJSONObject(i);

                        String promoImageTitle = currentJsonObject.getString("title");

                        String promoImage = currentJsonObject.getString("promo_img");
                        if (promoImage == "" || promoImage == null || promoImage == "null") {
                            promoImage = "null";
                            Log.v("promoAdapter", "The null image url's title is " + promoImageTitle);
                        } else
                            promoImage = promoImage.replace("\\/", "/");


                        currentPromoImage.setPromoImage(promoImage);
                        currentPromoImage.setPromoImageTitle(promoImageTitle);
                        dataPromoImage.add(i, currentPromoImage);
                        Log.v("promo", "in the json array, the array image " + promoImage);
                    }
                    promotionGalleryAdapter.notifyDataSetChanged();
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
                params.put("v_shop_name", "Nam Pont Bldg");
                Log.v("spot", "in the getParams, spot :" + clickedPinpointName);
                return super.getParams();
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    @Override
    public boolean onClusterClick(Cluster<PromotionMarkerInfo> cluster) {
        clickedPinpointName = cluster.getItems().iterator().next().getName();
        String clusterTime = cluster.getItems().iterator().next().getTime();

        Toast.makeText(this, cluster.getSize() + " (including " + clusterTime + ")", Toast.LENGTH_SHORT).show();

        return false;
    }

    @Override
    public boolean onClusterItemClick(PromotionMarkerInfo promotionMarkerInfo) {
        mProgressBar.setVisibility(View.VISIBLE);
//        mProgressBar.setLayoutParams(new TableLayout.LayoutParams(5,5, 1f));
        clickedPinpointName = promotionMarkerInfo.getName();
        String clusterTime = promotionMarkerInfo.getTime();
        Log.v("cluster", "you clicked " + promotionMarkerInfo.getName());
        Toast.makeText(getBaseContext(), "The Promotion time of " + clickedPinpointName + " is " + clusterTime, Toast.LENGTH_LONG).show();
        textInDrag.setVisibility(View.GONE);
        promotionShopperName.setText(clickedPinpointName);

        dataPromotion = new ArrayList<>();

        if (dataPromotion != null) {
            dataPromotion.clear();
            Log.v("promo", "cleared");
        }

        fetchPromotionDataFromDB();
        locationView.setText(promotionMarkerInfo.getShopperLocation());
        promotionRecyclerAdapter = new PromotionInfoAdapter(PromotionActivity.this, dataPromotion);
        promotionInfoRecyclerView.setAdapter(promotionRecyclerAdapter);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(PromotionActivity.this);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(PromotionActivity.this, DividerItemDecoration.VERTICAL_LIST);
        promotionInfoRecyclerView.addItemDecoration(itemDecoration);

        promotionInfoRecyclerView.setLayoutManager(mLayoutManager);
        promotionInfoRecyclerView.setHasFixedSize(true);

        dataPromoImage = new ArrayList<>();
//        fetchPromotionImage();

        if (dataPromoImage != null) {
            dataPromoImage.clear();
            Log.v("promo", "cleared");
        }
        promotionGalleryAdapter = new PromotionGalleryAdapter(PromotionActivity.this, dataPromoImage);
        promotionGalleryRecyclerView.setAdapter(promotionGalleryAdapter);
        final LinearLayoutManager galleryLayoutManer = new LinearLayoutManager(PromotionActivity.this);

        promotionGalleryRecyclerView.setLayoutManager(galleryLayoutManer);
        promotionInfoRecyclerView.setHasFixedSize(true);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        return false;
    }

    private class PersonRenderer extends DefaultClusterRenderer<PromotionMarkerInfo> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

        public PersonRenderer() {
            super(getApplicationContext(), mapFragment.getMap(), mClusterManager);
            if (mapFragment.getMap() != null) {
                Log.v("cluster", " after super, map not null");
            }
            View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

            mImageView = new ImageView(getApplicationContext());
            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(PromotionMarkerInfo promotionMarkerInfo, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
            mImageView.setImageResource(promotionMarkerInfo.getShopperIcon());
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(promotionMarkerInfo.getName()
            );
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<PromotionMarkerInfo> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (PromotionMarkerInfo p : cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size() == 4) break;
                Drawable drawable = getResources().getDrawable(p.getShopperIcon());
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);

            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }

    }
}