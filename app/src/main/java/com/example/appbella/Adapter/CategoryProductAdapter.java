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
import com.example.appbella.Model.ProductCategory;
import com.example.appbella.Model.ServiceCategory;
import com.example.appbella.Model.EventBust.ProductSubcategoryEvent;

import com.example.appbella.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CategoryProductAdapter extends RecyclerView.Adapter<CategoryProductAdapter.MyViewHolder> {

    private Context mContext;
    private List<ProductCategory> mProductCategoryList;
    private int row_index = 0;


    public CategoryProductAdapter(Context context, List<ProductCategory> productCategoryList) {
        mContext = context;
        mProductCategoryList = productCategoryList;
    }

    @NonNull
    @Override
    public CategoryProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_category, parent, false);
        return new CategoryProductAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryProductAdapter.MyViewHolder holder, int position) {
        holder.txt_category.setText(mProductCategoryList.get(position).getName());

        //EventBus.getDefault().postSticky(new SubcategoryEvent(true, mCategoryList.get(position)));


        // Remember implement it if you don't want to get  crash
        /*holder.setIOnRecyclerViewClickListener((view, pos) -> {
            row_index=pos;
            notifyDataSetChanged();
            EventBus.getDefault().postSticky(new SubcategoryEvent(true, mCategoryList.get(row_index)));
            //mContext.startActivity(new Intent(mContext, SubcategoryActivity.class));
        });*/

        holder.card.setOnClickListener(view -> {
            row_index = position;
            EventBus.getDefault().postSticky(new ProductSubcategoryEvent(true, mProductCategoryList.get(position)));
            notifyDataSetChanged();
        });

        if(row_index==position){
            holder.img_view.setBackground(mContext.getResources().getDrawable(R.drawable.fondo));
            holder.txt_category.setTextColor(Color.parseColor("#ffffff"));
            EventBus.getDefault().postSticky(new ProductSubcategoryEvent(true, mProductCategoryList.get(position)));
        } else {
            holder.img_view.setBackground(mContext.getResources().getDrawable(R.drawable.outline_background));
            holder.txt_category.setTextColor(Color.parseColor("#1F4E5B"));
        }

    }

    @Override
    public int getItemCount() {
        return mProductCategoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.txt_category)
        TextView txt_category;
        @BindView(R.id.img_view)
        ImageView img_view;

        @BindView(R.id.card)
        CardView card;

        //IOnRecyclerViewClickListener mIOnRecyclerViewClickListener;

        Unbinder mUnbinder;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mUnbinder = ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //mIOnRecyclerViewClickListener.onClick(v, getAdapterPosition());
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (mProductCategoryList.size() == 1) {
            return Common.DEFAULT_COLUMN_COUNT;
        }
        else {
            if (mProductCategoryList.size() % 2 == 0) {
                return Common.DEFAULT_COLUMN_COUNT;
            }
            else {
                return (position > 1 && position == mProductCategoryList.size()-1)
                        ? Common.FULL_WIDTH_COLUMN
                        : Common.DEFAULT_COLUMN_COUNT;
            }
        }
    }
}
