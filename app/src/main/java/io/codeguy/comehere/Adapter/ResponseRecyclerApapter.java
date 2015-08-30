package io.codeguy.comehere.Adapter;

/**
 * Created by KaiHin on 7/8/2015.
 * ResponseRecyclerApapter
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import io.codeguy.comehere.AppController;
import io.codeguy.comehere.DataObject.response;
import io.codeguy.comehere.DetailItem;
import io.codeguy.comehere.R;

/**
 * Created by KaiHin on 6/28/2015.
 */
//<VH> <- this should be contain the subclass in the java
public class ResponseRecyclerApapter extends RecyclerView.Adapter<ResponseRecyclerApapter.MyViewHolder> {

    View layout;
    //    public FrameLayout responseFrameLayout;
    ArrayList<response> data = new ArrayList<>();
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private LayoutInflater inflater;
    private Context context;
    private ClickListener clickListener;

    public ResponseRecyclerApapter(Context context, ArrayList<response> data) {
        this.context = context;
        this.data = data;
        Log.v("default controller", "adde din the MyViewHolder");

    }
//
//    public interface OnStartDragListener
//    {
//        void onStartDrag(RecyclerView.ViewHolder viewHolder);
//    }
//    private OnStartDragListener mDragStartListener;
//
//
//
//    public ResponseRecyclerApapter(OnStartDragListener dragListener){
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_response_row, parent, false);
        // pass the view to the view holder
        MyViewHolder holder = new MyViewHolder(view);
        Log.v("onCreateViewHolder", "adde din the MyViewHolder");
        return holder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        response current = data.get(position);
        Log.v("oscar", "onViewHolder data array name :" + data.get(position).getName());
        Log.v("oscar", "onViewHolder data array id " + current.getId());
        Log.v("oscar", "onViewHolder data array time " + current.getTimeExpired());
        Log.v("oscar", "onViewHolder data array key " + current.getInstalledKey());
        holder.name.setText(data.get(position).getName());
        holder.id.setText(data.get(position).getId());
        holder.time.setText(data.get(position).getTimeExpired());
        holder.appKey.setText(data.get(position).getInstalledKey());
        holder.profileIcon.setImageUrl(data.get(position).getIcon_thum(), imageLoader);
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

    public void addItem(response responseObj, int index) {

        data.add(responseObj);
        notifyItemInserted(index);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

//    @Override
//    public void onItemMove(int fromPosition, int toPosition) {
//        Collections.swap(data,fromPosition,toPosition);
//        notifyItemMoved(fromPosition,toPosition);
//    }

//    @Override
//    public void onItemDismiss(int position) {
//        data.remove(position);
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
        NetworkImageView profileIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.item_id);
            name = (TextView) itemView.findViewById(R.id.item_name);
            appKey = (TextView) itemView.findViewById(R.id.item_app_registered_key);
            time = (TextView) itemView.findViewById(R.id.item_time_expired);
            profileIcon = (NetworkImageView) itemView.findViewById(R.id.item_profilePic);
            profileIcon.setOnClickListener(this);
            itemView.setOnClickListener(this);
            Log.v("oscar", "added in the MyViewHolder");
        }

        // On Long Click, changge color         By, Oska
//        @Override
//        public void onItemSelected() {
//            itemView.setBackgroundColor(context.getResources().getColor(R.color.selected_item));
//            Snackbar.make(itemView,"You selected item",Snackbar.LENGTH_LONG).setAction("Action", this).show();
//
//        }
        // DOWN Motion, set Original color      By, Oska
//        @Override
//        public void onItemClear() {
//            itemView.setBackgroundColor(0);
//        }

        @Override
        public void onClick(View v) {
            // on Click profile icon                        by oska
            if (v == profileIcon) {
                Toast.makeText(v.getContext(), "Icon is clicked ", Toast.LENGTH_SHORT).show();
                LayoutInflater layoutInflater
                        = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                // set the outside background to grey,      by oscar
//                responseFrameLayout.getForeground().setAlpha(140);
                View popupView = layoutInflater.inflate(R.layout.popup, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);

                Button btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
                btnDismiss.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

//                        responseFrameLayout.getForeground().setAlpha(0);
                        popupWindow.dismiss();

                    }
                });

                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                popupWindow.setFocusable(true);
                popupWindow.update();

            } else {

                response current = data.get(getPosition());
                int currentClicked = getPosition();
                Bundle itemBundle = new Bundle();
                Intent intentToDetail = new Intent(context, DetailItem.class);
                itemBundle.putString("clickedItemName", current.getName());
                itemBundle.putString("clickedItemID", current.getId());
                itemBundle.putString("clickedItemKey", current.getInstalledKey());
                itemBundle.putString("clickedItemTime", current.getTimeExpired());
                itemBundle.putString("clickedProfileIcon", current.getIcon_thum());
                itemBundle.putString("clickedProductDiscount", current.getResponseProductDiscount());
                itemBundle.putString("clickedProductImage", current.getResponseProductImage());
                itemBundle.putString("clickedShopperName", current.getShopperName());
                itemBundle.putString("clickedProductPrice", current.getResponseProductPrice());
                itemBundle.putString("clickedShopperLocation", current.getShopperLocation());
                Log.v("Todaty", "done");
                Log.v("DetailItem", "onclick Name: " + current.getName());


                String translationName = "Translation";
//
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
//                        v,   // The view which starts the transition
//                        translationName    // The transitionName of the view we�re transitioning to
//                );
                Snackbar.make(v, "You Click" + current.getId(), Snackbar.LENGTH_LONG).show();
                intentToDetail.putExtras(itemBundle);
                context.startActivity(intentToDetail);
//                ActivityCompat.startActivity((Activity) context, intentToDetail, options.toBundle());
                if (clickListener != null) {
                    clickListener.itemClicked(v, getPosition());
                }
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
