package io.codeguy.comehere.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import io.codeguy.comehere.Network.AppController;
import io.codeguy.comehere.DataObject.Product;
import io.codeguy.comehere.R;
import io.codeguy.comehere.SearchProductDetail;

/**
 * Created by KaiHin on 8/23/2015.
 */
public class SpotPendingAdapter extends RecyclerView.Adapter<SpotPendingAdapter.MyViewHolder> {

    View layout;
    ArrayList<Product> data = new ArrayList<>();
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    boolean isFirst;
    private LayoutInflater inflater;
    private Context context;
    private ClickListener clickListener;

    public SpotPendingAdapter(Context context, ArrayList<Product> data) {
        this.context = context;
        this.data = data;
        Log.v("default controller", "adde din the MyViewHolder");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get the root of the custom row
//        View view = inflater.inflate(R.layout.custom_row, parent,false );
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_instant_search, parent, false);
        // pass the view to the view holder
        MyViewHolder holder = new MyViewHolder(view);
        Log.v("onCreateViewHolder", "adde din the MyViewHolder");
        return holder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Product current = data.get(position);
        Log.v("instant", "name " + data.get(position).getpName());
        holder.name.setText(data.get(position).getpName());
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(Product product, int index) {

        data.add(product);
        notifyItemInserted(index);
    }

    @Override
    public int getItemCount() {
        Log.v("promo", "the getItemCount size is " + data.size());
        return data.size();
    }

    public interface ClickListener {
        public void itemClicked(View view, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.product_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Product currentProduct = data.get(getPosition());
            Bundle itemBundle = new Bundle();
            Intent intentToDetail = new Intent(context, SearchProductDetail.class);
            itemBundle.putString("searchProductName",currentProduct.getpName());
            itemBundle.putString("searchProductLocation", currentProduct.getpVendorAddr());
            itemBundle.putString("searchProductImage", currentProduct.getImaageURL());
            itemBundle.putString("searchPRoductPrice", currentProduct.getpPrice());
            itemBundle.putString("searchProductShopperName", currentProduct.getpVendorName());
            intentToDetail.putExtras(itemBundle);
            context.startActivity(intentToDetail);
        }
    }
}
