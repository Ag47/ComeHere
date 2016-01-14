package io.codeguy.comehere.Adapter;

/**
 * Created by KaiHin on 7/8/2015.
 * MyRecyclerAdapter
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import io.codeguy.comehere.Network.AppController;
import io.codeguy.comehere.DataObject.pending;
import io.codeguy.comehere.R;

/**
 * Created by KaiHin on 6/28/2015.
 */
public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.MyViewHolder> {

    ArrayList<pending> data = new ArrayList<>();

    public VendorAdapter(ArrayList<pending> data) {
        this.data = data;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.name.setText(data.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView id, name;
        Button btnAccept;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_name);
            btnAccept = (Button) itemView.findViewById(R.id.btn_accept);
            itemView.setOnClickListener(this);
            btnAccept.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            data.remove(getPosition());
             notifyDataSetChanged();

        }
    }
}