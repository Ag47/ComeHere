package io.codeguy.comehere.menu_item;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.codeguy.comehere.MainActivity;
import io.codeguy.comehere.R;
import io.codeguy.comehere.SpotListFragment;

public class SpotFragment extends Fragment {

    String url = String.format("https://api-us.clusterpoint.com/100403/android.json");
    String search_url = String.format("https://api-us.clusterpoint.com/100403/android/_search.json");
    public String search;
    private static final String TAG_doc = "documents";
    private static final String TAG_ID = "id";
    private static final String TAG_COL1 = "col1";
    private static final String TAG_COL2 = "col2";
    JSONObject jsonObject = null;
    public JSONObject responseObject = null;

    // contacts JSONArray
    JSONArray documents = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> documentsList;

    public SpotFragment() {
    }

    Button submitSearch;
    EditText searchString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_spot, container, false);
        searchString = (EditText) rootView.findViewById(R.id.inputSearch);
        submitSearch = (Button) rootView.findViewById(R.id.inputSearchBtn);
        submitSearch.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        search = searchString.getText().toString();
                        MainActivity.fragmentExtra = search;
                        Fragment fragment = new SpotListFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
                    }
                });
        return rootView;
    }


}


