package io.codeguy.comehere.Fragment;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import io.codeguy.comehere.R;

/**
 * Created by KaiHin on 28/12/2015.
 */
public class FragmentSharedHomePage2 extends Fragment {
    private EditText editSearch;
    public static FragmentSharedHomePage2 newInstance() {
        FragmentSharedHomePage2 fragment = new FragmentSharedHomePage2();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_main_v21up_shared_fragment_2, container, false);
        //handle focus edittext in the special case, which is editText VISIBILITY change from GONE to VISIBLE in transition, by oska
        (new Handler()).postDelayed(new Runnable() {

            public void run() {
//              ((EditText) findViewById(R.id.et_find)).requestFocus();
//
                EditText yourEditText= (EditText) view.findViewById(R.id.edit_search);
//              InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//              imm.showSoftInput(yourEditText, InputMethodManager.SHOW_IMPLICIT);

                yourEditText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                yourEditText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP , 0, 0, 0));

            }
        }, 200);


//        Fade fadeOut = new Fade();
//        fadeOut.setDuration(300);
//        animate FragmentSharedHomePage2 elements
//        getActivity().getWindow().setReturnTransition(fadeOut);
//        animate FragmentSharedHomePage1 elements
//        getActivity().getWindow().setReenterTransition(fadeOut);
        Log.v("transition", "3");

        return view;
    }

}

