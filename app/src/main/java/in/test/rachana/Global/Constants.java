package  in.test.rachana.Global;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Crane on 4/7/2018.
 */

public class Constants {
    public static final String kPrefKeyEmpNo = "kPrefKeyEmpNo";
    public static final String kPrefKeyEmpName = "kPrefKeyEmpName";
    public static final String kPrefKeyProcessDate = "kPrefKeyProcessDate";
    public static final String kPrefKeyPaymentType = "kPrefKeyPaymentType";
    public static final String kPrefKeyEmpAddr1 = "kPrefKeyEmpAddr1";
    public static final String kPrefKeyEmpAddr2 = "kPrefKeyEmpAddr2";
    public static final String kPrefKeyEmpAddr3 = "kPrefKeyEmpAddr3";
    public static final String kPrefKeyCompanyName = "kPrefKeyCompanyName";
    public static final String kPrefKeyCompanyAddr1 = "kPrefKeyCompanyAddr1";
    public static final String kPrefKeyCompanyAddr2 = "kPrefKeyCompanyAddr2";
    public static final String kPrefKeyCompanyAddr3 = "kPrefKeyCompanyAddr3";
    public static final String kPrefKeyCompanyTaxCode = "kPrefKeyCompanyTaxCode";
    public static final String kPrefKeyCompanyNINO = "kPrefKeyCompanyNINO";
    public static final String kPrefKeyIncomeTax = "kPrefKeyIncomeTax";
    public static final String kPrefKeyNI = "kPrefKeyNI";
    public static final String kPrefKeyTotalDeduction = "kPrefKeyTotalDeduction";
    public static final String kPrefKeyEmpNIC = "kPrefKeyEmpNIC";
    public static final String kPrefKeyNITable = "kPrefKeyNITable";
    public static final String kPrefKeyDepart = "kPrefKeyDepart";
    public static final String kPrefKeyNetPay = "kPrefKeyNetPay";



    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
