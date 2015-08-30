package io.codeguy.comehere.Adapter;

/**
 * Created by KaiHin on 8/13/2015.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import io.codeguy.comehere.AppController;
import io.codeguy.comehere.DataObject.PromotionInfo;
import io.codeguy.comehere.R;

/**
 * Created by KaiHin on 6/28/2015.
 */
//<VH> <- this should be contain the subclass in the java
public class PromotionInfoAdapter extends RecyclerView.Adapter<PromotionInfoAdapter.MyViewHolder> {

    View layout;
    ArrayList<PromotionInfo> data = new ArrayList<>();
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    boolean isFirst;
    private LayoutInflater inflater;
    private Context context;
    private ClickListener clickListener;

    public PromotionInfoAdapter(Context context, ArrayList<PromotionInfo> data) {
        this.context = context;
        this.data = data;
        Log.v("default controller", "adde din the MyViewHolder");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get the root of the custom row
//        View view = inflater.inflate(R.layout.custom_row, parent,false );
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.promotion_info_row, parent, false);
        // pass the view to the view holder
        MyViewHolder holder = new MyViewHolder(view);
        Log.v("onCreateViewHolder", "adde din the MyViewHolder");
        return holder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Log.v("promo", "the title that set is" + data.get(position).getTime().toString());
        PromotionInfo current = data.get(position);
        holder.title.setText(data.get(position).getTitle());
        holder.content.setText(data.get(position).getContent());
        holder.time.setText(data.get(position).getTime());
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(PromotionInfo promotionInfo, int index) {

        data.add(promotionInfo);
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
        TextView title, content, time;

        public MyViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.promo_time);
            title = (TextView) itemView.findViewById(R.id.promo_title);
            content = (TextView) itemView.findViewById(R.id.promo_content);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
