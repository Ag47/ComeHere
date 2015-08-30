package io.codeguy.comehere.Adapter;

/**
 * Created by KaiHin on 8/5/2015.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import io.codeguy.comehere.R;

public class ProductInfoAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] productInfo;
    private final Integer[] imageId;
    private final String[] tags;

    public ProductInfoAdapter(Activity context,
                              String[] productInfo, Integer[] imageId, String[] tags) {
        super(context, R.layout.list_product_info, productInfo);
        this.context = context;
        this.productInfo = productInfo;
        this.imageId = imageId;
        this.tags = tags;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_product_info, parent, false);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        TextView tagsTitle = (TextView) rowView.findViewById(R.id.infoTags);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(productInfo[position]);
        tagsTitle.setText(tags[position]);

        imageView.setImageResource(imageId[position]);


        return rowView;
    }
}