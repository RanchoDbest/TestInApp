package in.test.rachana.UtilsApp;

/**
 * Created by CloudStream on 4/7/2018.
 */

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.ParseException;

public class DecimalFilter implements TextWatcher {

//    int count= -1 ;
//    EditText et;
//    Activity activity;
//
//    public DecimalFilter(EditText edittext, Activity activity) {
//        et = edittext;
//        this.activity = activity;
//    }
//
//    public void afterTextChanged(Editable s) {
//
//        if (s.length() > 0) {
//            String str = et.getText().toString();
//            et.setOnKeyListener(new OnKeyListener() {
//
//                public boolean onKey(View v, int keyCode, KeyEvent event) {
//                    if (keyCode == KeyEvent.KEYCODE_DEL) {
//                        count--;
//                        InputFilter[] fArray = new InputFilter[1];
//                        fArray[0] = new InputFilter.LengthFilter(100);//Re sets the maxLength of edittext to 100.
//                        et.setFilters(fArray);
//                    }
//                    if (count > 2) {
//                        //Toast.makeText(activity, "Sorry! You cant enter more than two digits after decimal point!", Toast.LENGTH_SHORT).show();
//                    }
//                    return false;
//                }
//            });
//
//            char t = str.charAt(s.length() - 1);
//
//            if (t == '.') {
//                count = 0;
//            }
//
//            if (count >= 0) {
//                if (count == 2) {
//                    InputFilter[] fArray = new InputFilter[1];
//                    fArray[0] = new InputFilter.LengthFilter(s.length());
//                    et.setFilters(fArray); // sets edittext's maxLength to number of digits now entered.
//
//                }
//                count++;
//            }
//        }
//
//    }
//
//    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//// TODO Auto-generated method stub
//    }
//
//    public void onTextChanged(CharSequence s, int start, int before, int count) {
//// TODO Auto-generated method stub
//    }
private DecimalFormat df;
    private DecimalFormat dfnd;
    private boolean hasFractionalPart;
    int trailingZeroCount = 0;
    private EditText et;
    int a=0;
    public DecimalFilter(EditText et)
    {
        df = new DecimalFormat("#,###.##");
        df.setDecimalSeparatorAlwaysShown(true);
        dfnd = new DecimalFormat("#,###");
        this.et = et;
        hasFractionalPart = false;
    }

    @SuppressWarnings("unused")
    private static final String TAG = "NumberTextWatcher";

    @Override
    public void afterTextChanged(Editable s)
    {
        if(s.length()>0){
            et.removeTextChangedListener(this);

            try {
                int inilen, endlen;
                inilen = et.getText().length();

                String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
                Number n = df.parse(v);
                int cp = et.getSelectionStart();

                if (hasFractionalPart) {
                    StringBuilder trailingZeros = new StringBuilder();
                    while (trailingZeroCount-- > 0)
                        trailingZeros.append('0');

                    String txt = df.format(n);
                    Log.v("TXT", txt + trailingZeros.toString());
                    et.setText(txt + trailingZeros.toString());
                } else {
                    String txt = dfnd.format(n);
                    et.setText(txt);
                }

                endlen = et.getText().length();
                int sel = (cp + (endlen - inilen));
                if (sel > 0 && sel <= et.getText().length()) {
                    et.setSelection(sel);
                } else {
                    // place cursor at the end?
                    et.setSelection(et.getText().length() - 1);
                }
            } catch (NumberFormatException nfe) {
                // do nothing?
            } catch (ParseException e) {
                // do nothing?
                e.printStackTrace();
            }

            et.addTextChangedListener(this);
        }}

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        int index = s.toString().indexOf(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()));
        trailingZeroCount = 0;
        Log.d("index",""+index);
        a=index;
        if (index > -1)
        {		Log.d("s.length()",""+s.length());
            for (index++; index < s.length(); index++) {
                Log.d("scharindex",""+index);

                if (s.charAt(index) == '0' && index-a<=2)
                    trailingZeroCount++;
                else {
                    trailingZeroCount = 0;
                }
            }

            hasFractionalPart = true;
        } else {
            hasFractionalPart = false;
        }
    }
}