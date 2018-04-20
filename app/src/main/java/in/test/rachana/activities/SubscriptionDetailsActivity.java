package in.test.rachana.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ListMenuItemView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.test.rachana.BuildConfig;
import in.test.rachana.R;
import in.test.rachana.Utils.IabHelper;
import in.test.rachana.Utils.IabResult;
import in.test.rachana.Utils.Inventory;
import in.test.rachana.Utils.Purchase;
import in.test.rachana.Utils.Security;

import java.security.PublicKey;

public class SubscriptionDetailsActivity extends AppCompatActivity {

    TextView txt_policy;
    String termsOfUse,privacyPolicy;

    private static final String TAG = "INAPPPURCHASE";
    IabHelper mHelper;
    static final String ITEM_SKU_1 = "mehulggohil";
    static final String ITEM_SKU_3 = "rachana_test";
    static final String ITEM_SKU_6 = "6_monthspaymentuk";
    static final String ITEM_SKU_life = "Life_timepaymentuk";

    LinearLayout lin_one_month_plan, lin_three_month_plan, lin_six_month_plan, lin_lifetime_plan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        lin_one_month_plan = (LinearLayout) findViewById(R.id.lin_one_month_plan);
        lin_three_month_plan = (LinearLayout) findViewById(R.id.lin_three_month_plan);
        lin_six_month_plan = (LinearLayout) findViewById(R.id.lin_six_month_plan);
        lin_lifetime_plan = (LinearLayout) findViewById(R.id.lin_lifetime_plan);

        txt_policy = findViewById(R.id.txt_policy);
        //Put your url of terms and  privacy

        termsOfUse = "<u><font color=\"blue\"> terms of use </font></u>";
        privacyPolicy =  "<u><font color=\"blue\"> privacy policy </font> </u>";
        txt_policy.setText(Html.fromHtml("By purchasing, you agree to the" + termsOfUse +"and" + privacyPolicy));

        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmFRc40GI6o3N92vIhJdrIaPsrGjjV7cDNiXeAX6PqHquBilLdeQJScQJ/KFx+44yNqPof2UWecb/mWI/IXNizIxxhK93ktDJF3U/En3h2KRHb5tExdCMwYE/NJzLjwMDSjssMiLaoVslxFmPja0J35MeXlhKQVG/qYdqzjpYkCLWH7ddzv7rH+SJydYvKwNN9Z9gS6UGxxFmNu/briwEPOSqpQOOHIZf2sNM4wk+fTMUy3JTJqFvHpGduBBIUtqytnFTOwcY0x4C0u7XgFBtxBcrw7bbfKCqIGAQSMFM9UAf3KwzGkZ7Xxw0hWvNk3swenJPRTBMVIyhBp71J6rjrQIDAQAB";

        txt_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SubscriptionDetailsActivity.this, TermsOfUseActivity.class));
            }
        });

        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.d(TAG, "In-app Billing setup failed: " + result);
                } else {
                    Log.d(TAG, "In-app Billing is set up OK");
                }
            }
        });

        lin_one_month_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyClick();
            }
        });

        lin_three_month_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                three_buyClick();
            }
        });
        lin_six_month_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                six_buyClick();

            }
        });
        lin_lifetime_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                life_buyClick();
            }
        });

    }


    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {
                        Log.e("Hello","Test");
                       Log.e("Result999999999", String.valueOf(result));
                    } else {
                        // handle error
                    }
                }
            };




    public void buyClick() {
        Log.e("Clicked","Clicked");
        try {
            mHelper.launchSubscriptionPurchaseFlow(this, ITEM_SKU_1, 10001, mPurchaseFinishedListener, "mypurchasetoken");
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }

    public void three_buyClick() {
        Log.e("Clicked3","Clicked");
        try {
            mHelper.launchSubscriptionPurchaseFlow(this, ITEM_SKU_3, 10001, mPurchaseFinishedListener, "mypurchasetoken");
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }
    public void six_buyClick() {
        Log.e("Clicked6","Clicked");
        try {
            mHelper.launchSubscriptionPurchaseFlow(this, ITEM_SKU_6, 10001, mPurchaseFinishedListener, "mypurchasetoken");
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }
    public void life_buyClick() {
        Log.e("Clickedl","Clicked");
        try {
            mHelper.launchSubscriptionPurchaseFlow(this, ITEM_SKU_life,10001 , mPurchaseFinishedListener, "mypurchasetoken");
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }



    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }


            else if (purchase.getSku().equals(ITEM_SKU_1)) {

                Log.e("Subscription_1","Subscription_1");

            }
            else if (purchase.getSku().equals(ITEM_SKU_3)) {
                Log.e("Subscription_3","Subscription_3");
            }
            else if (purchase.getSku().equals(ITEM_SKU_6)) {
                Log.e("Subscription_6", "Subscription_6");
            }
            else if (purchase.getSku().equals(ITEM_SKU_life)) {
                Log.e("Subscription_life","Subscription_life");
            }


        }
    };

    public void consumeItem() {
        try {
            mHelper.queryInventoryAsync(mReceivedInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {

            if (result.isFailure()) {

                // Handle failure
            } else {

            }
        }
    };

    public static boolean verifyPurchase(String base64PublicKey,
                                         String signedData, String signature) {
        if (TextUtils.isEmpty(signedData) ||
                TextUtils.isEmpty(base64PublicKey) ||
                TextUtils.isEmpty(signature)) {
            Log.e(TAG, "Purchase verification failed: missing data.");
            if (BuildConfig.DEBUG) {
                return true;
            }
            return false;
        }

        PublicKey key = Security.generatePublicKey(base64PublicKey);
        return Security.verify(key, signedData, signature);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) try {
            mHelper.dispose();
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
        mHelper = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {

            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

