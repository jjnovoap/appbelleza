package com.example.appbella.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;

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
        holder.ckb_addon.setText(new StringBuilder(mAddonList.get(position).getName()).append(" (").append(mContext.getString(R.string.money_sign))
                .append(mAddonList.get(position).getExtraPrice())
                .append(")"));
        holder.ckb_addon.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Common.addonList.add(mAddonList.get(position));
                EventBus.getDefault().post(new AddOnEventChange(true, mAddonList.get(position)));
            } else {
                Common.addonList.remove(mAddonList.get(position));
                EventBus.getDefault().post(new AddOnEventChange(false, mAddonList.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAddonList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ckb_addon)
        CheckBox ckb_addon;
        Unbinder mUnbinder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mUnbinder = ButterKnife.bind(this, itemView);
        }
    }
}
