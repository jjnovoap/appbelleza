package com.example.appbella.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbella.Common.Common;
import com.example.appbella.Interface.IOnRecyclerViewClickListener;
import com.example.appbella.Model.ServiceSubcategory;
import com.example.appbella.Model.EventBust.ServiceListEvent;
import com.example.appbella.ServicesList;
import com.example.appbella.R;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SubcategoryServiceAdapter extends RecyclerView.Adapter<SubcategoryServiceAdapter.MyViewHolder> {

    private Context mContext;
    private List<ServiceSubcategory> mServiceSubcategoryList;

    public SubcategoryServiceAdapter(Context context, List<ServiceSubcategory> serviceSubcategoryList) {
        mContext = context;
        mServiceSubcategoryList = serviceSubcategoryList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_service_subcategory, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(mServiceSubcategoryList.get(position).getImage()).into(holder.img_subcategory);
        holder.txt_subcategory.setText(mServiceSubcategoryList.get(position).getName());

        holder.setIOnRecyclerViewClickListener((view, i) -> {
            // Send sticky post event to FoodListActivity
            EventBus.getDefault().postSticky(new ServiceListEvent(true, mServiceSubcategoryList.get(position)));
            mContext.startActivity(new Intent(mContext, ServicesList.class));
        });
    }

    @Override
    public int getItemCount() {
        return mServiceSubcategoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.img_subcategory)
        ImageView img_subcategory;
        @BindView(R.id.txt_subcategory)
        TextView txt_subcategory;

        IOnRecyclerViewClickListener mIOnRecyclerViewClickListener;

        public void setIOnRecyclerViewClickListener(IOnRecyclerViewClickListener IOnRecyclerViewClickListener) {
            mIOnRecyclerViewClickListener = IOnRecyclerViewClickListener;
        }

        Unbinder mUnbinder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mUnbinder = ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mIOnRecyclerViewClickListener.onClick(v, getAdapterPosition());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mServiceSubcategoryList.size() == 1) {
            return Common.DEFAULT_COLUMN_COUNT;
        }
        else {
            if (mServiceSubcategoryList.size() % 2 == 0) {
                return Common.DEFAULT_COLUMN_COUNT;
            }
            else {
                return (position > 1 && position == mServiceSubcategoryList.size()-1)
                        ? Common.FULL_WIDTH_COLUMN
                        : Common.DEFAULT_COLUMN_COUNT;
            }
        }
    }
}
