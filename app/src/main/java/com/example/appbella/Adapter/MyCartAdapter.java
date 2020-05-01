package com.example.appbella.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbella.Database.CartDataSource;
import com.example.appbella.Database.CartDatabase;
import com.example.appbella.Database.CartItem;
import com.example.appbella.Database.LocalCartDataSource;
import com.example.appbella.Interface.IOnImageViewAdapterClickListener;
import com.example.appbella.Model.EventBust.CalculatePriceEvent;
import com.example.appbella.R;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyViewHolder> {

    private Context mContext;
    private List<CartItem> mCartItemList;
    private CartDataSource mCartDataSource;

    public MyCartAdapter(Context context, List<CartItem> cartItemList) {
        mContext = context;
        mCartItemList = cartItemList;
        mCartDataSource = new LocalCartDataSource(CartDatabase.getInstance(context).cartDAO());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_cart, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(mCartItemList.get(position).getProductImage()).into(holder.img_food);
        holder.txt_food_name.setText(mCartItemList.get(position).getProductName());
        holder.txt_food_price.setText(String.valueOf(mCartItemList.get(position).getProductPrice()));
        holder.txt_quantity.setText(String.valueOf(mCartItemList.get(position).getProductQuantity()));
        Long finalResult = ((mCartItemList.get(position).getProductPrice() * mCartItemList.get(position).getProductQuantity()));
        holder.txt_price_new.setText(String.valueOf(finalResult));
        holder.txt_items_add.setText(new StringBuilder("Adiciones: ")
                .append(mCartItemList.get(position).getProductExtraPrice() / 1000));
        holder.txt_extra_price.setText(new StringBuilder("Precio Extra: $ ")
                .append(mCartItemList.get(position).getProductExtraPrice()));
        holder.img_delete_food.setVisibility(View.GONE);

        if (holder.txt_quantity.getText().equals("1")) {
            holder.img_decrease.setVisibility(View.GONE);
            holder.img_delete_food.setVisibility(View.VISIBLE);
        }

        // Event
        holder.setIOnImageViewAdapterClickListener((view, position1, isDecrease, isDelete) -> {
            // If not button delete food from Cart click
            if (!isDelete) {
                // If decrease quantity
                if (isDecrease) {
                    if (mCartItemList.get(position1).getProductQuantity() > 1) {
                        mCartItemList.get(position1).setProductQuantity(mCartItemList.get(position1).getProductQuantity() - 1);
                    }
                }
                // If increase quantity
                else {
                    if (mCartItemList.get(position1).getProductQuantity() < 99) {
                        mCartItemList.get(position1).setProductQuantity(mCartItemList.get(position1).getProductQuantity() + 1);
                    }
                }

                // Update Cart
                mCartDataSource.updateCart(mCartItemList.get(position1))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Integer>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(Integer integer) {
                                EventBus.getDefault().postSticky(new CalculatePriceEvent());
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(mContext, "[UPDATE CART]" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            // Delete item
            else {
                mCartDataSource.deleteCart(mCartItemList.get(position1))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Integer>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(Integer integer) {
                                notifyItemRemoved(position1);
                                EventBus.getDefault().postSticky(new CalculatePriceEvent());
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(mContext, "[DELETE CART]" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCartItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.txt_price_new)
        TextView txt_price_new;
        @BindView(R.id.txt_food_name)
        TextView txt_food_name;
        @BindView(R.id.txt_food_price)
        TextView txt_food_price;
        @BindView(R.id.txt_quantity)
        TextView txt_quantity;
        @BindView(R.id.txt_extra_price)
        TextView txt_extra_price;
        @BindView(R.id.txt_items_add)
        TextView txt_items_add;

        @BindView(R.id.img_food)
        ImageView img_food;
        @BindView(R.id.img_delete_food)
        ImageView img_delete_food;
        @BindView(R.id.img_decrease)
        ImageView img_decrease;
        @BindView(R.id.img_increase)
        ImageView img_increase;

        IOnImageViewAdapterClickListener mIOnImageViewAdapterClickListener;

        public void setIOnImageViewAdapterClickListener(IOnImageViewAdapterClickListener IOnImageViewAdapterClickListener) {
            mIOnImageViewAdapterClickListener = IOnImageViewAdapterClickListener;
        }

        Unbinder mUnbinder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mUnbinder = ButterKnife.bind(this, itemView);
            img_decrease.setOnClickListener(this);
            img_increase.setOnClickListener(this);
            img_delete_food.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == img_decrease) {
                mIOnImageViewAdapterClickListener.onCalculatePriceListener(v, getAdapterPosition(), true, false);
            } else if (v == img_increase) {
                mIOnImageViewAdapterClickListener.onCalculatePriceListener(v, getAdapterPosition(), false, false);
            } else if (v == img_delete_food) {
                mIOnImageViewAdapterClickListener.onCalculatePriceListener(v, getAdapterPosition(), true, true);
            }
        }
    }

}
