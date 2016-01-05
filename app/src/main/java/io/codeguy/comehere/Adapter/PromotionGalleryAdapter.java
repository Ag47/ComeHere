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
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import io.codeguy.comehere.Network.AppController;
import io.codeguy.comehere.DataObject.PromoImage;
import io.codeguy.comehere.DataObject.PromotionInfo;
import io.codeguy.comehere.R;

/**
 * Created by KaiHin on 6/28/2015.
 */
//<VH> <- this should be contain the subclass in the java
public class PromotionGalleryAdapter extends RecyclerView.Adapter<PromotionGalleryAdapter.MyViewHolder> {

    View layout;
    ArrayList<PromoImage> data = new ArrayList<>();
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    boolean isFirstImage = true;
    private LayoutInflater inflater;
    private Context context;
    private ClickListener clickListener;

    public PromotionGalleryAdapter(Context context, ArrayList<PromoImage> data) {
        this.context = context;
        this.data = data;
        Log.v("default controller", "adde din the MyViewHolder");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get the root of the custom row
//        View view = inflater.inflate(R.layout.custom_row, parent,false );
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.promo_first_image_row, parent, false);
        // pass the view to the view holder
        MyViewHolder holder = new MyViewHolder(view);
        Log.v("onCreateViewHolder", "adde din the MyViewHolder");
        return holder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        PromoImage current = data.get(position);
//        if(isFirstImage && data.get(position).getPromoImage() !="null" ) {
        Log.v("promoAdapter", "fource in " + position);
        isFirstImage = false;

        Log.v("promoAdapter", "fource boolean" + isFirstImage);
        isFirstImage = false;
        Log.v("promoAdapter", "the image url is " + data.get(position).getPromoImage());
        Log.v("promoAdapter", "the image title is " + data.get(position).getPromoImageTitle());

        holder.promoImage.setImageUrl(data.get(position).getPromoImage(), imageLoader);

//        }
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(PromotionInfo promotionInfo, int index) {

//        data.add();
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
        TextView title;
        NetworkImageView promoImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            promoImage = (NetworkImageView) itemView.findViewById(R.id.promo_image);
//            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
