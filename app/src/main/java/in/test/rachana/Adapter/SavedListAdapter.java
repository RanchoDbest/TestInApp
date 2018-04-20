package in.test.rachana.Adapter;

import android.widget.BaseAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.test.rachana.R;

/**
 * Created by Andrey on 4/6/2018.
 */

public class SavedListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflter;

    public ArrayList<String> items = new ArrayList<>();

    public SavedListAdapter(Context applicationContext) {

        this.context = applicationContext;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public String getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {
        View row = view;
        SavedListAdapter.Holder holder;
        if (row == null)
        {
            holder = new Holder();
            row = inflter.inflate(R.layout.list_item_layout, viewGroup, false);

            holder.tvTitle = (TextView) row.findViewById(R.id.tvTitle);
            holder.itemImage = (ImageView) row.findViewById(R.id.ivThumbnail);

            row.setTag(holder);
        }else {
            holder = (Holder) row.getTag();
        }

        holder.tvTitle.setText(getItem(i));

        return row;
    }

    public class Holder {
        TextView tvTitle;
        ImageView itemImage;
    }
}
