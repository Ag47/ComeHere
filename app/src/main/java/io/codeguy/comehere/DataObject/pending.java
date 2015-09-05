package io.codeguy.comehere.DataObject;

/**
 * Created by KaiHin on 7/8/2015.
 */
public class pending {
    private String id;
    private String name;
    private String timeExpired;
    private String installedKey;
//    private String iconThum;


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

//    public String getIcon_thum() {
//        return iconThum;
//    }

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

//    public void setIcon_thum(String iconThum) {
//        this.iconThum = iconThum;
//    }
}
