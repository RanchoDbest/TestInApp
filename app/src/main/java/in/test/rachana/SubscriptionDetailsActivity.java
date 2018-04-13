package in.test.rachana;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import in.test.rachana.Utils.IabHelper;
import in.test.rachana.Utils.IabResult;
import in.test.rachana.Utils.Inventory;
import in.test.rachana.Utils.Purchase;
import in.test.rachana.Utils.SkuDetails;

public class SubscriptionDetailsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 10001;
    private static final String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmFRc40GI6o3N92vIhJdrIaPsrGjjV7cDNiXeAX6PqHquBilLdeQJScQJ/KFx+44yNqPof2UWecb/mWI/IXNizIxxhK93ktDJF3U/En3h2KRHb5tExdCMwYE/NJzLjwMDSjssMiLaoVslxFmPja0J35MeXlhKQVG/qYdqzjpYkCLWH7ddzv7rH+SJydYvKwNN9Z9gS6UGxxFmNu/briwEPOSqpQOOHIZf2sNM4wk+fTMUy3JTJqFvHpGduBBIUtqytnFTOwcY0x4C0u7XgFBtxBcrw7bbfKCqIGAQSMFM9UAf3KwzGkZ7Xxw0hWvNk3swenJPRTBMVIyhBp71J6rjrQIDAQAB";
    private IabHelper iabHelper;
    Button buyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_details);

        iabHelper = new IabHelper(this, base64EncodedPublicKey);
        iabHelper.enableDebugLogging(true, "TEST");

        buyButton = (Button) findViewById(R.id.buyButton);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initSubscription("mehulggohil",mPurchaseFinishedListener);
                initSubscriptionWithExtras("mehulggohil",  mPurchaseFinishedListener, "");

            }
        });
        setup();
    }


    private void setup() {
        if (iabHelper != null) {
            iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                @Override
                public void onIabSetupFinished(IabResult result) {
                    if (result.isFailure()) {
                        Log.d("TEST", "Problem setting up In-app Billing: " + result);
                        dispose();
                    }
                }
            });
        }
    }


    public void initSubscriptionWithExtras(final String subscriptionType,
                                           final IabHelper.OnIabPurchaseFinishedListener subscriptionFinishedListener,
                                           String payload) {
        if (iabHelper != null) {
            try {
                iabHelper.launchSubscriptionPurchaseFlow(this,
                        subscriptionType,
                        REQUEST_CODE,
                        mPurchaseFinishedListener,
                        payload
                );
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
            //In case you get below error:
            //`Can't start async operation (refresh inventory) because another async operation (launchPurchaseFlow) is in progress.`
            //Include this line of code to end proccess after purchase
            //iabHelper.flagEndAsync();
        }
    }

    public void getSkuDetailsList(
            final ArrayList<String> skuIdsList,
            final SubscriptionInventoryListener subscriptionInventoryListener
    ) {
        if (iabHelper != null) {
            try {
                iabHelper.queryInventoryAsync(true, null, skuIdsList, new IabHelper.QueryInventoryFinishedListener() {
                    @Override
                    public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                        if (result.isFailure()) {
                            Log.d("TEST", "Problem querying inventory: " + result);
                            dispose();
                            return;
                        }
                        ArrayList<SkuDetails> skuDetailsList = new ArrayList<>();
                        for (String skuId : skuIdsList) {
                            SkuDetails sku = inventory.getSkuDetails(skuId);
                            if (sku.getSku().equals(skuId)) {
                                skuDetailsList.add(sku);
                                sku.getPrice();
                            }
                        }

                        if (subscriptionInventoryListener != null) {
                            subscriptionInventoryListener.onQueryInventoryFinished(skuDetailsList);
                        }
                    }
                });
            } catch (IabHelper.IabAsyncInProgressException e) {
                Log.e("TEST", "EXCEPTION:" + e.getMessage());
            }
        }

    }

    public void dispose() {
        if (iabHelper != null) {
            try {
                iabHelper.dispose();
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
            iabHelper = null;
        }
    }

    public IabHelper getIabHelper() {
        if (iabHelper == null) {
            iabHelper = new IabHelper(this, base64EncodedPublicKey);
        }
        return iabHelper;
    }

    public interface SubscriptionInventoryListener {
        void onQueryInventoryFinished(ArrayList<SkuDetails> skuList);
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase)
        {
            if (result.isFailure()) {

                Log.e("Qkkaskksks","Fails");
                // Handle error
                return;
            }
            else if (purchase.getSku().equals("mehulggohil")) {
                Log.e("MehulSuccess","MehulSuccess");

            }

        }
    };


    public interface SubscriptionFinishedListener{
        void onSuccess();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Rachu","1111");
        if (!getIabHelper().handleActivityResult(requestCode, resultCode, data)) {
            Log.e("Rachu","qqq");
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
