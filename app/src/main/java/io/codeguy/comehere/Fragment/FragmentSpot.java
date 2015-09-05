package io.codeguy.comehere.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import io.codeguy.comehere.R;
import io.codeguy.comehere.RecyclerViewActivity;
import io.codeguy.comehere.SpotActivity;


/**
 * Created by KaiHin on 7/13/2015.
 */
public class FragmentSpot extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public View mlayout;
    private String mParam1;
    private String mParam2;
    private Button goPending;

    public FragmentSpot() {

    }

    public static FragmentSpot newInstance(String param1, String param2) {
        FragmentSpot fragment = new FragmentSpot();
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
        View layout = inflater.inflate(R.layout.fragment_spot, container, false);
        mlayout = layout;
        goPending = (Button) layout.findViewById(R.id.go_recycler);
        goPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mlayout.getContext(), RecyclerViewActivity.class));
            }
        });

        Button btnSearch = (Button) layout.findViewById(R.id.btn_search);
        final EditText editSearch = (EditText) layout.findViewById(R.id.editSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchContent = editSearch.getText().toString();
                Snackbar.make(v, "You Entered: " + searchContent, Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), SpotActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("searchContent", searchContent);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return mlayout;
    }
}
