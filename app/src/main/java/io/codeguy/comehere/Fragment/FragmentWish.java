package io.codeguy.comehere.Fragment;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wunderlist.slidinglayer.LayerTransformer;
import com.wunderlist.slidinglayer.SlidingLayer;
import com.wunderlist.slidinglayer.transformer.AlphaTransformer;
import com.wunderlist.slidinglayer.transformer.RotationTransformer;
import com.wunderlist.slidinglayer.transformer.SlideJoyTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.codeguy.comehere.Adapter.BottomSheetAdapter;
import io.codeguy.comehere.AppController;
import io.codeguy.comehere.DataObject.BottomSheetItem;
import io.codeguy.comehere.R;
import io.codeguy.comehere.RequestActivity;

/**
 * Created by KaiHin on 8/25/2015.
 */
public class FragmentWish extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    int screenHeight = 0;
    private String mParam1;
    private String mParam2;
    private Button btnType, btnSubmit;
    private ArrayAdapter<String> adapter;
    private SlidingLayer mSlidingLayer;
    private TextView swipeText, tag1, tag2, tag3;
    private ViewGroup hiddenPanel;
    private ViewGroup mainScreen;
    private boolean isPanelShown;
    private ViewGroup root;
    private ArrayList<BottomSheetItem> arrayOfBottomSheetItem;
    private RecyclerView.Adapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private RecyclerView bottomSheetRecyclerview;
    private LinearLayout rowTags;
    private MaterialEditText spotName, spotNote;

    public static FragmentWish newInstance(String param1, String param2) {
        FragmentWish fragment = new FragmentWish();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_wish, container, false);
        rowTags = (LinearLayout) layout.findViewById(R.id.tag_row);
        tag1 = (TextView) layout.findViewById(R.id.tag1);
        tag2 = (TextView) layout.findViewById(R.id.tag2);
        tag3 = (TextView) layout.findViewById(R.id.tag3);
        btnSubmit = (Button) layout.findViewById(R.id.btn_submit);
        spotName = (MaterialEditText) layout.findViewById(R.id.spot_name);
        spotNote = (MaterialEditText) layout.findViewById(R.id.note);


        TextView btnOpenSheet = (TextView) layout.findViewById(R.id.buttonOpen);
        btnOpenSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlidingLayer.openLayer(true);
            }
        });
        TextView btnCloseSheet = (TextView) layout.findViewById(R.id.buttonClose);
        btnCloseSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlidingLayer.closeLayer(true);
            }
        });
        bindViews(layout);
//        initToolbar(layout);
        initState(layout);
        initButtonItem(layout);

        mainScreen = (ViewGroup) layout.findViewById(R.id.main_screen);
        ViewTreeObserver vto = mainScreen.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                screenHeight = mainScreen.getHeight();
                mainScreen.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        root = (ViewGroup) layout.findViewById(R.id.root);
        hiddenPanel = (ViewGroup) getLayoutInflater(savedInstanceState).inflate(R.layout.bottom_sheet, root, false);
        hiddenPanel.setVisibility(View.INVISIBLE);

        root.addView(hiddenPanel);

        isPanelShown = false;
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String submitTag1 = "";
                String submitName = "";
                String submitNote = "";
                submitTag1 = tag1.getText().toString();
                submitTag1 = submitTag1.substring(2,submitTag1.length());
                Log.v("wish", submitTag1);
                if (spotName != null) {
                    submitName = spotName.getText().toString();
                    submitName = submitName.replaceAll(" ","%20");
                }
                if (spotNote != null) {
                    submitNote = spotNote.getText().toString();
                    submitNote = submitNote.replaceAll(" ", "%20");
                } else
                    submitNote = "";
                AddToDB(submitName, submitNote, submitTag1);
                Integer colorBeforeTran = R.color.dragedColorFromEnd;
                Integer colorAfterTran = R.color.dragColorFromEnd;
                ValueAnimator btnSumbitAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorBeforeTran, colorAfterTran);
                btnSumbitAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        btnSubmit.setBackgroundColor(getResources().getColor(R.color.theme_orange));
                    }
                });
                btnSumbitAnimation.setDuration(500).start();
                startActivity(new Intent(getActivity(), RequestActivity.class));

            }
        });
        return layout;
    }

    private void initState(View layout) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        setupSlidingLayerPosition(prefs.getString("layer_location", "right"));
        setupSlidingLayerTransform(prefs.getString("layer_transform", "none"));

        setupShadow(prefs.getBoolean("layer_has_shadow", true));
        setupLayerOffset(prefs.getBoolean("layer_has_offset", false));
        setupPreviewMode(prefs.getBoolean("preview_mode_enabled", false));
    }


    private void bindViews(View layout) {
        mSlidingLayer = (SlidingLayer) layout.findViewById(R.id.slidingLayer1);
//        swipeText = (TextView) layout.findViewById(R.id.swipeText);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.tags_option_menu, menu);
    }


    private void setupSlidingLayerTransform(String layerTransform) {

        LayerTransformer transformer;

        switch (layerTransform) {
            case "alpha":
                transformer = new AlphaTransformer();
                break;
            case "rotation":
                transformer = new RotationTransformer();
                break;
            case "slide":
                transformer = new SlideJoyTransformer();
                break;
            default:
                return;
        }
        mSlidingLayer.setLayerTransformer(transformer);
    }

    private void setupShadow(boolean enabled) {
        if (enabled) {
            mSlidingLayer.setShadowSizeRes(R.dimen.shadow_size);
            mSlidingLayer.setShadowDrawable(R.drawable.sidebar_shadow);
        } else {
            mSlidingLayer.setShadowSize(0);
            mSlidingLayer.setShadowDrawable(null);
        }
    }

    private void setupLayerOffset(boolean enabled) {
        int offsetDistance = enabled ? getResources().getDimensionPixelOffset(R.dimen.offset_distance) : 0;
        mSlidingLayer.setOffsetDistance(offsetDistance);
    }

    private void setupPreviewMode(boolean enabled) {
        int previewOffset = enabled ? getResources().getDimensionPixelOffset(R.dimen.preview_offset_distance) : -1;
        mSlidingLayer.setPreviewOffsetDistance(previewOffset);
    }


    private void setupSlidingLayerPosition(String layerPosition) {

        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) mSlidingLayer.getLayoutParams();
        int textResource;
        Drawable d;


        textResource = R.string.swipe_down_label;
