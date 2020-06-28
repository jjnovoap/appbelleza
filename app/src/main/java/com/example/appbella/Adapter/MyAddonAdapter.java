package com.example.appbella.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbella.Common.Common;
import com.example.appbella.Model.Addon;
import com.example.appbella.Model.EventBust.AddOnEventChange;
import com.example.appbella.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyAddonAdapter extends RecyclerView.Adapter<MyAddonAdapter.MyViewHolder> {

    private Context mContext;
    private List<Addon> mAddonList;

    @SuppressLint("StaticFieldLeak")
    private static CheckBox lastChecked = null;
    private static int lastCheckedPos = 0;

    public MyAddonAdapter(Context context, List<Addon> addonList) {
        mContext = context;
        mAddonList = addonList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.layout_addon, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.ckb_addon.setSelected(mAddonList.get(position).isSelected());
        holder.ckb_addon.setButtonDrawable(null);
        holder.ckb_addon.setTag(position);
        holder.txt_title.setText(mAddonList.get(position).getName());
        holder.txt_price.setText(new StringBuilder(mContext.getString(R.string.money_sign))
                .append(" ")
                .append(mAddonList.get(position).getExtraPrice()));

        //for default check in first item
        if (position == 0 && mAddonList.get(0).isSelected() && holder.ckb_addon.isChecked()) {
            lastChecked = holder.ckb_addon;
            lastCheckedPos = 0;
        }

        holder.ckb_addon.setOnClickListener(v -> {
            CheckBox cb = (CheckBox) v;
            int clickedPos = (Integer) cb.getTag();

            if (cb.isChecked()) {
                if (lastChecked != null) {
                    lastChecked.setChecked(false);
                    mAddonList.get(lastCheckedPos).setSelected(false);
                }

                lastChecked = cb;
                lastCheckedPos = clickedPos;
            } else
                lastChecked = null;

            mAddonList.get(clickedPos).setSelected(cb.isSelected());
        });



        holder.ckb_addon.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Common.addonList.add(mAddonList.get(position));
                holder.txt_price.setTextColor(mContext.getResources().getColor(R.color.white));
                holder.txt_title.setTextColor(mContext.getResources().getColor(R.color.white));
                EventBus.getDefault().post(new AddOnEventChange(true, mAddonList.get(position)));
            } else {
                Common.addonList.remove(mAddonList.get(position));
                holder.txt_price.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                holder.txt_title.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                EventBus.getDefault().post(new AddOnEventChange(false, mAddonList.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAddonList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ckb_addon)
        CheckBox ckb_addon;
        @BindView(R.id.txt_price)
        TextView txt_price;
        @BindView(R.id.txt_title)
        TextView txt_title;
        Unbinder mUnbinder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mUnbinder = ButterKnife.bind(this, itemView);
        }
    }
}
