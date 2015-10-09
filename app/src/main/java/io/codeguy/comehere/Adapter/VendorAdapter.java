package io.codeguy.comehere.Adapter;

/**
 * Created by KaiHin on 7/8/2015.
 * MyRecyclerAdapter
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import io.codeguy.comehere.AppController;
import io.codeguy.comehere.DataObject.pending;
import io.codeguy.comehere.DetailItem;
import io.codeguy.comehere.R;

/**
 * Created by KaiHin on 6/28/2015.
 */
//<VH> <- this should be contain the subclass in the java
public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.MyViewHolder> {

    ArrayList<pending> data = new ArrayList<>();
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private LayoutInflater inflater;
    private Context context;
    private ClickListener clickListener;
    private OnStartDragListener mDragStartListener;

    public VendorAdapter(Context context, ArrayList<pending> data) {
        this.context = context;
        this.data = data;
        Log.v("pending", "the adapter in the data size is " + data.size());
        Log.v("default controller", "added in the MyViewHolder");

    }

    public VendorAdapter(OnStartDragListener dragListener) {
        mDragStartListener = dragListener;
    }

    // the viewgroup into which the new view will be added after it is bound to an adapter position
    // the view type of the new view
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get the root of the custom row
//        View view = inflater.inflate(R.layout.custom_row, parent,false );
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_row, parent, false);
        // pass the view to the view holder
        MyViewHolder holder = new MyViewHolder(view);
        Log.v("onCreateViewHolder", "adde din the MyViewHolder");
        return holder;
    }
    // recycleView.ViewHolder -> MyViewHolder
    // the new viewholder should be constructed with a new View that can represent the the items of the items of tthe fiven type
    // the new viewHolder will be used to display items of the adapter using onBindViewHolder

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        pending current = data.get(position);
        Log.v("oscar", "onViewHolder data array name :" + data.get(position).getName());
        Log.v("oscar", "onViewHolder data array id " + current.getId());
        Log.v("oscar", "onViewHolder data array time " + current.getTimeExpired());
        Log.v("oscar", "onViewHolder data array key " + current.getInstalledKey());
        holder.name.setText(data.get(position).getName());
//        holder.profileIcon.setImageUrl(data.get(position).getIcon_thum(), imageLoader);
        Log.v("oscar", "onBindViewHolder adding..." + current.getName());
//     java.lang.NullPointerException onViewHolder setText

//        holder.icon.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(),"Item click at "+ position,Toast.LENGTH_LONG).show();;
//            }
//        });
        Log.v("onBindViewHolder", "adde din the MyViewHolder");

    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(pending pendingObj, int index) {

        data.add(pendingObj);
        notifyItemInserted(index);
    }

    @Override
    public int getItemCount() {
        Log.v("oscar", "the data array size is :" + data.size() + "");
        return data.size();
    }

    public interface OnStartDragListener {
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }


//    @Override
//    public void onItemDismiss(int position) {
//
//        pending current = data.get(position);
//
//        String delete_url = "http://androiddebugoska.host22.com/delete_pending_item.php?pending_id=" + current.getId();
//        JsonObjectRequest delete_request = new JsonObjectRequest(delete_url,
//                null, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.v("pending","onItemDississ run");
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(delete_request);
//
//        data.remove(position);
//
//        notifyItemRemoved(position);
//    }


    public interface ClickListener {
        public void itemClicked(View view, int position);
    }

    // sub class
    // this way is initializing the data in the class members, so that it should be called in the
    // onCreateViewHolder to initialzing the data members
    // this is set the specfic item view action
    // By           Oska
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView id, name, appKey, time;
        Button btnAccept;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_name);
            btnAccept = (Button) itemView.findViewById(R.id.btn_accept);
            itemView.setOnClickListener(this);
            btnAccept.setOnClickListener(this);
            Log.v("oscar", "added in the MyViewHolder");
        }

        // On Long Click, changge color         By, Oska

        @Override
        public void onClick(View v) {
            pending current = data.get(getPosition());

            notifyItemRemoved(getPosition());
        }
    }

//    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
//            urlJsonObj,(String)null, new Response.Listener<JSONObject>() {
//
//        @Override
//        public void onResponse(JSONObject response) {
//            Log.d(TAG, response.toString());
//
//            try {
//                comeHereDB = response.getJSONArray("come_here");
}
