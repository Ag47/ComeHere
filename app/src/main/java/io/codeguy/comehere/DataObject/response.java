package io.codeguy.comehere.DataObject;

/**
 * Created by KaiHin on 7/8/2015.
 */
public class response {
    private String id;
    private String name;
    private String timeExpired;
    private String installedKey;
    private String iconThum;
    private String shopperName;
    private String responseProductPrice;
    private String responseProductDiscount;
    private String responseProductImage;
    private String shopperLocation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstalledKey() {
        return installedKey;
    }

    public void setInstalledKey(String installedKey) {
        this.installedKey = installedKey;
    }

    public String getTimeExpired() {
        return timeExpired;
    }

    public void setTimeExpired(String timeExpired) {
        this.timeExpired = timeExpired;
    }

    public String getIcon_thum() {
        return iconThum;
    }

    public void setIcon_thum(String iconThum) {
        this.iconThum = iconThum;
    }

    public String getShopperName() {
        return shopperName;
    }

    public void setShopperName(String shopperName) {
        this.shopperName = shopperName;
    }

    public String getResponseProductPrice() {
        return responseProductPrice;
    }

    public void setResponseProductPrice(String responseProductPrice) {
        this.responseProductPrice = responseProductPrice;
    }

    public String getResponseProductDiscount() {
        return responseProductDiscount;
    }

    public void setResponseProductDiscount(String responseProductDiscount) {
        this.responseProductDiscount = responseProductDiscount;
    }

    public String getResponseProductImage() {
        return responseProductImage;
    }

    public void setResponseProductImage(String responseProductImage) {
        this.responseProductImage = responseProductImage;
    }

    public String getShopperLocation() {
        return shopperLocation;
    }

    public void setShopperLocation(String shopperLocation) {
        this.shopperLocation = shopperLocation;
    }
}
