package io.codeguy.comehere.Adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wunderlist.slidinglayer.SlidingLayer;

import java.util.ArrayList;

import io.codeguy.comehere.DataObject.BottomSheetItem;
import io.codeguy.comehere.R;

/**
 * Created by KaiHin on 8/27/2015.
 */
public class GridProductTypeAdapter extends RecyclerView.Adapter<GridProductTypeAdapter.MyViewHolder> {

    ArrayList<BottomSheetItem> data = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    private ClickListener clickListener;
    private SlidingLayer gotSlidingLayout;
    private LinearLayout gotRowTags;
    private boolean isButtomSheet;
    private TextView gotTag1, gotTag2, gotTag3, responseNum;

    public GridProductTypeAdapter(Context context, ArrayList<BottomSheetItem> data, SlidingLayer slidingLayer, LinearLayout rowTags, TextView tag1, TextView tag2, TextView tag3, boolean isButtomSheet) {
        this.context = context;
        this.data = data;
        this.isButtomSheet = isButtomSheet;
        gotSlidingLayout = slidingLayer;
        gotRowTags = rowTags;
        gotTag1 = tag1;
        gotTag2 = tag2;
        gotTag3 = tag3;
    }
    public GridProductTypeAdapter(Context context, ArrayList<BottomSheetItem> data, boolean isButtomSheet) {
        this.context = context;
        this.data = data;
        this.isButtomSheet = isButtomSheet;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get the root of the custom row
//        View view = inflater.inflate(R.layout.custom_row, parent,false );
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_bottom_sheet, parent, false);

        // pass the view to the view holder
        MyViewHolder holder = new MyViewHolder(view);
        Log.v("result", "in the MyViewHolder");
        return holder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        BottomSheetItem current = data.get(position);
         Log.v("bottomSheet", "the bs name" + current.getCatName());

        holder.catText.setText(data.get(position).getCatName());
        holder.catIcon.setImageResource(data.get(position).getCatIcon());
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(BottomSheetItem item, int index) {

        data.add(item);
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
        ImageView catIcon;
        TextView catText;
        LinearLayout clickable;

        public MyViewHolder(View itemView) {
            super(itemView);
            clickable = (LinearLayout) itemView.findViewById(R.id.clickable);
            catIcon = (ImageView) itemView.findViewById(R.id.cat_icon);
            catText = (TextView) itemView.findViewById(R.id.cat_name);
            if(isButtomSheet) {
                // action for tag
                catIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BottomSheetItem current = data.get(getPosition());
                        Log.v("wish", "clicked image " + current.getCatName());
                        Snackbar.make(v, "Added type tag " + current.getCatName(), Snackbar.LENGTH_SHORT).show();
                        gotTag1.setVisibility(View.VISIBLE);
                        gotTag1.setText("# " + current.getCatName());
                        gotSlidingLayout.closeLayer(true);


                    }
                });
            }
            if(!isButtomSheet) {

            }
        }

        @Override
        public void onClick(View v) {

        }
    }
}

