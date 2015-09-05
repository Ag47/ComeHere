package io.codeguy.comehere.DataObject;

import java.io.Serializable;

/**
 * Created by KaiHin on 7/21/2015.
 */
public class Product implements Serializable {
    String pid;
    String pPrice;
    String pDiscount;
    String pName;
    String imaageURL;

    String pTypeId;
    String pTypeName;

    String pVendorId;
    String pVendorName;
    String pVendorAddr;

    String latitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    String longitude;
    public String getpVendorAddr() {
        return pVendorAddr;
    }

    public void setpVendorAddr(String pVendorAddr) {
        this.pVendorAddr = pVendorAddr;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getpPrice() {
        return pPrice;
    }

    public void setpPrice(String pPrice) {
        this.pPrice = pPrice;
    }

    public String getpDiscount() {
        return pDiscount;
    }

    public void setpDiscount(String pDiscount) {
        this.pDiscount = pDiscount;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getImaageURL() {
        return imaageURL;
    }

    public void setImaageURL(String imaageURL) {
        this.imaageURL = imaageURL;
    }

    public String getpTypeId() {
        return pTypeId;
    }

    public void setpTypeId(String pTypeId) {
        this.pTypeId = pTypeId;
    }

    public String getpTypeName() {
        return pTypeName;
    }

    public void setpTypeName(String pTypeName) {
        this.pTypeName = pTypeName;
    }

    public String getpVendorId() {
        return pVendorId;
    }

    public void setpVendorId(String pVendorId) {
        this.pVendorId = pVendorId;
    }

    public String getpVendorName() {
        return pVendorName;
    }

    public void setpVendorName(String pVendorName) {
        this.pVendorName = pVendorName;
    }
}