//        d = getResources().getDrawable(R.drawable.container_rocket);

        mSlidingLayer.setStickTo(SlidingLayer.STICK_TO_BOTTOM);
        rlp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        rlp.height = getResources().getDimensionPixelSize(R.dimen.layer_size);


//        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
//        swipeText.setCompoundDrawables(null, d, null, null);
//        swipeText.setText(getResources().getString(textResource));
        mSlidingLayer.setLayoutParams(rlp);
    }

    private void initButtonItem(View layout) {
        String[] iconName = {"Watch", "Tools", "SmartPhone", "TV", "Cable", "TDN"};
        Integer[] icon = {R.drawable.type_watch,
                R.drawable.type_tools,
                R.drawable.type_smartphone,
                R.drawable.type_tv,
                R.drawable.type_tvcable,
                R.drawable.type_tnd};
        arrayOfBottomSheetItem = new ArrayList<BottomSheetItem>();
        for (int i = 0; i < 6; i++) {
            BottomSheetItem bottomSheetItem = new BottomSheetItem();
            bottomSheetItem.setCatIcon(icon[i]);
            bottomSheetItem.setCatName(iconName[i]);
            arrayOfBottomSheetItem.add(i, bottomSheetItem);
        }

        bottomSheetRecyclerview = (RecyclerView) layout.findViewById(R.id.bottom_sheet_recyclerview);
        mAdapter = new BottomSheetAdapter(getActivity(), arrayOfBottomSheetItem, mSlidingLayer, rowTags, tag1, tag2, tag3);
        mLayoutManager = new GridLayoutManager(this.getActivity(), 3);
        mLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        bottomSheetRecyclerview.setLayoutManager(mLayoutManager);
        bottomSheetRecyclerview.setAdapter(mAdapter);

        bottomSheetRecyclerview.setHasFixedSize(true);


    }

    private void AddToDB(final String spotName, final String spotNote, final String spotTag1) {
        String insertURL = "http://androiddebugoska.host22.com/add_to_pending.php?spot_name=" + spotName + "&spot_note=" + spotNote + "&spot_tag1=" + spotTag1;
        Toast.makeText(getActivity(), "added", Toast.LENGTH_LONG);
        StringRequest postRequest = new StringRequest(Request.Method.POST, insertURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        "failed to insert", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("spot_item_name", spotName);
                params.put("spot_item_time", spotNote);
                params.put("spot_item_type", spotTag1);

                Log.v("pending", "in the hash map" + spotName);
                return params;
            }
        };

        // Adding request to request queues
        AppController.getInstance().addToRequestQueue(postRequest);

    }
}
