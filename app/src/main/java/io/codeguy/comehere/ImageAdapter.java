package io.codeguy.comehere;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ImageAdapter extends PagerAdapter {
    public static String[] loadURL = new String[]{
            "http://xamarin.com/resources/design/home/devices.png",
            "http://xamarin.com/resources/design/home/devices.png",
            "http://www.att.com/catalog/en/skus/images/apple-iphone%205c%20-%208gb-white-100x160.jpg                                 "
    };
    Context context;
    private int[] GalImages = new int[]{
//            R.drawable.one,
//            R.drawable.two,
//            R.drawable.three
    };

    ImageAdapter(Context context) {
        this.context = context;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    @Override
    public int getCount() {
        return loadURL.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_medium);
        imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//        imageView.setImageResource(GalImages[position]);
        //        imageView.setImageResource(GalImages[position]);

        Bitmap imageBitmap = getBitmapFromURL(loadURL[position]);
        imageView.setImageBitmap(imageBitmap);
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }
}


