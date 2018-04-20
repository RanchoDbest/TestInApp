package in.test.rachana.UtilsApp;

import android.content.Context;
import android.util.DisplayMetrics;

import java.text.DecimalFormat;

/**
 * Created by Andrey on 4/6/18.
 */

public class GlobalUtils {
    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static int getWidthOfScreen(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    public static int getHeightOfScreen(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }

    public static String timeStampString() {
        Long tsLong = System.currentTimeMillis();
        String ts = tsLong.toString();

        return ts;
    }

    public static float floatValueFromString(String str) {
        String tmpStr = str.replace(",", "");
        float value = 0.0f;
        try {
            value = Float.parseFloat(tmpStr);
        }catch (Exception e){
            e.printStackTrace();
        }

        return value;
    }

    public static String decimalFormatedString(String str, Boolean currencySymbol) {
        String prefix = "";
        if (currencySymbol) {
            prefix = "£";
        }

        float fValue = floatValueFromString(str);
        try {
            DecimalFormat df = new DecimalFormat("#,###.##");
            String separedStr = df.format(fValue);
            if (!separedStr.contains(".")) {
                return prefix + separedStr + ".00";
            }
            else {
                if (separedStr.indexOf(".") == (separedStr.length() - 2)) {
                    return prefix + separedStr + "0";
                }
            }
            return prefix + separedStr;

        } catch (Exception e) {
            e.printStackTrace();
        }


        return prefix + "0.00";
    }
}
