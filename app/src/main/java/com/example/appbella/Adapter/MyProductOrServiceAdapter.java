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
import com.example.appbella.MenuActivity;
import com.example.appbella.Model.EventBust.MenuItemEvent;
import com.example.appbella.Model.CategoryProductOrServices;
import com.example.appbella.R;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyProductOrServiceAdapter extends RecyclerView.Adapter<MyProductOrServiceAdapter.MyViewHolder> {

    private Context mContext;
    private List<CategoryProductOrServices> mCategoryProductOrServicesList;

    public MyProductOrServiceAdapter(Context context, List<CategoryProductOrServices> categoryProductOrServicesList) {
        mContext = context;
        mCategoryProductOrServicesList = categoryProductOrServicesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_catalogo, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(mCategoryProductOrServicesList.get(position).getImage()).into(holder.img_restaurant);
        holder.txt_restaurant_name.setText(mCategoryProductOrServicesList.get(position).getName());

        // Remember implement it if you don't want to get  crash
        holder.setIOnRecyclerViewClickListener((view, i) -> {
            Common.currentCategoryProductOrServices = mCategoryProductOrServicesList.get(i);
            // Here use postSticky, that mean this event will be listen from other activity
            // It will different with just 'post'
            EventBus.getDefault().postSticky(new MenuItemEvent(true, mCategoryProductOrServicesList.get(i)));
            mContext.startActivity(new Intent(mContext, MenuActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return mCategoryProductOrServicesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.txt_restaurant_name)
        TextView txt_restaurant_name;
        @BindView(R.id.img_restaurant)
        ImageView img_restaurant;

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
}
