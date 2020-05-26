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
import com.example.appbella.Interface.IOnRecyclerViewClickListener;
import com.example.appbella.Model.Subcategory;
import com.example.appbella.Model.EventBust.FoodListEvent;
import com.example.appbella.ProductAndServiceList;
import com.example.appbella.R;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SubcategoryProductAdapter extends RecyclerView.Adapter<SubcategoryProductAdapter.MyViewHolder> {

    private Context mContext;
    private List<Subcategory> mSubcategoryList;

    public SubcategoryProductAdapter(Context context, List<Subcategory> subcategoryList) {
        mContext = context;
        mSubcategoryList = subcategoryList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_product_subcategory, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(mSubcategoryList.get(position).getImage()).into(holder.img_subcategory);
        holder.txt_subcategory.setText(mSubcategoryList.get(position).getName());

        holder.setIOnRecyclerViewClickListener((view, i) -> {
            // Send sticky post event to FoodListActivity
            EventBus.getDefault().postSticky(new FoodListEvent(true, mSubcategoryList.get(position)));
            mContext.startActivity(new Intent(mContext, ProductAndServiceList.class));
        });
    }

    @Override
    public int getItemCount() {
        return mSubcategoryList.size();
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
}
