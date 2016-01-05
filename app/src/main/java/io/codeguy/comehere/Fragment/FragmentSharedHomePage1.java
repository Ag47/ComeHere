package io.codeguy.comehere.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.skyfishjy.library.RippleBackground;

import io.codeguy.comehere.R;

/**
 * Created by KaiHin on 28/12/2015.
 */
public class FragmentSharedHomePage1  extends Fragment {

    public static FragmentSharedHomePage1 newInstance() {

        Bundle args = new Bundle();

        FragmentSharedHomePage1 fragment = new FragmentSharedHomePage1();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_v21up_shared_fragment_1, container, false);

        final RippleBackground rippleBackground=(RippleBackground)view.findViewById(R.id.content);

        rippleBackground.startRippleAnimation();

        final ImageView animatedSearch = (ImageView) view.findViewById(R.id.animated_search);
        animatedSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNextFragment(animatedSearch);
                Toast.makeText(getActivity(), "click icon", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    private void addNextFragment(ImageView animatedSearch) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            FragmentSharedHomePage2 sharedElementFragment2 = FragmentSharedHomePage2.newInstance();
            Log.v("transition", "2");

            Fade fadeIn = new Fade();
            fadeIn.setDuration(300);

            ChangeBounds changeBoundsTransition = new ChangeBounds();
            changeBoundsTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium_long));

            sharedElementFragment2.setEnterTransition(fadeIn);
            sharedElementFragment2.setExitTransition(fadeIn);
//            sharedElementFragment2.setExitTransition(fadeIn);
            sharedElementFragment2.setAllowEnterTransitionOverlap(false);
            sharedElementFragment2.setAllowReturnTransitionOverlap(false);
            sharedElementFragment2.setSharedElementEnterTransition(changeBoundsTransition);

            getFragmentManager().beginTransaction()
                    .replace(R.id.shared_content, sharedElementFragment2)
                    .addToBackStack(null)
                    .addSharedElement(animatedSearch, getString(R.string.transition_name))
                    .commit();
        }
    }
}

