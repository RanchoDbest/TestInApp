package in.test.rachana.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import in.test.rachana.R;
import in.test.rachana.Utils.Inventory;
import in.test.rachana.UtilsApp.SessionManager;


public class CreateActivity extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout pdfLayout, mainLayout;
    private AdView mAdView;
    Inventory inventory;
    Button btnGenerate;
    String sTodayDate;
    Date today,strDate,enDate;
    Boolean stop;

       /* PDF Render View */
    private TextView tvEmpNo, tvEmpName, tvProcessDate,
                        tvNINo, tvUnits, tvRate, tvAmount, tvTotalPayment, tvIncomeTax,
                        tvNI, tvTotalDeduction, tvEmpName1, tvEmpAddress, tvTotalPayment1,
                        tvTotalDeduction1, tvTexableGrossPay, tvIncomeTax1, tvEmployeeNIC, tvEmployerNIC,
                        tvCompanyName, tvTaxCode, tvNITable, tvDept, tvTaxPeroid, tvPaymentMethod, tvNetPay;

    /* Input Views */
    private EditText etEmpNo, etEmpName, etProcessDate, etEmpAddr1, etEmpAddr2, etEmpAddr3,
                        etCompanyName, etCompanyAddr1, etCompanyAddr2, etCompanyAddr3, etTaxCode, etNINo,
                        etIncomeTax, etNI, etTotalDeductions, etEmployerNIC, etNITable, etDepart, etNetPay,
                        etDescription, etRate, etHour, etAmount;

    private String[] arrPaymentTypes = {"Bank","BACS", "CASH", "Cheque", "Others"};
    private Spinner spPaymentType;
    private String paymentType = "";
    private SharedPreferences _preference;

    static final String ITEM_SKU_1 = "mehulggohil";
    static final String ITEM_SKU_3 = "rachana_test";
    static final String ITEM_SKU_6 = "6_monthspaymentuk";
    static final String ITEM_SKU_life = "Life_timepaymentuk";


    //sharedprefence for subscription plans

    HashMap<String, String> purchase;
    in.test.rachana.UtilsApp.SessionManager session;

     InterstitialAd mInterstitialAd;
     AdRequest adRequest;

     Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        initViews();

        session = new in.test.rachana.UtilsApp.SessionManager(CreateActivity.this);
     //   session.startEndDate("2018-4-19","2018-4-21");
      //  session.createUserPurchseData("life_timepaymentuk",null,null,null,null,null,null);

        purchase = session.getUserDetails();
        mAdView = (AdView) findViewById(R.id.adView);


        Log.e("sproductIdsproductId",""+purchase.get(in.test.rachana.UtilsApp.SessionManager.sproductId));
        Log.e("spstartDate",""+purchase.get(in.test.rachana.UtilsApp.SessionManager.startDate));
        Log.e("spEWndtDate",""+purchase.get(SessionManager.endDate));


        inventory = new Inventory();
        stop = false;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        sTodayDate = mdformat.format(calendar.getTime());


        adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice("655545AEB10CBDC8FF5224EC9B5A0A5D")
                .build();


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (id == 1) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }

        return null;

    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            etProcessDate.setText(day + "-" + new DateFormatSymbols().getShortMonths()[month] + "-" + year);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGenerate:

                if (purchase.get(in.test.rachana.UtilsApp.SessionManager.sproductId) != null && purchase.get(in.test.rachana.UtilsApp.SessionManager.sproductId).equalsIgnoreCase("Life_timepaymentuk")) {

                    onBtnGenerate();
                } else {
                    if (strDate != null && enDate != null) {
                        if (today.compareTo(strDate) >= 0 && today.compareTo(enDate) < 1) {
                            onBtnGenerate();
                        } else {

                            final Dialog dialog = new Dialog(CreateActivity.this);
                            dialog.setContentView(R.layout.subscription_dialog);

                            TextView tv_subsctiption_now = (TextView) dialog.findViewById(R.id.tv_subsctiption_now);
                            TextView tv_subsctiption_cancel = (TextView) dialog.findViewById(R.id.tv_subsctiption_cancel);


                            tv_subsctiption_now.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(CreateActivity.this, SubscriptionDetailsActivity.class));
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                            tv_subsctiption_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();

                        }
                    } else {

                        final Dialog dialog = new Dialog(CreateActivity.this);
                        dialog.setContentView(R.layout.subscription_dialog);

                        TextView tv_subsctiption_now = (TextView) dialog.findViewById(R.id.tv_subsctiption_now);
                        TextView tv_subsctiption_cancel = (TextView) dialog.findViewById(R.id.tv_subsctiption_cancel);


                        tv_subsctiption_now.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(CreateActivity.this, SubscriptionDetailsActivity.class));
                                dialog.dismiss();
                                finish();
                            }
                        });
                        tv_subsctiption_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                    }
                    break;
                }
        }
    }

    private void initViews() {
        initRenderView();
        initInputViews();

        btnGenerate = findViewById(R.id.btnGenerate);
        btnGenerate.setOnClickListener(this);

        pdfLayout = (RelativeLayout) findViewById(R.id.layoutPDF);
        mainLayout = (RelativeLayout) findViewById(R.id.layoutMain);

        loadValues();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {

            } else {
                requestPermission();
            }
        }
    }

    private void saveCurrentValues() {
        _preference = getSharedPreferences("epaystubukpreference", MODE_PRIVATE);
        SharedPreferences.Editor editor = _preference.edit();

        editor.putString( in.test.rachana.Global.Constants.kPrefKeyEmpNo, etEmpNo.getText().toString());
        editor.putString( in.test.rachana.Global.Constants.kPrefKeyEmpName, etEmpName.getText().toString());
        editor.putString( in.test.rachana.Global.Constants.kPrefKeyProcessDate, etProcessDate.getText().toString());
        editor.putString( in.test.rachana.Global.Constants.kPrefKeyPaymentType, paymentType.toString());
        editor.putString( in.test.rachana.Global.Constants.kPrefKeyEmpAddr1, etEmpAddr1.getText().toString());
        editor.putString( in.test.rachana.Global.Constants.kPrefKeyEmpAddr2, etEmpAddr2.getText().toString());
        editor.putString( in.test.rachana.Global.Constants.kPrefKeyEmpAddr3, etEmpAddr3.getText().toString());
        editor.putString( in.test.rachana.Global.Constants.kPrefKeyCompanyName, etCompanyName.getText().toString());
        editor.putString( in.test.rachana.Global.Constants.kPrefKeyCompanyAddr1, etCompanyAddr1.getText().toString());
        editor.putString( in.test.rachana.Global.Constants.kPrefKeyCompanyAddr2, etCompanyAddr2.getText().toString());
        editor.putString( in.test.rachana.Global.Constants.kPrefKeyCompanyAddr3, etCompanyAddr3.getText().toString());
        editor.putString( in.test.rachana.Global.Constants.kPrefKeyCompanyTaxCode, etTaxCode.getText().toString());
        editor.putString( in.test.rachana.Global.Constants.kPrefKeyCompanyNINO, etNINo.getText().toString());
        editor.putString( in.test.rachana.Global.Constants.kPrefKeyIncomeTax, etIncomeTax.getText().toString());
        editor.putString( in.test.rachana.Global.Constants.kPrefKeyNI, etNI.getText().toString());
        editor.putString( in.test.rachana.Global.Constants.kPrefKeyTotalDeduction, etTotalDeductions.getText().toString());
        editor.putString( in.test.rachana.Global.Constants.kPrefKeyEmpNIC, etEmployerNIC.getText().toString());
        editor.putString( in.test.rachana.Global.Constants.kPrefKeyNITable, etNITable.getText().toString());
        editor.putString( in.test.rachana.Global.Constants.kPrefKeyDepart, etDepart.getText().toString());
        editor.putString( in.test.rachana.Global.Constants.kPrefKeyNetPay, etNetPay.getText().toString());
        editor.apply();
    }

    private void loadValues() {
        _preference = getSharedPreferences("epaystubukpreference", MODE_PRIVATE);
        etEmpNo.setText(_preference.getString( in.test.rachana.Global.Constants.kPrefKeyEmpNo, ""));
        etEmpName.setText(_preference.getString( in.test.rachana.Global.Constants.kPrefKeyEmpName, ""));
        etProcessDate.setText(_preference.getString( in.test.rachana.Global.Constants.kPrefKeyProcessDate, ""));
        paymentType = (_preference.getString( in.test.rachana.Global.Constants.kPrefKeyPaymentType, arrPaymentTypes[0].toString()));

        int activeIndex = 0;
        for (int i = 0; i < arrPaymentTypes.length; i++) {
            if (arrPaymentTypes[i] == paymentType) {
                activeIndex = i;
                break;
            }
        }

        spPaymentType.setSelection(activeIndex);

        etEmpAddr1.setText(_preference.getString(in.test.rachana.Global.Constants.kPrefKeyEmpAddr1, ""));
        etEmpAddr2.setText(_preference.getString(in.test.rachana.Global.Constants.kPrefKeyEmpAddr2, ""));
        etEmpAddr3.setText(_preference.getString(in.test.rachana.Global.Constants.kPrefKeyEmpAddr3, ""));
        etCompanyName.setText(_preference.getString(in.test.rachana.Global.Constants.kPrefKeyCompanyName, ""));
        etCompanyAddr1.setText(_preference.getString(in.test.rachana.Global.Constants.kPrefKeyCompanyAddr1, ""));
        etCompanyAddr2.setText(_preference.getString(in.test.rachana.Global.Constants.kPrefKeyCompanyAddr2, ""));
        etCompanyAddr3.setText(_preference.getString(in.test.rachana.Global.Constants.kPrefKeyCompanyAddr3, ""));
        etTaxCode.setText(_preference.getString(in.test.rachana.Global.Constants.kPrefKeyCompanyTaxCode, ""));
        etNINo.setText(_preference.getString(in.test.rachana.Global.Constants.kPrefKeyCompanyNINO, ""));
        etIncomeTax.setText(_preference.getString(in.test.rachana.Global.Constants.kPrefKeyIncomeTax, ""));
        etNI.setText(_preference.getString(in.test.rachana.Global.Constants.kPrefKeyNI, ""));
        etTotalDeductions.setText(_preference.getString(in.test.rachana.Global.Constants.kPrefKeyTotalDeduction, ""));
        etEmployerNIC.setText(_preference.getString(in.test.rachana.Global.Constants.kPrefKeyEmpNIC, ""));
        etNITable.setText(_preference.getString(in.test.rachana.Global.Constants.kPrefKeyNITable, ""));
        etDepart.setText(_preference.getString(in.test.rachana.Global.Constants.kPrefKeyDepart, ""));
        etNetPay.setText(_preference.getString(in.test.rachana.Global.Constants.kPrefKeyNetPay, ""));
    }

    private void initRenderView() {
        tvEmpNo = (TextView) findViewById(R.id.textViewEmployeeNO);
        tvEmpName = (TextView) findViewById(R.id.textViewEmployeeName);
        tvProcessDate = (TextView) findViewById(R.id.textViewProcessDate);
        tvNINo = (TextView) findViewById(R.id.textViewNiNo);
        tvUnits = (TextView) findViewById(R.id.textViewUnit);
        tvRate = (TextView) findViewById(R.id.textViewRate);
        tvAmount = (TextView) findViewById(R.id.textViewBasicPay);
        tvTotalPayment = (TextView) findViewById(R.id.textViewTotalPayments);
        tvIncomeTax = (TextView) findViewById(R.id.textViewIncomeTax);
        tvNI = (TextView) findViewById(R.id.textViewNI);
        tvTotalDeduction = (TextView) findViewById(R.id.textViewTotalDeduction);
        tvEmpName1 = (TextView) findViewById(R.id.textViewEmployeeName1);
        tvEmpAddress = (TextView) findViewById(R.id.textViewEmployeeAddr);
        tvTotalPayment1 = (TextView) findViewById(R.id.textViewTotalPayments1);
        tvTotalDeduction1 = (TextView) findViewById(R.id.textViewTotalDeduction1);
        tvTexableGrossPay = (TextView) findViewById(R.id.textViewTaxableGrossPay);
        tvIncomeTax1 = (TextView) findViewById(R.id.textViewIncomeTax1);
        tvEmployeeNIC = (TextView) findViewById(R.id.textViewEmployeeNIC);
        tvEmployerNIC = (TextView) findViewById(R.id.textViewEmployerNIC);
        tvCompanyName = (TextView) findViewById(R.id.textViewCompanyName);
        tvTaxCode = (TextView) findViewById(R.id.textViewTaxCode);
        tvNITable = (TextView) findViewById(R.id.textViewNITable);
        tvDept = (TextView) findViewById(R.id.textViewDept);
        tvTaxPeroid = (TextView) findViewById(R.id.textViewTaxPeriod);
        tvPaymentMethod = (TextView) findViewById(R.id.textViewPaymentMethod);
        tvNetPay = (TextView) findViewById(R.id.textViewNetPay);
    }

    private void initInputViews() {
        etEmpNo = (EditText)findViewById(R.id.etEmpNO);
        etEmpName = (EditText)findViewById(R.id.etEmpName);
        etProcessDate = (EditText)findViewById(R.id.etProcessDate);
        etEmpAddr1 = (EditText)findViewById(R.id.etEmpAddr1);
        etEmpAddr2 = (EditText)findViewById(R.id.etEmpAddr2);
        etEmpAddr3 = (EditText)findViewById(R.id.etEmpAddr3);
        etCompanyName = (EditText)findViewById(R.id.etCompanyName);
        etCompanyAddr1 = (EditText)findViewById(R.id.etCompanyAddr1);
        etCompanyAddr2 = (EditText)findViewById(R.id.etCompanyAddr2);
        etCompanyAddr3 = (EditText)findViewById(R.id.etCompanyAddr3);
        etTaxCode = (EditText)findViewById(R.id.etTaxCode);
        etNINo = (EditText)findViewById(R.id.etNationalInsuranceNo);
        etIncomeTax = (EditText)findViewById(R.id.etIncomeTax);
        etNI = (EditText)findViewById(R.id.etNationalInsurance);
        etTotalDeductions = (EditText)findViewById(R.id.etTotalDeductions);
        etEmployerNIC = (EditText)findViewById(R.id.etEmployerNIC);
        etNITable = (EditText)findViewById(R.id.etNITable);
        etDepart = (EditText)findViewById(R.id.etDepart);
        etNetPay = (EditText)findViewById(R.id.etNetPay);
        etDescription = (EditText)findViewById(R.id.etDescription);

        TextWatcher rateWacher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                calcTotalAmount();
            }
        };

        TextWatcher hourWacher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                calcTotalAmount();
            }
        };

        etRate = (EditText)findViewById(R.id.etRate);
        etHour = (EditText)findViewById(R.id.etHour);

        etRate.addTextChangedListener(new in.test.rachana.UtilsApp.DecimalFilter(etRate));
        etHour.addTextChangedListener(new in.test.rachana.UtilsApp.DecimalFilter(etHour));

        etRate.addTextChangedListener(rateWacher);
        etHour.addTextChangedListener(hourWacher);

        etAmount = (EditText)findViewById(R.id.etAmount);
        spPaymentType = (Spinner)findViewById(R.id.spPaymentTypes);

        ArrayAdapter<String> spinnerStateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrPaymentTypes);
        spinnerStateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPaymentType.setAdapter(spinnerStateAdapter);

        spPaymentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                paymentType = arrPaymentTypes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.btnProcessDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateActivity.this.showDialog(1);
            }
        });

        etNetPay.addTextChangedListener(new in.test.rachana.UtilsApp.DecimalFilter(etNetPay));
        etEmployerNIC.addTextChangedListener(new in.test.rachana.UtilsApp.DecimalFilter(etEmployerNIC));

        etAmount.addTextChangedListener(new in.test.rachana.UtilsApp.DecimalFilter(etAmount));
        etTotalDeductions.addTextChangedListener(new in.test.rachana.UtilsApp.DecimalFilter(etTotalDeductions));
        etNI.addTextChangedListener(new in.test.rachana.UtilsApp.DecimalFilter(etNI));
        etIncomeTax.addTextChangedListener(new in.test.rachana.UtilsApp.DecimalFilter(etIncomeTax));
    }

    private void calcTotalAmount() {
        String hourStr = etHour.getText().toString();
        String rateStr = etRate.getText().toString();
        float hr = 0.0f;
        float rate = 0.0f;

        try {
            hr = Float.parseFloat(hourStr);
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            rate = Float.parseFloat(rateStr);
        }catch (Exception e){
            e.printStackTrace();
        }

        etAmount.setText(String.format("%.2f", hr * rate));
    }

    private void setValuesToRenderView() {
        tvEmpNo.setText(etEmpNo.getText().toString());
        tvProcessDate.setText(etProcessDate.getText().toString());
        tvNINo.setText(etNINo.getText().toString());
        tvUnits.setText(in.test.rachana.UtilsApp.GlobalUtils.decimalFormatedString(etHour.getText().toString(), false));
        tvRate.setText(in.test.rachana.UtilsApp.GlobalUtils.decimalFormatedString(etRate.getText().toString(), false));
        tvAmount.setText(in.test.rachana.UtilsApp.GlobalUtils.decimalFormatedString(etAmount.getText().toString(), true));
        tvTotalPayment.setText(in.test.rachana.UtilsApp.GlobalUtils.decimalFormatedString(etAmount.getText().toString(), true));
        tvIncomeTax.setText(in.test.rachana.UtilsApp.GlobalUtils.decimalFormatedString(etIncomeTax.getText().toString(), true));
        tvNI.setText(in.test.rachana.UtilsApp.GlobalUtils.decimalFormatedString(etNI.getText().toString(), true));
        tvTotalDeduction.setText(in.test.rachana.UtilsApp.GlobalUtils.decimalFormatedString(etTotalDeductions.getText().toString(), true));
        tvEmpName.setText(etEmpName.getText().toString());
        tvEmpName1.setText(etEmpName.getText().toString());
        tvEmpAddress.setText(etEmpAddr1.getText().toString() + "\n" + etEmpAddr2.getText().toString() + "\n" + etEmpAddr3.getText().toString());
        tvTotalPayment1.setText(in.test.rachana.UtilsApp.GlobalUtils.decimalFormatedString(etAmount.getText().toString(), true));
        tvTotalDeduction1.setText(in.test.rachana.UtilsApp.GlobalUtils.decimalFormatedString(etTotalDeductions.getText().toString(), true));
        tvTexableGrossPay.setText(in.test.rachana.UtilsApp.GlobalUtils.decimalFormatedString(etAmount.getText().toString(), true));
        tvIncomeTax1.setText(in.test.rachana.UtilsApp.GlobalUtils.decimalFormatedString(etIncomeTax.getText().toString(), true));
        tvEmployerNIC.setText(in.test.rachana.UtilsApp.GlobalUtils.decimalFormatedString(etEmployerNIC.getText().toString(), true));
        tvEmployeeNIC.setText(in.test.rachana.UtilsApp.GlobalUtils.decimalFormatedString(etNI.getText().toString(), true));
        tvCompanyName.setText(etCompanyName.getText().toString());
        String companyAddr = etCompanyAddr1.getText().toString() + " " + etCompanyAddr2.getText().toString() + " " + etCompanyAddr3.getText().toString();
        if (companyAddr.trim().length() != 0) {
            tvCompanyName.setText(etCompanyName.getText().toString() + ",  " + companyAddr);
        }
        tvTaxCode.setText(etTaxCode.getText().toString());
        tvNITable.setText(etNITable.getText().toString());
        tvDept.setText(etDepart.getText().toString());

        String processDate = etProcessDate.getText().toString();
        if (!processDate.isEmpty()) {
            String[] splitAry = processDate.split("-");
            if (splitAry.length == 3) {
                tvTaxPeroid.setText(splitAry[1] + "-" + splitAry[2]);
            }
        }
        else {
            tvTaxPeroid.setText("");
        }
        tvPaymentMethod.setText(paymentType);
        tvNetPay.setText(in.test.rachana.UtilsApp.GlobalUtils.decimalFormatedString(etNetPay.getText().toString(), true));
    }

    private void hideKeyboard() {
        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
        }
    }

    private void onBtnGenerate() {
        saveCurrentValues();
        setValuesToRenderView();
        Bitmap bm = layoutToImage();
        Log.e("Tag", "after layoutToImage");
        FileOutputStream out = null;

        String imgDirPath = Environment.getExternalStorageDirectory() + File.separator + "UKPaystub/pdf/";
        String imgPath = imgDirPath + in.test.rachana.UtilsApp.GlobalUtils.timeStampString() + ".jpg";
        if (bm != null) {
            Log.e("Tag", "Bitmap is not null");
            File dirFile = new File(imgDirPath);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }

            try {
                out = new FileOutputStream(imgPath);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                Intent intent = new Intent(this, in.test.rachana.activities.PreviewActivity.class);
                intent.putExtra("image_path", imgPath);
                startActivity(intent);
            }catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    requestPermission();
                }
                break;
        }
    }

    public Bitmap layoutToImage() {
        // get view group using reference
        // convert view group to bitmap
        pdfLayout.setDrawingCacheEnabled(true);
        pdfLayout.buildDrawingCache();
        Bitmap bm = pdfLayout.getDrawingCache();

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return bm;
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }

        Log.e("Pause","Pause");
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
        Log.e("Resume","Resume");
        stop = false;



        if (purchase.get(in.test.rachana.UtilsApp.SessionManager.sproductId) != null && purchase.get(in.test.rachana.UtilsApp.SessionManager.sproductId).equalsIgnoreCase("Life_timepaymentuk")) {
            mAdView.setVisibility(View.GONE);

        }else {

            if (purchase.get(in.test.rachana.UtilsApp.SessionManager.startDate) != null && purchase.get(in.test.rachana.UtilsApp.SessionManager.endDate) != null) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    today = sdf.parse(sTodayDate);
                    strDate = sdf.parse(purchase.get(in.test.rachana.UtilsApp.SessionManager.startDate));

                    enDate = sdf.parse(purchase.get(in.test.rachana.UtilsApp.SessionManager.endDate));

                    if (today.compareTo(strDate) >= 0 && today.compareTo(enDate) < 1) {
                        mAdView.setVisibility(View.GONE);
                    } else {
                        session.clearData();
                        Log.e("WrongDate","WrongDate");

                        mAdView.setVisibility(View.VISIBLE);
                        mInterstitialAd = new InterstitialAd(CreateActivity.this);
                        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

                        // Load ads into Interstitial Ads

                        mAdView.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {
                            }

                            @Override
                            public void onAdClosed() {
                                Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdFailedToLoad(int errorCode) {
                                Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdLeftApplication() {
                                Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdOpened() {
                                super.onAdOpened();
                            }
                        });


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

                        mAdView.loadAd(adRequest);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("Startdate", "null");
                mAdView.setVisibility(View.VISIBLE);
                mInterstitialAd = new InterstitialAd(CreateActivity.this);
                mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));


                // Load ads into Interstitial Ads

                mAdView.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                    }

                    @Override
                    public void onAdClosed() {
                        Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdLeftApplication() {
                        Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdOpened() {
                        super.onAdOpened();
                    }
                });
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

                mAdView.loadAd(adRequest);

            }
        }

        if(inventory.hasPurchase(ITEM_SKU_1)){
            Log.e("HasPurchased","HasPurchasd");
        }else{
            Log.e("Not","Not");
        }

    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
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
