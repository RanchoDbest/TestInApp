package in.test.rachana.UtilsApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;


public class SessionManager {

	SharedPreferences pref;
	Editor editor;
	Context _context;
	int PRIVATE_MODE = 0;

	private static final String PREF_NAME = "MYBYKPref";

	public static final String sorderId ="OrderID";
	public static final String sproductId = "ProductId";
	public static final String spackageName = "PackageName";
	public static final String spurchaseTime = "PurchaseTime";
	public static final String spurchaseState = "PurchaseState";
	public static final String spurchaseToken = "PurchaseToken";
	public static final String sautoRenewing = "AutoRenew";
	public static final String startDate = "StartDate";
	public static final String endDate = "EndDate";



	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
		editor.apply();
	}

	public void startEndDate(String start, String end){
		editor.putString(startDate, start);
		editor.putString(endDate, end);
		editor.commit();
	}

	public void createUserPurchseData(String productId, String orderId, String packageName, String purchaseTime, String purchaseState, String autoRenewing, String purchaseToken){

		editor.putString(sproductId, productId);
		editor.putString(sorderId,orderId);
		editor.putString(spackageName, packageName);
		editor.putString(spurchaseTime,purchaseTime);
		editor.putString(spurchaseState,purchaseState);
		editor.putString(sautoRenewing,autoRenewing);
		editor.putString(spurchaseToken,purchaseToken);
		editor.commit();
	}

	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		user.put(sproductId, pref.getString(sproductId, null));
		user.put(sorderId, pref.getString(sorderId, null));
		user.put(spackageName, pref.getString(spackageName, null));
		user.put(spurchaseTime, pref.getString(spurchaseTime, null));
		user.put(spurchaseState, pref.getString(spurchaseState, null));
		user.put(sautoRenewing, pref.getString(sautoRenewing, null));
		user.put(spurchaseToken, pref.getString(spurchaseToken, null));
		user.put(startDate, pref.getString(startDate, null));
		user.put(endDate, pref.getString(endDate, null));

		return user;
	}

	public void clearData(){
		editor.clear();
		editor.commit();
	}

}
