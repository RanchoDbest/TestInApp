package  in.test.rachana.activities;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import in.test.rachana.Adapter.Restore_Subscription_Adapter;

import in.test.rachana.Global.Constants;
import in.test.rachana.R;
import in.test.rachana.Utils.SkuDetails;
import in.test.rachana.UtilsApp.Data;
import in.test.rachana.UtilsApp.SessionManager;
import com.android.vending.billing.IInAppBillingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static in.test.rachana.Utils.IabHelper.RESPONSE_BUY_INTENT;
import static in.test.rachana.Utils.IabHelper.RESPONSE_CODE;


public class RestoreSubscriptionActivity extends AppCompatActivity {

    RecyclerView rl_restore_data;
    IInAppBillingService mService;
    ServiceConnection mServiceConn;
    public static final int BILLING_RESPONSE_RESULT_OK = 0;
    Bundle bundle;
    ArrayList<Data> datas ;
    Button btnRestoreSubscription;
    String productId,orderId,packageName,purchaseTime,purchaseState,autoRenewing,purchaseToken,startTimeMillis,expiryTimeMillis,auto_renew;
    SessionManager session;
    FormBody.Builder formBody;

    String access_token,expires_in,refresh_token,token_type;
    Data data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_subscription);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        datas = new ArrayList<>();
        rl_restore_data = (RecyclerView) findViewById(R.id.rl_restore_data);
        btnRestoreSubscription= (Button)findViewById(R.id.btn_restore_subscription);

        session = new SessionManager(RestoreSubscriptionActivity.this);

        mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
            }

            @Override
            public void onServiceConnected(ComponentName name,
                                           IBinder service) {
                mService = IInAppBillingService.Stub.asInterface(service);
            }
        };
        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        btnRestoreSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    bundle = mService.getPurchases(3, getPackageName(),
                            "subs", null);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                int response = bundle.getInt("RESPONSE_CODE");
                Log.e("response", "" + response);
                datas.clear();
                if (response == BILLING_RESPONSE_RESULT_OK) {
                    Log.e("response","test");
                    ArrayList<String> ownedSkus =
                            bundle.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                    ArrayList<String> purchaseDataList =
                            bundle.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                    ArrayList<String> signatureList =
                            bundle.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
                    String continuationToken =
                            bundle.getString("INAPP_CONTINUATION_TOKEN");

                    for (int i = 0; i < purchaseDataList.size(); ++i) {
                        String purchaseData = purchaseDataList.get(i);
                        String signature = signatureList.get(i);
                        String sku = ownedSkus.get(i);
                        Log.e("purchaseData1", "" + purchaseData);
                        Log.e("signature1", "" + signature);
                        Log.e("sku1", "" + sku);

                        try {
                            JSONObject jsonObject = new JSONObject(purchaseData);
                             orderId = jsonObject.getString("orderId");
                             packageName = jsonObject.getString("packageName");
                             productId = jsonObject.getString("productId");
                             purchaseTime = jsonObject.getString("purchaseTime");
                             purchaseState = jsonObject.getString("purchaseState");
                             purchaseToken = jsonObject.getString("purchaseToken");
                             autoRenewing = jsonObject.getString("autoRenewing");
                            Log.e("packageNamedd", "" + packageName);
                            data = new Data(orderId, packageName, productId, purchaseTime, purchaseState, purchaseToken, autoRenewing);
                            datas.add(data);
                            //storing all the details in session

                            session = new SessionManager(RestoreSubscriptionActivity.this);
                            session.createUserPurchseData(productId,orderId,packageName,purchaseTime,purchaseState,autoRenewing,purchaseToken);


                            Restore_Subscription_Adapter adapter = new Restore_Subscription_Adapter(datas, RestoreSubscriptionActivity.this, new Restore_Subscription_Adapter.AdapterInterface() {
                                @Override
                                public void buttonPressed(String sProductid) {
                                   /* try {
                                        bundle = mService.getBuyIntent(3, getPackageName(),
                                                sProductid, "subs", "testing");
                                    } catch (RemoteException e) {
                                        e.printStackTrace();
                                    }
                                    PendingIntent pendingIntent = bundle.getParcelable(RESPONSE_BUY_INTENT);
                                    if (bundle.getInt(RESPONSE_CODE) == BILLING_RESPONSE_RESULT_OK) {
                                        try {
                                            startIntentSenderForResult(pendingIntent.getIntentSender(), 0, new Intent(),
                                                    Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
                                        } catch (IntentSender.SendIntentException e) {
                                            e.printStackTrace();
                                        }
                                    }*/

                                    if (Constants.isNetworkAvailable(RestoreSubscriptionActivity.this)) {
                                        try {
                        /*formBody = new FormBody.Builder();
                        formBody.add("grant_type","authorization_code" );
                        formBody.add("code", "4/AACw-RYQA7T1H_aGBwVvq4yra66NbRvj_T6VAQAHDZRfGBWhqTK-cN8");
                        // Note : Add code whatever you get from console developer it looks like  4/AAA1a8HsmAt1NbNEa4EkDykNyOC7iEBJM9aeQYy4xRlp6fmnFLuxRVM

                        //client_id,client_secret,redirect_uri will get from JSON file which can be downloaded by creating service account
                        formBody.add("client_id", "198296149347-pgktvugegln8qie4i4tcqobd4ce9mp66.apps.googleusercontent.com");
                        formBody.add("client_secret", "1oU4zEUrct4Vq5m4fO3hoid9");
                        formBody.add("redirect_uri", "urn:ietf:wg:oauth:2.0:oob");*/


                                            formBody = new FormBody.Builder();
                                            formBody.add("grant_type","refresh_token" );
                                            //  formBody.add("code", "4/AAA1a8HsmAt1NbNEa4EkDykNyOC7iEBJM9aeQYy4xRlp6fmnFLuxRVM");
                                            // Note : Add code whatever you get from console developer it looks like  4/AAA1a8HsmAt1NbNEa4EkDykNyOC7iEBJM9aeQYy4xRlp6fmnFLuxRVM
                                            //client_id,client_secret,redirect_uri will get from JSON file which can be downloaded by creating service account
                                            formBody.add("client_id", "198296149347-pgktvugegln8qie4i4tcqobd4ce9mp66.apps.googleusercontent.com");
                                            formBody.add("client_secret", "1oU4zEUrct4Vq5m4fO3hoid9");
                                            formBody.add("refresh_token", "1/KxxE6Us1rFUGzO3QNc419KkCjvLA7kXuk00ReNvM_l4");


                                            getAccessToken getAccessToken = new getAccessToken(formBody.build());
                                            //here parse the token_uri which is also get from downloaded JSON file
                                            getAccessToken.execute("https://accounts.google.com/o/oauth2/token");

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Toast.makeText(RestoreSubscriptionActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });


                            rl_restore_data.setAdapter(adapter);
                            rl_restore_data.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (purchaseDataList.isEmpty()){
                        Toast.makeText(RestoreSubscriptionActivity.this,"Active subscription plans are not available",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

    }

 @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BILLING_RESPONSE_RESULT_OK) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            Log.e("purchaseDataddd", "" + purchaseData);


            if (resultCode == RESULT_OK) {
                try {
                    JSONObject jo = new JSONObject(purchaseData);

                    productId = jo.getString("productId");
                    orderId = jo.getString("orderId");
                    packageName = jo.getString("packageName");
                    purchaseTime = jo.getString("purchaseTime");
                    purchaseState = jo.getString("purchaseState");
                    autoRenewing = jo.getString("autoRenewing");
                    purchaseToken = jo.getString("purchaseToken");


                } catch (JSONException e) {
                    Log.e("", "Failed to parse purchase data.");
                    e.printStackTrace();
                }


            }
        }
    }


    public class getAccessToken extends AsyncTask<String, Integer, String> {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(7, TimeUnit.MINUTES)
                .build();

        RequestBody requestBody;

        public getAccessToken(RequestBody requestBody) {
            this.requestBody = requestBody;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Request.Builder builder = new Request.Builder();

            builder.post(requestBody);
            builder.url(params[0]);
            Request request = builder.build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            Log.e("getAccessToken", "" + response);


            if (response != null) {
                try {
                    JSONObject JSONresult = new JSONObject(response);

                    access_token = JSONresult.getString("access_token");
                   // refresh_token = JSONresult.getString("refresh_token");
                    expires_in = JSONresult.getString("expires_in");
                    token_type = JSONresult.getString("token_type");

                    if(!JSONresult.has("error")){
                        if(JSONresult.has("access_token")){
                            if (Constants.isNetworkAvailable(RestoreSubscriptionActivity.this)) {
                                try {
                                    getSubscriptionDetails getSubscriptionDetails = new getSubscriptionDetails();
                                    getSubscriptionDetails.execute("https://www.googleapis.com/androidpublisher/v2/applications/"+packageName+"/purchases/subscriptions/"+productId+"/tokens/"+purchaseToken+"?access_token="+access_token);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(RestoreSubscriptionActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                            }
                        }

                    }else{

                        if (Constants.isNetworkAvailable(RestoreSubscriptionActivity.this)) {
                            try {
                                formBody = new FormBody.Builder();
                                formBody.add("grant_type","refresh_token" );
                                //  formBody.add("code", "4/AAA1a8HsmAt1NbNEa4EkDykNyOC7iEBJM9aeQYy4xRlp6fmnFLuxRVM");
                                // Note : Add code whatever you get from console developer it looks like  4/AAA1a8HsmAt1NbNEa4EkDykNyOC7iEBJM9aeQYy4xRlp6fmnFLuxRVM
                                //client_id,client_secret,redirect_uri will get from JSON file which can be downloaded by creating service account
                                formBody.add("client_id", "198296149347-pgktvugegln8qie4i4tcqobd4ce9mp66.apps.googleusercontent.com");
                                formBody.add("client_secret", "1oU4zEUrct4Vq5m4fO3hoid9");
                                formBody.add("refresh_token", refresh_token);


                                getAccessToken getAccessToken = new getAccessToken(formBody.build());

                                //here parse the token_uri which is also get from downloaded JSON file
                                getAccessToken.execute("https://accounts.google.com/o/oauth2/token");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RestoreSubscriptionActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(RestoreSubscriptionActivity.this,"Some error occured",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

        }
    }

    public class getSubscriptionDetails extends AsyncTask<String, Integer, String> {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(7, TimeUnit.MINUTES)
                .build();



        public getSubscriptionDetails() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            Request.Builder builder = new Request.Builder();

            builder.get();
            builder.url(params[0]);
            Request request = builder.build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            Log.e("getSubscriptionDetails", "" + response);


            if (response != null) {
                try {
                    JSONObject JSONresult = new JSONObject(response);

                    startTimeMillis = JSONresult.getString("startTimeMillis");
                    expiryTimeMillis = JSONresult.getString("expiryTimeMillis");
                    auto_renew = JSONresult.getString("autoRenewing");



                  /*  public static String getDate(long milliSeconds, String dateFormat) {
                        // Create a DateFormatter object for displaying date in specified format.

                    }*/

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                    // Create a calendar object that will convert the date and time value in milliseconds to date.
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(Long.parseLong(startTimeMillis));

                    Calendar calendarend = Calendar.getInstance();
                    calendarend.setTimeInMillis(Long.parseLong(expiryTimeMillis));

                    session.startEndDate(formatter.format(calendar.getTime()),formatter.format(calendarend.getTime()));
                    session.createUserPurchseData(productId,orderId,packageName,purchaseTime,purchaseState,autoRenewing,purchaseToken);

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

        }
    }

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
