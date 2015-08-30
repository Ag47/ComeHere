//package io.codeguy.comehere.menu_item;
//
//import android.app.Fragment;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapFragment;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.maps.android.ui.IconGenerator;
//
//import org.json.JSONObject;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import io.codeguy.comehere.MySingleton;
//import io.codeguy.comehere.R;
//
//
//public class PromotionFragment extends Fragment implements OnMapReadyCallback {
//
//    public PromotionFragment() {
//    }
//
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container,
////                             Bundle savedInstanceState) {
////
////        View rootView = inflater.inflate(R.layout.fragment_promotion, container, false);
////
////        MapFragment mapFragment = (MapFragment) getFragmentManager()
////                .findFragmentById(R.id.map);
////        mapFragment.getMapAsync(this);
////
////        return rootView;
////    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        final View rootView = inflater.inflate(R.layout.fragment_promotion, container, false);
//
//        MapFragment mapFragment = (MapFragment) getFragmentManager()
//                .findFragmentById(R.id.mapView);
//        mapFragment.getMapAsync(this);
//
//        return rootView;
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        LatLng apliu = new LatLng(22.3291536, 114.1633124);
//
//        googleMap.setMyLocationEnabled(true);
////        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(apliu, 19));
////// Zoom in, animating the camera.
////        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
////
////// Zoom out to zoom level 10, animating with a duration of 2 seconds.
////        googleMap.animateCamera(CameraUpdateFactory.zoomTo(19), 3000, null);
////        googleMap.addMarker(new MarkerOptions()
////                .title("Apliu")
////                .snippet("Best Flea Market in HK")
////                .position(apliu));
//
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.3291536, 114.1633124), 18));
//
//        IconGenerator iconFactory = new IconGenerator(getActivity());
//        addIcon(iconFactory, "Apliu YEAH", new LatLng(22.331096, 114.161008), googleMap);
//
//        iconFactory.setColor(Color.CYAN);
//        addIcon(iconFactory, "BEST IT", new LatLng(22.330053, 114.161008), googleMap);
//
//        iconFactory.setRotation(90);
//        iconFactory.setStyle(IconGenerator.STYLE_RED);
//        addIcon(iconFactory, "Buy one get one Free", new LatLng(22.330175, 114.16224), googleMap);
//
//        iconFactory.setContentRotation(-90);
//        iconFactory.setStyle(IconGenerator.STYLE_PURPLE);
//        addIcon(iconFactory, "SELL", new LatLng(22.330039, 114.161891), googleMap);
//
//        iconFactory.setRotation(0);
//        iconFactory.setContentRotation(90);
//        iconFactory.setStyle(IconGenerator.STYLE_GREEN);
//        addIcon(iconFactory, "haha", new LatLng(22.330109, 114.162131), googleMap);
//
//    }
//
//    private void addIcon(IconGenerator iconFactory, String text, LatLng position, GoogleMap map) {
//        MarkerOptions markerOptions = new MarkerOptions().
//                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
//                position(position).
//                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
//
//        map.addMarker(markerOptions);
//    }
//}