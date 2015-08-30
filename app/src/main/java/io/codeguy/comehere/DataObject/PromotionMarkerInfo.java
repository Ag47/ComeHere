package io.codeguy.comehere.DataObject;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by KaiHin on 8/11/2015.
 */
public class PromotionMarkerInfo implements ClusterItem {
    private String name;
    private String time;
    private Integer shopperIcon;
    private LatLng pointLocation;
    private String shopperLocation;

    public PromotionMarkerInfo(String name, String time, LatLng pointLocation, String shopperLocation, Integer shopperIcon) {
        this.name = name;
        this.time = time;
        this.pointLocation = pointLocation;
        this.shopperLocation = shopperLocation;
        this.shopperIcon = shopperIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public LatLng getPointLocation() {
        return pointLocation;
    }

    public void setPointLocation(LatLng pointLocation) {
        this.pointLocation = pointLocation;
    }

    public Integer getShopperIcon() {
        return shopperIcon;
    }

    public void setShopperIcon(Integer shopperIcon) {
        this.shopperIcon = shopperIcon;
    }

    public String getShopperLocation() {
        return shopperLocation;
    }

    public void setShopperLocation(String shopperLocation) {
        this.shopperLocation = shopperLocation;
    }

    @Override
    public LatLng getPosition() {
        return pointLocation;
    }
}
