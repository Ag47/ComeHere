package io.codeguy.comehere.DataObject;

/**
 * Created by KaiHin on 8/13/2015.
 */
public class PromotionInfo {
    private String content, promotionID, title, time,
            vShopNName, shopperIcon, vAddr;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPromotionID() {
        return promotionID;
    }

    public void setPromotionID(String promotionID) {
        this.promotionID = promotionID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getvShopNName() {
        return vShopNName;
    }

    public void setvShopNName(String vShopNName) {
        this.vShopNName = vShopNName;
    }

    public String getShopperIcon() {
        return shopperIcon;
    }

    public void setShopperIcon(String shopperIcon) {
        this.shopperIcon = shopperIcon;
    }

    public String getvAddr() {
        return vAddr;
    }

    public void setvAddr(String vAddr) {
        this.vAddr = vAddr;
    }
}
