package io.codeguy.comehere.Fragment;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import io.codeguy.comehere.R;

/**
 * Created by KaiHin on 28/12/2015.
 */
public class FragmentSharedHomePage2 extends Fragment {

    public static FragmentSharedHomePage2 newInstance() {
        FragmentSharedHomePage2 fragment = new FragmentSharedHomePage2();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_v21up_shared_fragment_2, container, false);
        view.findViewById(R.id.home_edit_text).getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        Fade fadeOut = new Fade();
        fadeOut.setDuration(300);
        //animate FragmentSharedHomePage2 elements
        getActivity().getWindow().setReturnTransition(fadeOut);
        //animate FragmentSharedHomePage1 elements
        getActivity().getWindow().setReenterTransition(fadeOut);
        ImageView search = (ImageView) view.findViewById(R.id.animated_search);
        Log.v("transition", "3");

        return view;
    }

}

