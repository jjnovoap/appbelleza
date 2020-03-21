package com.example.appbella.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbella.Common.Common;
import com.example.appbella.Database.CartDataSource;
import com.example.appbella.Database.CartDatabase;
import com.example.appbella.Database.CartItem;
import com.example.appbella.Database.LocalCartDataSource;
import com.example.appbella.ProductAndServiceDetailActivity;
import com.example.appbella.Interface.IFoodDetailOrCartClickListener;
import com.example.appbella.Model.EventBust.FoodDetailEvent;
import com.example.appbella.Model.Favorite;
import com.example.appbella.Model.Product_and_Service;
import com.example.appbella.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MyItemAdapter extends RecyclerView.Adapter<MyItemAdapter.MyViewHolder> {

    private Context mContext;
    private List<Product_and_Service> mProductAndServiceList;
    private CompositeDisposable mCompositeDisposable;
    private CartDataSource mCartDataSource;
    private DatabaseReference favorite, productAndService;

    private final String TAG = "[Favorite] ";

    public void onStop() {
        mCompositeDisposable.clear();
    }

    public MyItemAdapter(Context context, List<Product_and_Service> productandServiceList) {
        mContext = context;
        mProductAndServiceList = productandServiceList;
        mCompositeDisposable = new CompositeDisposable();
        mCartDataSource = new LocalCartDataSource(CartDatabase.getInstance(context).cartDAO());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_product_and_service, parent, false);
        favorite = FirebaseDatabase.getInstance().getReference("Favorite");
        productAndService = FirebaseDatabase.getInstance().getReference("Services");
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(mProductAndServiceList.get(position).getImage())
                .placeholder(R.drawable.app_icon).into(holder.img_food);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        holder.txt_product_and_service_name.setText(mProductAndServiceList.get(position).getName());
        holder.txt_product_and_service_price.setText(new StringBuilder(mContext.getString(R.string.money_sign))
                .append(mProductAndServiceList.get(position).getPrice()));

        if (mProductAndServiceList.get(position).getStatus().equals("1")) {
            holder.img_fav.setImageResource(R.drawable.ic_favorite_button_color_24dp);
        } else {
            holder.img_fav.setImageResource(R.drawable.ic_favorite_border_button_color_24dp);
        }

        // Event
        holder.img_fav.setOnClickListener(v -> {
            if (mProductAndServiceList.get(position).getStatus().equals("1")) {
                favorite.child(auth.getCurrentUser().getUid()).child(mProductAndServiceList.get(position).getName()).removeValue().addOnCompleteListener(task -> {
                    holder.img_fav.setImageResource(R.drawable.ic_favorite_border_button_color_24dp);
                    holder.img_fav.setTag(false);
                    Map<String, Object> status = new HashMap<>();
                    status.put("status", "0");
                    productAndService.child(mProductAndServiceList.get(position).getKey()).updateChildren(status);
                }).addOnFailureListener(e ->
                        Toast.makeText(mContext, "Error al eliminar de favoritos", Toast.LENGTH_SHORT).show());
            } else {
                Favorite favoriteServices = new Favorite(auth.getCurrentUser().getUid(),
                        Common.currentCategoryProductOrServices.getName(),
                        mProductAndServiceList.get(position).getName(),
                        mProductAndServiceList.get(position).getImage(),
                        "0",
                        Integer.parseInt(mProductAndServiceList.get(position).getId()),
                        Common.currentCategoryProductOrServices.getId(),
                        mProductAndServiceList.get(position).getPrice());

                favorite.child(auth.getCurrentUser().getUid()).child(mProductAndServiceList.get(position).getName()).setValue(favoriteServices)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                holder.img_fav.setImageResource(R.drawable.ic_favorite_button_color_24dp);
                                holder.img_fav.setTag(true);
                                Map<String, Object> status = new HashMap<>();
                                status.put("status", "1");
                                productAndService.child(mProductAndServiceList.get(position).getKey()).updateChildren(status);
                                Common.currentFavorite = favoriteServices;
                                Toast.makeText(mContext, "Añadido a favoritos", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(e -> {
                    Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }


        });

        holder.setIFoodDetailOrCartClickListener((view, i, isDetail) -> {
            if (isDetail) {

                mContext.startActivity(new Intent(mContext, ProductAndServiceDetailActivity.class));
                EventBus.getDefault().postSticky(new FoodDetailEvent(true, mProductAndServiceList.get(i)));

            } else {
                // Cart create
                CartItem cartItem = new CartItem();
                cartItem.setProductId(mProductAndServiceList.get(i).getName());
                cartItem.setCategoryId(mProductAndServiceList.get(i).getCategoryId());
                cartItem.setProductName(mProductAndServiceList.get(i).getName());
                cartItem.setProductPrice(mProductAndServiceList.get(i).getPrice());
                cartItem.setProductImage(mProductAndServiceList.get(i).getImage());
                cartItem.setProductQuantity(1);
                cartItem.setUserPhone(Common.currentUser.getUserPhone());
                cartItem.setProductAddon("NORMAL");
                cartItem.setProductSize("NORMAL");
                cartItem.setProductExtraPrice(0.0);
                cartItem.setFbid(Common.currentUser.getFbid());

                mCompositeDisposable.add(mCartDataSource.insertOrReplaceAll(cartItem)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Toast.makeText(mContext, "Añadido al carro de compras", Toast.LENGTH_SHORT).show();
                        }, throwable -> {
                            Toast.makeText(mContext, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mProductAndServiceList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.img_food)
        ImageView img_food;
        @BindView(R.id.img_fav)
        ImageView img_fav;
        @BindView(R.id.txt_product_and_service_name)
        TextView txt_product_and_service_name;
        @BindView(R.id.txt_product_and_service_price)
        TextView txt_product_and_service_price;
        @BindView(R.id.img_detail)
        ImageView img_detail;
        @BindView(R.id.img_cart)
        ImageView img_add_cart;

        IFoodDetailOrCartClickListener mIFoodDetailOrCartClickListener;

        public void setIFoodDetailOrCartClickListener(IFoodDetailOrCartClickListener IFoodDetailOrCartClickListener) {
            mIFoodDetailOrCartClickListener = IFoodDetailOrCartClickListener;
        }

        Unbinder mUnbinder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mUnbinder = ButterKnife.bind(this, itemView);

            favorite = FirebaseDatabase.getInstance().getReference().child("Favorites");
            img_detail.setOnClickListener(this);
            img_add_cart.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.img_detail) {
                mIFoodDetailOrCartClickListener.onFoodItemClickListener(v, getAdapterPosition(), true);
            } else if (v.getId() == R.id.img_cart) {
                mIFoodDetailOrCartClickListener.onFoodItemClickListener(v, getAdapterPosition(), false);
            }
        }
    }
}
