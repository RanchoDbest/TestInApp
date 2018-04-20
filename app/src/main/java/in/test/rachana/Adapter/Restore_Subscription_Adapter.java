package in.test.rachana.Adapter;

import android.content.Context;
import android.content.ServiceConnection;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import in.test.rachana.R;
import in.test.rachana.UtilsApp.Data;
import com.android.vending.billing.IInAppBillingService;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class Restore_Subscription_Adapter extends RecyclerView.Adapter<Restore_Subscription_Adapter.View_Holder> {

    List<Data> list = Collections.emptyList();
    Context RestoreActivity;
    IInAppBillingService mService;
    ServiceConnection mServiceConn;
    AdapterInterface buttonListener;


    public interface AdapterInterface {
        public void buttonPressed(String sProductid);
    }

    public Restore_Subscription_Adapter(List<Data> list, Context restoreActivity, AdapterInterface buttonListener) {
        this.list = list;
        this.RestoreActivity = restoreActivity;
        this.buttonListener = buttonListener;
    }

    
    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_restore, parent, false);
        View_Holder holder = new View_Holder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final View_Holder holder, final int position) {

        holder.tv_product_name.setText(list.get(position).getSproductId());

        long unixSeconds = Long.parseLong(list.get(position).getSpurchaseTime());

        Date date = new Date(unixSeconds*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        sdf.setTimeZone(java.util.TimeZone.getDefault());
        String formattedDate = sdf.format(date);

        holder.tv_purchaseTime.setText(""+date);

        holder.btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonListener.buttonPressed(list.get(position).getSproductId());
            }
        });

    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



    public class View_Holder extends RecyclerView.ViewHolder {

        TextView tv_product_name,tv_purchaseTime;
        Button btnRestore;

        public View_Holder(View itemView) {
            super(itemView);
            tv_product_name = (TextView) itemView.findViewById(R.id.tv_product_name);
            tv_purchaseTime = (TextView) itemView.findViewById(R.id.tv_purchaseTime);
            btnRestore = (Button) itemView.findViewById(R.id.btn_restore_active);
        }
    }

    @Override
    public void onViewDetachedFromWindow(View_Holder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }
    
    
}



