package io.codeguy.comehere.Adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import io.codeguy.comehere.AppController;
import io.codeguy.comehere.DataObject.Product;
import io.codeguy.comehere.R;

/**
 * Created by KaiHin on 8/25/2015.
 */
public class ResultSearchAdapter extends RecyclerView.Adapter<ResultSearchAdapter.MyViewHolder> {

    ArrayList<Product> data = new ArrayList<>();
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private LayoutInflater inflater;
    private Context context;
    private ClickListener clickListener;

    public ResultSearchAdapter(Context context, ArrayList<Product> data) {
        this.context = context;
        this.data = data;
        Log.v("result", "in the constructer");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get the root of the custom row
//        View view = inflater.inflate(R.layout.custom_row, parent,false );
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_result_search, parent, false);

        // pass the view to the view holder
        MyViewHolder holder = new MyViewHolder(view);
        Log.v("result", "in the MyViewHolder");
        return holder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Product current = data.get(position);
        Log.v("result", "name " + data.get(position).getpName());
        holder.name.setText(data.get(position).getpName());
        holder.productImage.setImageUrl(data.get(position).getImaageURL(), imageLoader);
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
        Log.v("result ", "the getItemCount size is " + data.size());
        return data.size();
    }

    public interface ClickListener {
        public void itemClicked(View view, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        NetworkImageView productImage;
        Button btnShare, btnView;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.product_result_name);
            productImage = (NetworkImageView) itemView.findViewById(R.id.result_product_image);
            btnShare = (Button) itemView.findViewById(R.id.btn_share);
            btnView = (Button) itemView.findViewById(R.id.btn_view);

            btnShare.setOnClickListener(this);
            btnView.setOnClickListener(this);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (v == btnShare) {
                Snackbar.make(v, "clicked share", Snackbar.LENGTH_SHORT).show();
            }
            if (v == btnView) {
                Snackbar.make(v, "clicked view", Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
