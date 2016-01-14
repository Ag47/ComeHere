package io.codeguy.comehere.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import io.codeguy.comehere.Adapter.GridProductTypeAdapter;
import io.codeguy.comehere.DataObject.BottomSheetItem;
import io.codeguy.comehere.R;

/**
 * Created by KaiHin on 28/12/2015.
 */
public class FragmentSharedHomePage1  extends Fragment {
    private RecyclerView.Adapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private RecyclerView gridProductTypeRecycler;
    private ArrayList<BottomSheetItem> arrayOfBottomSheetItem;
    private LinearLayout searchBar;
    public static FragmentSharedHomePage1 newInstance() {

        Bundle args = new Bundle();

        FragmentSharedHomePage1 fragment = new FragmentSharedHomePage1();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_v21up_shared_fragment_1, container, false);

        initButtonItem(view);
        animateSearchBar(view);
//        final RippleBackground rippleBackground=(RippleBackground)view.findViewById(R.id.content);

//        rippleBackground.startRippleAnimation();

//        final ImageView animatedSearch = (ImageView) view.findViewById(R.id.animated_search);
//        animatedSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addNextFragment(animatedSearch);
//                Toast.makeText(getActivity(), "click icon", Toast.LENGTH_LONG).show();
//            }
//        });

        return view;
    }

    private void animateSearchBar(View view) {
        searchBar = (LinearLayout) view.findViewById(R.id.animated_search_bar);
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNextFragment(searchBar);
            }
        });
    }

    private void initButtonItem(View view) {
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

        gridProductTypeRecycler = (RecyclerView) view.findViewById(R.id.grid_type_recycler);
        mAdapter = new GridProductTypeAdapter(getActivity(), arrayOfBottomSheetItem, false);
        mLayoutManager = new GridLayoutManager(this.getActivity(), 3);
        mLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        gridProductTypeRecycler.setLayoutManager(mLayoutManager);
        gridProductTypeRecycler.setAdapter(mAdapter);

        gridProductTypeRecycler.setHasFixedSize(true);

    }
    private void addNextFragment(LinearLayout searchBar) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            FragmentSharedHomePage2 sharedElementFragment2 = FragmentSharedHomePage2.newInstance();
            Log.v("transition", "2");

            Fade fadeIn = new Fade();
            fadeIn.setDuration(300);

            ChangeBounds changeBoundsTransition = new ChangeBounds();
            changeBoundsTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));

            sharedElementFragment2.setReturnTransition(fadeIn);
            sharedElementFragment2.setReenterTransition(fadeIn);
            sharedElementFragment2.setAllowEnterTransitionOverlap(true);
            sharedElementFragment2.setAllowReturnTransitionOverlap(true);
            sharedElementFragment2.setSharedElementEnterTransition(changeBoundsTransition);


            getFragmentManager().beginTransaction()
                    .replace(R.id.shared_content, sharedElementFragment2)
                    .addToBackStack(null)
                    .addSharedElement(searchBar, getString(R.string.transition_name))
                    .commit();
        }
    }
}

