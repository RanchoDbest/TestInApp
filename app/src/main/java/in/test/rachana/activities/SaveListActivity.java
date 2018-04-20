package in.test.rachana.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import in.test.rachana.Adapter.SavedListAdapter;
import in.test.rachana.R;
import in.test.rachana.UtilsApp.GlobalUtils;
import in.test.rachana.UtilsApp.SessionManager;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class SaveListActivity extends AppCompatActivity {

    private SwipeMenuListView listView;
    private SavedListAdapter listAdapter;
    private ArrayList<String> pdfArrayList = new ArrayList<>();
    private static final String PDF_MIME_TYPE = "application/pdf";


    String sTodayDate;
    Date today,strDate,enDate;
    Boolean stop;

    HashMap<String, String> purchase;
    SessionManager session;

    InterstitialAd mInterstitialAd;
    AdRequest adRequest;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savelist);

        session = new SessionManager(SaveListActivity.this);
//        session.startEndDate("2018-4-16","2018-4-18");
        // session.createUserPurchseData("Life_timepaymentuk",null,null,null,null,null,null);
        purchase = session.getUserDetails();
        Log.e("SavedList","Ture");

        listView = (SwipeMenuListView) findViewById(R.id.listView);
        listAdapter = new SavedListAdapter(this);

        adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice("655545AEB10CBDC8FF5224EC9B5A0A5D")
                .build();

        File dirFile = new File(Environment.getExternalStorageDirectory() + File.separator + "UKPaystub/pdf");

        String[] pdfList =  dirFile.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".pdf");
            }
        });

        if (pdfList != null) {
            for (int i = 0; i < pdfList.length; i++) {
                pdfArrayList.add(pdfList[i]);
            }
        }

        listAdapter.items = pdfArrayList;
        listAdapter.notifyDataSetChanged();
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openPDF(SaveListActivity.this, i);
            }
        });

        initListViewMenu();
    }

    private void initListViewMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width

                openItem.setWidth( (int) GlobalUtils.pxFromDp(SaveListActivity.this, 80));
                // set item title
                openItem.setTitle("Share");
                // set item title fontsize
                openItem.setTitleSize(17);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setTitle("Delete");
                deleteItem.setTitleSize(17);
                deleteItem.setTitleColor(Color.WHITE);
                // set item width
                deleteItem.setWidth( (int) GlobalUtils.pxFromDp(SaveListActivity.this, 80));
                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        shareViaEmail(index);
                        break;
                    case 1:
                        // delete
                        deletePdf(position);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
    }

    private void shareViaEmail(int index) {
        String pdfPath = getPdfPath(index);
        Intent emailSelectorIntent = new Intent(Intent.ACTION_SENDTO);
        emailSelectorIntent.setData(Uri.parse("mailto:"));

        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"address@mail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        emailIntent.setSelector( emailSelectorIntent );

        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(pdfPath)));

        startActivity(emailIntent);
    }

    private void deletePdf(int index) {
        String pdfPath = getPdfPath(index);
        File pdfFile = new File(pdfPath);
        if (pdfFile.exists()) {
            pdfFile.delete();
            pdfArrayList.remove(index);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e("handler", "called");
                    SaveListActivity.this.refreshListView();
                }
            }, 500);
        }
    }

    private void refreshListView() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listAdapter.items = pdfArrayList;
                listAdapter.notifyDataSetChanged();
            }
        });
    }

    private String getPdfPath(int index) {
        String pdfDirPath = Environment.getExternalStorageDirectory() + File.separator + "UKPaystub/pdf";
        String fullPath = pdfDirPath + File.separator + pdfArrayList.get(index);
        return fullPath;
    }

    public void openPDF(Context context, int index) {
        String pdfPath = getPdfPath(index);
        File pdfFile = new File(pdfPath);
        if (pdfFile.exists()) {
            Uri localUri = Uri.fromFile(pdfFile);
            Intent i = new Intent( Intent.ACTION_VIEW );
            i.setDataAndType( localUri, PDF_MIME_TYPE );
            context.startActivity( i );
        }
    }

    @Override
    public void onPause() {

        Log.e("Pause","Pause");
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("Resume","Resume");
        stop = false;

        if (purchase.get(SessionManager.sproductId) != null && purchase.get(SessionManager.sproductId).equalsIgnoreCase("Life_timepaymentuk")) {

            Toast.makeText(this,"You have saved List",Toast.LENGTH_SHORT).show();

        }else {

            if (purchase.get(SessionManager.startDate) != null && purchase.get(SessionManager.endDate) != null) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    today = sdf.parse(sTodayDate);
                    strDate = sdf.parse(purchase.get(SessionManager.startDate));

                    enDate = sdf.parse(purchase.get(SessionManager.endDate));

                    if (today.compareTo(strDate) >= 0 && today.compareTo(enDate) < 1) {
                        //mAdView.setVisibility(View.GONE);
                    } else {

                        Log.e("WrongDate","WrongDate");

                        //mAdView.setVisibility(View.VISIBLE);
                        mInterstitialAd = new InterstitialAd(SaveListActivity.this);
                        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

                        mInterstitialAd.loadAd(adRequest);

                        handler = new Handler();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("stop",""+stop);
                                Log.e("OneMinuteCall", "3");
                                if(!stop) {

                                    Log.e("OneMinuteCall", "2");
                                    mInterstitialAd.loadAd(adRequest);
                                    mInterstitialAd.setAdListener(new AdListener() {
                                        public void onAdLoaded() {
                                            if (mInterstitialAd.isLoaded()) {
                                                mInterstitialAd.show();
                                            }
                                        }
                                    });
                                }
                            }
                        }, 60000);


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("Startdate", "null");
                mInterstitialAd = new InterstitialAd(SaveListActivity.this);
                mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("stop",""+stop);
                        Log.e("OneMinuteCall", "4");
                        if(!stop) {

                            Log.e("OneMinuteCall", "1");
                            mInterstitialAd.loadAd(adRequest);
                            mInterstitialAd.setAdListener(new AdListener() {
                                public void onAdLoaded() {
                                    if (mInterstitialAd.isLoaded()) {
                                        mInterstitialAd.show();
                                    }
                                }
                            });
                        }
                    }
                }, 60000);

            }
        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Start","Start");
        stop = false;
    }

    protected void onStop() {
        super.onStop();
        Log.e("Stop","Stop");
        stop = true;
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


}
