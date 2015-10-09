package io.codeguy.comehere.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.Utils;

import java.util.ArrayList;
import java.util.Collection;

import io.codeguy.comehere.R;


/**
 * Displays basic information about beacon.
 *
 * @author wiktor@estimote.com (Wiktor Gworek)
 */
public class BeaconListAdapter extends BaseAdapter {

  private int TAG_MING_CHU_BLDGE_MAJOR = 49303;
  private int TAG_NAM_POINT_BLDGE_MAJOR = 33187;
  private int TAG_AUDIO_LTD_MAJOR = 12159;

  private ArrayList<Beacon> beacons;
  private LayoutInflater inflater;
  private boolean once = true;
  public BeaconListAdapter(Context context) {
    this.inflater = LayoutInflater.from(context);
    this.beacons = new ArrayList<>();
  }

  public void replaceWith(Collection<Beacon> newBeacons) {
    this.beacons.clear();
    this.beacons.addAll(newBeacons);
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    Log.v("7/10","count size" + beacons.size());
    return beacons.size();
  }

  @Override
  public Beacon getItem(int position) {
    return beacons.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View view, ViewGroup parent) {
    view = inflateIfRequired(view, position, parent);
    bind(getItem(position), view);
    return view;
  }

  private void bind(Beacon beacon, View view) {
    ViewHolder holder = (ViewHolder) view.getTag();
//    holder.macTextView.setText(String.format("MAC: %s (%.2fm)", beacon.getMacAddress(), Utils.computeAccuracy(beacon)));
//    holder.rssiTextView.setText("RSSI: " + beacon.getRssi());
    if(beacon.getMajor() == TAG_MING_CHU_BLDGE_MAJOR){
      holder.shopperIcon.setImageResource(R.drawable.ming_chu_bldg_shopper);
      holder.name.setText("Ming Chu Bldg");
      holder.distance.setText("Distance: " +  String.valueOf(Utils.computeAccuracy(beacon)).substring(0,4)+"m");
      holder.type.setText("phone");
    }
    if(beacon.getMajor() == TAG_NAM_POINT_BLDGE_MAJOR)
    {
      holder.shopperIcon.setImageResource(R.drawable.nam_pont_bldg_shopper);
      holder.name.setText("Nam Pont Bldg");
      holder.distance.setText("Distance: " +  String.valueOf(Utils.computeAccuracy(beacon)).substring(0,4)+"m");
      holder.type.setText("Type: Watch");
    }
    if(beacon.getMajor() == TAG_AUDIO_LTD_MAJOR)
    {
      holder.shopperIcon.setImageResource(R.drawable.audio_ltd);
      holder.name.setText("Audio Tech Ltd.");
      holder.distance.setText("Distance: " +  String.valueOf(Utils.computeAccuracy(beacon)).substring(0,4)+"m");
      holder.type.setText("Audio");
    }
    Log.v("beaconAdapter",  beacon.getMacAddress().toHexString());
    Log.v("beaconAdapter", "cm" + Utils.computeAccuracy(beacon));
//    holder.majorTextView.setText("Major: " + beacon.getMajor());
//    holder.minorTextView.setText("Minor: " + beacon.getMinor());
//    holder.measuredPowerTextView.setText("MPower: " + beacon.getMeasuredPower());
//      holder.rssiTextView.setText("RSSI: " + beacon.getRssi());
    }

    private View inflateIfRequired(View view, int position, ViewGroup parent) {
    if (view == null) {
      view = inflater.inflate(R.layout.beacon_item, null);
      view.setTag(new ViewHolder(view));
    }
    return view;
  }

  static class ViewHolder {
    final TextView name;
    final TextView distance;
    final TextView type;

//    final TextView macTextView;
//    final TextView majorTextView;
//    final TextView minorTextView;
//    final TextView measuredPowerTextView;
//    final TextView rssiTextView;
    final ImageView shopperIcon;
    ViewHolder(View view) {
//      macTextView = (TextView) view.findViewWithTag("mac");
//      majorTextView = (TextView) view.findViewWithTag("major");
//      minorTextView = (TextView) view.findViewWithTag("minor");
//      measuredPowerTextView = (TextView) view.findViewWithTag("mpower");
//      rssiTextView = (TextView) view.findViewWithTag("rssi");
      name = (TextView) view.findViewById(R.id.beacon_name);
      distance = (TextView) view.findViewById(R.id.beacon_distance);
      type = (TextView) view.findViewById(R.id.beacon_type);
      shopperIcon = (ImageView) view.findViewById(R.id.shopper_icon);
    }
  }
  private void db(Beacon beacon, View view){
    Log.v("beaconAdapter","inside");
    Log.v("mac 7/10"," "+beacon.getMacAddress());
//    if(beacon.getMacAddress().toString() == "FE:87:1F:B2:81:A3"){
//       ViewHolder holder = (ViewHolder) view.getTag();
//      holder.rssiTextView.setText("hahahahahahhahahah");
//      Log.v("7/10","inside hahaha");
//    }
  }
}
