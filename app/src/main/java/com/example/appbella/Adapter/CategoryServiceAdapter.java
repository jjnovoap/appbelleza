package com.example.appbella.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbella.Common.Common;
import com.example.appbella.Model.EventBust.ServiceSubcategoryEvent;
import com.example.appbella.Model.ServiceCategory;
import com.example.appbella.R;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CategoryServiceAdapter extends RecyclerView.Adapter<CategoryServiceAdapter.MyViewHolder> {

    private Context mContext;
    private List<ServiceCategory> mServiceCategoryList;
    private int row_index = 0;


    public CategoryServiceAdapter(Context context, List<ServiceCategory> serviceCategoryList) {
        mContext = context;
        mServiceCategoryList = serviceCategoryList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_category, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_category.setText(mServiceCategoryList.get(position).getName());

        holder.card.setOnClickListener(view -> {
            row_index = position;
            EventBus.getDefault().postSticky(new ServiceSubcategoryEvent(true, mServiceCategoryList.get(position)));
            notifyDataSetChanged();
        });

        if(row_index==position){
            holder.img_view.setBackground(mContext.getResources().getDrawable(R.drawable.fondo));
            holder.txt_category.setTextColor(Color.parseColor("#ffffff"));
            EventBus.getDefault().postSticky(new ServiceSubcategoryEvent(true, mServiceCategoryList.get(position)));
        } else {
            holder.img_view.setBackground(mContext.getResources().getDrawable(R.drawable.outline_background));
            holder.txt_category.setTextColor(Color.parseColor("#1F4E5B"));
        }

    }

    @Override
    public int getItemCount() {
        return mServiceCategoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.txt_category)
        TextView txt_category;
        @BindView(R.id.img_view)
        ImageView img_view;
        @BindView(R.id.card)
        CardView card;

        Unbinder mUnbinder;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mUnbinder = ButterKnife.bind(this, itemView);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (mServiceCategoryList.size() == 1) {
            return Common.DEFAULT_COLUMN_COUNT;
        }
        else {
            if (mServiceCategoryList.size() % 2 == 0) {
                return Common.DEFAULT_COLUMN_COUNT;
            }
            else {
                return (position > 1 && position == mServiceCategoryList.size()-1)
                        ? Common.FULL_WIDTH_COLUMN
                        : Common.DEFAULT_COLUMN_COUNT;
            }
        }
    }
}
