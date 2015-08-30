//package io.codeguy.comehere.menu_item;
//
//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//
//import io.codeguy.comehere.MainActivity;
//import io.codeguy.comehere.R;
//import io.codeguy.comehere.SeekListFragment;
//import io.codeguy.comehere.SpotListFragment;
//
//
//public class SeekFragment extends Fragment {
//
//    public SeekFragment() {
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View rootView = inflater.inflate(R.layout.fragment_seek, container, false);
//        Button cable_button = (Button) rootView.findViewById(R.id.cable);
//        cable_button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                MainActivity.fragmentExtra = "TV Cables";
//                Fragment fragment = new SeekListFragment();
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.frame_container, fragment).commit();
//            }
//        });
//
//        Button tools_button = (Button) rootView.findViewById(R.id.tools);
//        tools_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("clicked tools", "");
//                MainActivity.fragmentExtra = "tools";
//                Fragment fragment = new SeekListFragment();
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.frame_container, fragment).commit();
//            }
//        });
//        return rootView;
//    }
//}