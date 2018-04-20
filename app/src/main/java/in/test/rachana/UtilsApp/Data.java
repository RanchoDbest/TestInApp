package in.test.rachana.UtilsApp;

/**
 * Created by mehul on 4/15/2018.
 */

public class Data {
    String sorderId,spackageName,sproductId,spurchaseTime,spurchaseState,spurchaseToken,sautoRenewing;


    public Data(String orderId, String packageName, String productId, String purchaseTime, String purchaseState, String purchaseToken, String autoRenewing) {
    this.sorderId=orderId;
    this.spackageName=packageName;
    this.sproductId=productId;
    this.spurchaseTime=purchaseTime;
    this.spurchaseState=purchaseState;
    this.spurchaseToken=purchaseToken;
    this.sautoRenewing=autoRenewing;
    }

    public String getSorderId() {
        return sorderId;
    }

    public void setSorderId(String sorderId) {
        this.sorderId = sorderId;
    }

    public String getSpackageName() {
        return spackageName;
    }

    public void setSpackageName(String spackageName) {
        this.spackageName = spackageName;
    }

    public String getSproductId() {
        return sproductId;
    }

    public void setSproductId(String sproductId) {
        this.sproductId = sproductId;
    }

    public String getSpurchaseTime() {
        return spurchaseTime;
    }

    public void setSpurchaseTime(String spurchaseTime) {
        this.spurchaseTime = spurchaseTime;
    }

    public String getSpurchaseState() {
        return spurchaseState;
    }

    public void setSpurchaseState(String spurchaseState) {
        this.spurchaseState = spurchaseState;
    }

    public String getSpurchaseToken() {
        return spurchaseToken;
    }

    public void setSpurchaseToken(String spurchaseToken) {
        this.spurchaseToken = spurchaseToken;
    }

    public String getSautoRenewing() {
        return sautoRenewing;
    }

    public void setSautoRenewing(String sautoRenewing) {
        this.sautoRenewing = sautoRenewing;
    }
}
