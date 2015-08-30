package io.codeguy.comehere.GoogleMap;

/**
 * Created by KaiHin on 8/11/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import com.google.android.gms.maps.MapFragment;
import com.google.maps.android.clustering.ClusterManager;

import io.codeguy.comehere.DataObject.PromotionMarkerInfo;
import io.codeguy.comehere.R;

public class MainMapFragment extends MapFragment {

    //    private View customMarkerView;
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.custom_marker_layout , container, false);
//        customMarkerView = rootView;
////        return super.onCreateView(inflater, container, savedInstanceState);
//        return rootView;
//    }
    int i = 27;

    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    public void placeMarker(PromotionMarkerInfo promotionMarkerInfo, ClusterManager<PromotionMarkerInfo> clusterManager) {
        View marker = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);

//        TextView numTxt = (TextView) marker.findViewById(R.id.num_txt);
//        numTxt.setText(i+"");
//        i++;
//        NetworkImageView shopperIcon = (NetworkImageView) marker.findViewById(R.id.shopper_icon);
//        ImageLoader imageLoader =  AppController.getInstance().getImageLoader();
//        shopperIcon.setImageUrl("https://lh5.ggpht.com/I7Q6vZsaxNvpozGVGfUvSfIOhfqCTO1-kFlCYP1au-0cuWERSDuiTS5hzw-nCF1uOJg8=w300",imageLoader);
        ImageView shopperIcon = (ImageView) marker.findViewById(R.id.shopper_icon);
        shopperIcon.setImageResource(promotionMarkerInfo.getShopperIcon());

//        Marker m  = getMap().addMarker(new MarkerOptions()
//
//                .position(promotionMarkerInfo.getPointLocation())
//
//                .title(promotionMarkerInfo.getName())
//        .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActivity(), marker))));

        Log.v("cluster", promotionMarkerInfo.getPointLocation().toString());
        clusterManager.addItem(promotionMarkerInfo);


//        return m;

    }

}