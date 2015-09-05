package io.codeguy.comehere.Adapter;

/**
 * Created by KaiHin on 7/8/2015.
 * SpotProductRecyclerAdapter
 */

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import io.codeguy.comehere.DataObject.Product;
import io.codeguy.comehere.GoogleTouchHelper.ItemTouchHelperAdapter;
import io.codeguy.comehere.GoogleTouchHelper.ItemTouchHelperViewHolder;
import io.codeguy.comehere.R;

/**
 * Created by KaiHin on 6/28/2015.
 */
//<VH> <- this should be contain the subclass in the java
public class SpotProductRecyclerAdapter extends RecyclerView.Adapter<SpotProductRecyclerAdapter.MyViewHolder> implements ItemTouchHelperAdapter {

    ArrayList<Product> data = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    private ClickListener clickListener;
//    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    //    public SpotProductRecyclerAdapter(Context context, ArrayList<Product> data, TouchInterceptionFrameLayout mTouchInterceptionFrameLayout) {
//        this.mTouchInterceptionFrameLayout = mTouchInterceptionFrameLayout;
//        this.context = context;
//        this.data = data;
//        Log.v("default controller", "adde din the MyViewHolder");
//
//    }
    public SpotProductRecyclerAdapter(Context context, ArrayList<Product> data) {
        this.context = context;
        this.data = data;
        Log.v("spot", "SpotProductRecyclerAdapter Construector");

    }

//    public interface OnStartDragListener {
//        void onStartDrag(RecyclerView.ViewHolder viewHolder);
//    }
//
//    private OnStartDragListener mDragStartListener;
//
//    public SpotProductRecyclerAdapter(TouchInterceptionFrameLayout mTouchInterceptionFrameLayout) {
//        this.mTouchInterceptionFrameLayout = mTouchInterceptionFrameLayout;
//    }

//    public SpotProductRecyclerAdapter(OnStartDragListener dragListener) {
//        mDragStartListener = dragListener;
//    }
    // recycleView.ViewHolder -> MyViewHolder
    // the new viewholder should be constructed with a new View that can represent the the items of the items of tthe fiven type
    // the new viewHolder will be used to display items of the adapter using onBindViewHolder

    // the viewgroup into which the new view will be added after it is bound to an adapter position
    // the view type of the new view
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get the root of the custom row
//        View view = inflater.inflate(R.layout.custom_row, parent,false );
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_spot_product_row, parent, false);
        // pass the view to the view holder
        MyViewHolder holder = new MyViewHolder(view);
        Log.v("spot", "onCreateaViewHolder");
        return holder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Product current = data.get(position);

        holder.pName.setText(data.get(position).getpName());
        holder.pid.setText(data.get(position).getPid());
        holder.pPrice.setText(data.get(position).getpPrice());
        holder.pDiscount.setText(data.get(position).getpDiscount());
        holder.pImage.setText(data.get(position).getImaageURL());
        holder.pVendorId.setText(data.get(position).getpVendorId());
        holder.pVendor.setText(data.get(position).getpVendorName());
        holder.pTypeName.setText(data.get(position).getpTypeName());
        holder.pTypeId.setText(data.get(position).getpTypeId());
//     java.lang.NullPointerException onViewHolder setText

//        holder.icon.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(),"Item click at "+ position,Toast.LENGTH_LONG).show();;
//            }
//        });
        Log.v("spot", "onBindViewHolder");

    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(Product responseObj, int index) {

        data.add(responseObj);
        notifyItemInserted(index);
    }

    @Override
    public int getItemCount() {
        Log.v("spot", "the item in the recycler Adapter is " + data.size());
        return data.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(data, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }


    public interface ClickListener {
        public void itemClicked(View view, int position);
    }

    // sub class
    // this way is initializing the data in the class members, so that it should be called in the
    // onCreateViewHolder to initialzing the data members
    // this is set the specfic item view action
    // By           Oska
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ItemTouchHelperViewHolder {
        TextView pid, pName, pVendor, pPrice, pVendorId, pTypeName, pTypeId, pDiscount, pImage;

        //        NetworkImageView profileIcon;
        public MyViewHolder(View itemView) {
            super(itemView);
            pid = (TextView) itemView.findViewById(R.id.spot_product_id);
            pName = (TextView) itemView.findViewById(R.id.spot_product_name);
            pVendor = (TextView) itemView.findViewById(R.id.spot_vendor_name);
            pPrice = (TextView) itemView.findViewById(R.id.spot_product_price);
            pVendorId = (TextView) itemView.findViewById(R.id.v_id);
            pTypeName = (TextView) itemView.findViewById(R.id.type_name);
            pTypeId = (TextView) itemView.findViewById(R.id.type_id);
            pDiscount = (TextView) itemView.findViewById(R.id.p_discount);
            pImage = (TextView) itemView.findViewById(R.id.p_image);


            itemView.setOnClickListener(this);
            Log.v("spot", "MyViewHolder");

        }

        // On Long Click, changge color         By, Oska
        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(context.getResources().getColor(R.color.selected_item));
            Snackbar.make(itemView, "You selected item", Snackbar.LENGTH_LONG).setAction("Action", this).show();

        }

        //         DOWN Motion, set Original color      By, Oska
        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }

        @Override
        public void onClick(View v) {
            // on Click profile icon                        by oska

            Product current = data.get(getPosition());
            int currentClicked = getPosition();
            Bundle itemBundle = new Bundle();
//                Intent intentToDetail = new Intent(context, DetailItem.class);
//                itemBundle.putString("clickedItemName", current.getName());
//                itemBundle.putString("clickedItemID", current.getId());
//                itemBundle.putString("clickedItemKey", current.getInstalledKey());
//                itemBundle.putString("clickedItemTime", current.getTimeExpired());
//                Log.v("DetailItem", "onclick Name: " + current.getName());
//
//                Snackbar.make(v,"You Click" + current.getId() , Snackbar.LENGTH_LONG).show();
//                intentToDetail.putExtras(itemBundle);
//                context.startActivity(intentToDetail);

            if (clickListener != null) {
                clickListener.itemClicked(v, getPosition());
            }

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
