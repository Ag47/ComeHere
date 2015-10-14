package io.codeguy.comehere.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import io.codeguy.comehere.R;

/**
 * Created by KaiHin on 10/13/2015.
 */
public class FocusTodayInfoAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] productInfo;
    private final Integer[] imageId;

    public FocusTodayInfoAdapter(Activity context,
                              String[] productInfo, Integer[] imageId) {
        super(context, R.layout.list_focus_today_info, productInfo);
        this.context = context;
        this.productInfo = productInfo;
        this.imageId = imageId;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_focus_today_info, parent, false);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.focus_title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.focus_img);
        txtTitle.setText(productInfo[position]);

        imageView.setImageResource(imageId[position]);


        return rowView;
    }

}
