package com.example.appbella.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbella.CartListActivity;
import com.example.appbella.Common.Common;
import com.example.appbella.Database.CartDataSource;
import com.example.appbella.Database.CartDatabase;
import com.example.appbella.Database.CartItem;
import com.example.appbella.Database.LocalCartDataSource;
import com.example.appbella.ProductAndServiceDetailActivity;
import com.example.appbella.Interface.IFoodDetailOrCartClickListener;
import com.example.appbella.Model.EventBust.ServiceDetailEvent;
import com.example.appbella.Model.Favorite;
import com.example.appbella.Model.Service;
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

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> {

    private Context mContext;
    private List<Service> mServiceList;
    private CompositeDisposable mCompositeDisposable;
    private CartDataSource mCartDataSource;
    private DatabaseReference favorite, productAndService;
    public void onStop() {
        mCompositeDisposable.clear();
    }

    public ServiceAdapter(Context context, List<Service> serviceList) {
        mContext = context;
        mServiceList = serviceList;
        mCompositeDisposable = new CompositeDisposable();
        mCartDataSource = new LocalCartDataSource(CartDatabase.getInstance(context).cartDAO());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_service, parent, false);
        favorite = FirebaseDatabase.getInstance().getReference("Favorite");
        productAndService = FirebaseDatabase.getInstance().getReference("Services");
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(mServiceList.get(position).getImage())
                .placeholder(R.drawable.app_icon).into(holder.img_food);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        holder.txt_product_and_service_name.setText(mServiceList.get(position).getName());
        holder.txt_product_and_service_price.setText(new StringBuilder(mContext.getString(R.string.money_sign))
                .append(" ").append(mServiceList.get(position).getPrice()));

        if (mServiceList.get(position).getStatus().equals("1")) {
            holder.img_fav.setImageResource(R.drawable.ic_favorite_button_color_24dp);
        } else {
            holder.img_fav.setImageResource(R.drawable.ic_favorite_border_button_color_24dp);
        }

        if (Long.parseLong(String.valueOf(holder.txt_quantity.getText())) == 1) {
            holder.img_decrease.setVisibility(View.GONE);
            holder.img_delete_food.setVisibility(View.VISIBLE);
        }else{
            holder.img_decrease.setVisibility(View.VISIBLE);
            holder.img_delete_food.setVisibility(View.GONE);
        }


        // Event
        holder.img_fav.setOnClickListener(v -> {
            if (mServiceList.get(position).getStatus().equals("1")) {
                favorite.child(auth.getCurrentUser().getUid()).child(mServiceList.get(position).getName()).removeValue().addOnCompleteListener(task -> {
                    holder.img_fav.setImageResource(R.drawable.ic_favorite_border_button_color_24dp);
                    holder.img_fav.setTag(false);
                    Map<String, Object> status = new HashMap<>();
                    status.put("status", "0");
                    productAndService.child(mServiceList.get(position).getKey()).updateChildren(status);
                }).addOnFailureListener(e ->
                        Toast.makeText(mContext, "Error al eliminar de favoritos", Toast.LENGTH_SHORT).show());
            } else {
                Favorite favoriteServices = new Favorite(auth.getCurrentUser().getUid(),
                        "",
                        mServiceList.get(position).getName(),
                        mServiceList.get(position).getImage(),
                        "0",
                        Integer.parseInt(mServiceList.get(position).getId()),
                        1,
                        mServiceList.get(position).getPrice());

                favorite.child(auth.getCurrentUser().getUid()).child(mServiceList.get(position).getName()).setValue(favoriteServices)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                holder.img_fav.setImageResource(R.drawable.ic_favorite_button_color_24dp);
                                holder.img_fav.setTag(true);
                                Map<String, Object> status = new HashMap<>();
                                status.put("status", "1");
                                productAndService.child(mServiceList.get(position).getKey()).updateChildren(status);
                                Common.currentFavorite = favoriteServices;
                                Toast.makeText(mContext, "Añadido a favoritos", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(e -> {
                    Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }


        });

        holder.img_increase.setOnClickListener(v -> {
            int q = Integer.parseInt(holder.txt_quantity.getText().toString());
            q += 1;
            holder.txt_quantity.setText(q + "");
            notifyDataSetChanged();
        });

        holder.img_decrease.setOnClickListener(v -> {
            int q = Integer.parseInt(holder.txt_quantity.getText().toString());
            if (q > 0) {
                q -= 1;
                holder.txt_quantity.setText(q + "");
                notifyDataSetChanged();
            }

        });


        holder.setIFoodDetailOrCartClickListener((view, i, isDetail, isDelete, isAdd) -> {
            CartItem cartItem = null;
            if (isDetail) {
                mContext.startActivity(new Intent(mContext, ProductAndServiceDetailActivity.class));
                EventBus.getDefault().postSticky(new ServiceDetailEvent(true, mServiceList.get(i)));

            } else {
                long j = Long.parseLong(holder.txt_quantity.getText().toString());
                // Cart create
                cartItem = new CartItem();
                cartItem.setProductId(mServiceList.get(i).getName());
                cartItem.setCategoryId(mServiceList.get(i).getCategoryId());
                cartItem.setProductName(mServiceList.get(i).getName());
                cartItem.setProductPrice(mServiceList.get(i).getPrice());
                cartItem.setProductImage(mServiceList.get(i).getImage());
                cartItem.setProductQuantity(Math.toIntExact(j));
                cartItem.setUserPhone(Common.currentUser.getUserPhone());
                cartItem.setProductAddon("NORMAL");
                cartItem.setProductSize("NORMAL");
                cartItem.setProductExtraPrice((long) 0);
                cartItem.setFbid(Common.currentUser.getFbid());

                mCompositeDisposable.add(mCartDataSource.insertOrReplaceAll(cartItem)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            //hay que verificar aqui si el item esta en el carrito de compras
                            if (isDelete){
                                Toast.makeText(mContext, "Elemento eliminado del carro de compras", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(mContext, "Añadido al carro de compras", Toast.LENGTH_SHORT).show();
                            }
                        }, throwable -> {
                            Toast.makeText(mContext, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }));

            }

            if (isDelete) {
                mCompositeDisposable.delete(mCartDataSource.deleteCart(cartItem)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mServiceList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.img_food)
        ImageView img_food;
        @BindView(R.id.img_fav)
        ImageView img_fav;
        @BindView(R.id.txt_quantity)
        TextView txt_quantity;
        @BindView(R.id.txt_product_and_service_name)
        TextView txt_product_and_service_name;
        @BindView(R.id.txt_product_and_service_price)
        TextView txt_product_and_service_price;
        @BindView(R.id.img_detail)
        TextView img_detail;
        @BindView(R.id.img_cart)
        ImageView img_add_cart;
        @BindView(R.id.img_delete_food)
        ImageView img_delete_food;
        @BindView(R.id.img_decrease)
        ImageView img_decrease;
        @BindView(R.id.img_increase)
        ImageView img_increase;

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
            img_increase.setOnClickListener(this);
            img_delete_food.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.img_detail) {
                mIFoodDetailOrCartClickListener.onFoodItemClickListener(v, getAdapterPosition(), true, false, false);
            } else if (v.getId() == R.id.img_cart) {
                mIFoodDetailOrCartClickListener.onFoodItemClickListener(v, getAdapterPosition(), false, false, true);
            }else if (v.getId() == R.id.img_increase) {
                mIFoodDetailOrCartClickListener.onFoodItemClickListener(v, getAdapterPosition(), false, false, true);
            } else if (v == img_delete_food) {
                mIFoodDetailOrCartClickListener.onFoodItemClickListener(v, getAdapterPosition(), false, true, false);
            }

        }
    }
}
