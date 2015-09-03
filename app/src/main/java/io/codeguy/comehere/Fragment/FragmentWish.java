package io.codeguy.comehere.Fragment;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.wunderlist.slidinglayer.LayerTransformer;
import com.wunderlist.slidinglayer.SlidingLayer;
import com.wunderlist.slidinglayer.transformer.AlphaTransformer;
import com.wunderlist.slidinglayer.transformer.RotationTransformer;
import com.wunderlist.slidinglayer.transformer.SlideJoyTransformer;

import java.util.ArrayList;

import io.codeguy.comehere.Adapter.BottomSheetAdapter;
import io.codeguy.comehere.DataObject.BottomSheetItem;
import io.codeguy.comehere.R;

/**
 * Created by KaiHin on 8/25/2015.
 */
public class FragmentWish extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    int screenHeight = 0;
    private String mParam1;
    private String mParam2;
    private Button btnType;
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

//        btnType = (Button) layout.findViewById(R.id.btn_type);
        Button btnOpenSheet = (Button) layout.findViewById(R.id.buttonOpen);
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

//        btnType.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//
//                slideUpDown(v);
////                Log.v("wish", "after btn clicked" + getShowsDialog());
////                setShowsDialog(true);
////                sheet = new BottomSheet.Builder(getActivity(), R.style.BottomSheet_StyleDialog).sheet(R.menu.tags_option_menu).listener(new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                        Log.v("wish", "before switch case");
////                        switch (which) {
////                            case 0:
////                                Snackbar.make(v, "clicked share", Snackbar.LENGTH_SHORT).show();
////                                break;
////                            case 1:
////                                Snackbar.make(v, "clicked upload", Snackbar.LENGTH_SHORT).show();
////                                break;
////                            case 2:
////                                Snackbar.make(v, "clicked call", Snackbar.LENGTH_SHORT).show();
////                                break;
////                            case 3:
////                                Snackbar.make(v, "clicked help", Snackbar.LENGTH_SHORT).show();
////                                break;
////
////                        }
////                    }
////                }).grid().build();
//            }
//        });
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

    //    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        getShowsDialog();
//        sheet = new BottomSheet.Builder(getActivity(), R.style.BottomSheet_StyleDialog).sheet(R.menu.tags_option_menu).listener(new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Log.v("wish", "before switch case");
//                switch (which) {
////                    case 0:
////                        Snackbar.make(v, "clicked share", Snackbar.LENGTH_SHORT).show();
////                        break;
////                    case 1:
////                        Snackbar.make(v, "clicked upload", Snackbar.LENGTH_SHORT).show();
////                        break;
////                    case 2:
////                        Snackbar.make(v, "clicked call", Snackbar.LENGTH_SHORT).show();
////                        break;
////                    case 3:
////                        Snackbar.make(v, "clicked help", Snackbar.LENGTH_SHORT).show();
////                        break;
//
//                    case 0:
//                        Log.v("wish","clicked");
//                }
//            }
//        }).grid().build();
//        return sheet;
//    }
//    public void slideUpDown(final View view) {
//        if (!isPanelShown) {
//            // Show the panel
//            mainScreen.layout(mainScreen.getLeft(),
//                    mainScreen.getTop() - (screenHeight * 25 / 100),
//                    mainScreen.getRight(),
//                    mainScreen.getBottom() - (screenHeight * 25 / 100));
//
//
//            hiddenPanel.layout(mainScreen.getLeft(), mainScreen.getBottom(), mainScreen.getRight(), screenHeight);
//            hiddenPanel.setVisibility(View.VISIBLE);
//
//            Animation bottomUp = AnimationUtils.loadAnimation(getActivity(),
//                    R.anim.bottom_sheet_up);
//
//            hiddenPanel.startAnimation(bottomUp);
//
//            isPanelShown = true;
//        } else {
//            isPanelShown = false;
//
//            // Hide the Panel
//            Animation bottomDown = AnimationUtils.loadAnimation(getActivity(),
//                    R.anim.bottom_sheet_down);
//            bottomDown.setAnimationListener(new Animation.AnimationListener() {
//
//                @Override
//                public void onAnimationStart(Animation arg0) {
//                    // TODO Auto-generated method stub
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation arg0) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animation arg0) {
//                    isPanelShown = false;
//
//                    mainScreen.layout(mainScreen.getLeft(),
//                            mainScreen.getTop() + (screenHeight * 25 / 100),
//                            mainScreen.getRight(),
//                            mainScreen.getBottom() + (screenHeight * 25 / 100));
//
//                    hiddenPanel.layout(mainScreen.getLeft(), mainScreen.getBottom(), mainScreen.getRight(), screenHeight);
//                }
//            });
//            hiddenPanel.startAnimation(bottomDown);
//        }
//    }


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
        String[] iconName = {"Watch", "Phone", "Tools", "Second Hands", "Computer", "Accessory"};
        Integer[] icon = {R.drawable.ic_watch_black_24dp,
                R.drawable.perm_group_phone_calls,
                R.drawable.ic_settings_grey_24dp,
                R.drawable.perm_group_phone_calls,
                R.drawable.ic_computer_grey_24dp,
                R.drawable.ic_assess_grey};
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

}