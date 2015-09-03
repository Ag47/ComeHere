package io.codeguy.comehere.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.codeguy.comehere.Adapter.ResultSearchAdapter;
import io.codeguy.comehere.DataObject.Product;
import io.codeguy.comehere.MapProductActivity;
import io.codeguy.comehere.R;

/**
 * Created by KaiHin on 8/25/2015.
 */
public class FragmentFindResult extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static ArrayList<Product> searchProduct;
    private String mParam1;
    private String mParam2;
    private RecyclerView resultRecyclerVIew;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private FloatingActionButton mapFab;


    public static FragmentFindResult newInstance(String param1, String param2, ArrayList<Product> getSearchProduct) {
        FragmentFindResult fragment = new FragmentFindResult();
        Log.v("result", "the array list size is in newinstance" + getSearchProduct.size());
        searchProduct = getSearchProduct;
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
        View layout = inflater.inflate(R.layout.fragment_find_result, container, false);
        Log.v("result", "the array list size is in onCreateView" + searchProduct.size());
        initRecyclerView(layout);
        return layout;
    }

    private void initRecyclerView(View layout) {
        resultRecyclerVIew = (RecyclerView) layout.findViewById(R.id.result_recycler_view);
        mapFab = (FloatingActionButton) layout.findViewById(R.id.fab_map);
        mAdapter = new ResultSearchAdapter(this.getActivity(), searchProduct);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        resultRecyclerVIew.setLayoutManager(mLayoutManager);
        resultRecyclerVIew.setHasFixedSize(true);
        resultRecyclerVIew.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();

        mapFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), MapProductActivity.class);
                Bundle toMap = new Bundle();
                toMap.putSerializable("listResult", searchProduct);
                i.putExtras(toMap);
                startActivity(i);
            }
        });
    }
}
